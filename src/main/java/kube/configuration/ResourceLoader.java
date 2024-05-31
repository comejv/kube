package kube.configuration;

// Import java classes
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

public class ResourceLoader {

    /**********
     * CONSTANTS
     **********/

    public static final String TEXT_FOLDER = "texts/";
    public static final String TEXT_EXTENSION = ".txt";
    public static final String IMAGE_FOLDER = "images/";
    public static final String IMAGE_EXTENSION = ".png";
    public static final String NOT_FIND_FILE = "notFound";

    /**********
     * ATTRIBUTE
     **********/

    private static volatile HashMap<String, byte[]> resources;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the class ResourceLoader
     */
    public ResourceLoader() {
        resources = new HashMap<>(10);
    }

    /**********
     * METHODS
     **********/

    /**
     * Get a resource as a stream
     * 
     * @param relativePath the relative path
     * @return the resource as a stream
     */
    public static InputStream getResourceAsStream(String relativePath) {

        byte[] temp, byteArray;
        int bytesRead;
        InputStream resourceStream;
        ByteArrayOutputStream buffer;

        if (resources.containsKey(relativePath)) {
            return new ByteArrayInputStream(resources.get(relativePath));
        }

        resourceStream = null;

        if (Configuration.isJar()) {
            resourceStream = ResourceLoader.class.getClassLoader().getResourceAsStream(relativePath);
            if (resourceStream == null) {
                Configuration.error("Resource " + relativePath + " not found.");
                return null;
            }
        } else {
            try {
                resourceStream = new FileInputStream(relativePath);
            } catch (FileNotFoundException e1) {
                Configuration.error("File " + relativePath + " not found.");
                return null;
            }
        }

        try {
            buffer = new ByteArrayOutputStream();
            temp = new byte[1024];

            while ((bytesRead = resourceStream.read(temp)) != -1) {
                buffer.write(temp, 0, bytesRead);
            }
            byteArray = buffer.toByteArray();

            // Store the byte array in the map
            resources.put(relativePath, byteArray);

            // Return a new ByteArrayInputStream from the buffered byte array
            return new ByteArrayInputStream(byteArray);
        } catch (IOException e) {
            Configuration.error("copying resource");
            return null;
        }
    }

    /**
     * Get a buffered image form a file
     * 
     * @param fileName the name of the file
     * @return the buffered image
     */
    public static BufferedImage getBufferedImage(String fileName) {
        
        String imgPath;
        InputStream in;

        imgPath = IMAGE_FOLDER + fileName + IMAGE_EXTENSION;

        try {
            in = ResourceLoader.getResourceAsStream(imgPath);
            if (in != null) {
                return ImageIO.read(in);
            }
            Configuration.debug("Attempting to use placeholder for image...");
            in = ResourceLoader.class.getClassLoader().getResourceAsStream(IMAGE_FOLDER + NOT_FIND_FILE + IMAGE_EXTENSION);
            if (in != null) {
                return ImageIO.read(in);
            }
        } catch (Exception e) {
            Configuration.error("could not load image " + fileName);
            System.exit(1);
        }

        Configuration.error("While handling missing resource " + imgPath + " new error occured :");
        Configuration.error("Missing required resource images/notFound.png");
        System.exit(1);
        return null; // should never happen
    }

    /**
     * Get a text from a file
     * 
     * @param fileName the name of the file
     * @return the text
     */
    public static String getText(String fileName) {

        String result, folder, relativePath;
        BufferedReader buf;
        InputStream resource;

        folder = fileName.equals("credits") ? "" : TEXT_FOLDER + Configuration.getLanguage();
        relativePath = folder + "/" + fileName + TEXT_EXTENSION;
        resource = getResourceAsStream(relativePath);

        if (resource == null) {
            resource = getResourceAsStream(TEXT_FOLDER + "notFound" + TEXT_EXTENSION);
        }

        buf = new BufferedReader(new InputStreamReader(resource));
        result = buf.lines().collect(Collectors.joining("\n"));
        return result;
    }
}
