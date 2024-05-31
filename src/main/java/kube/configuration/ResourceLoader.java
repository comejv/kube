package kube.configuration;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
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
    // TODO : refactor this class to make it more readable
    private static volatile HashMap<String, byte[]> resources;

    public ResourceLoader() {
        resources = new HashMap<>(10);
    }

    public static InputStream getResourceAsStream(String relativePath) {
        if (resources.containsKey(relativePath)) {
            return new ByteArrayInputStream(resources.get(relativePath));
        }
        InputStream resourceStream = null;
        if (Configuration.isJar()) {
            resourceStream = ResourceLoader.class.getClassLoader().getResourceAsStream(relativePath);
            if (resourceStream == null) {
                System.err.println("Resource " + relativePath + " not found.");
                return null;
            }
        } else {
            try {
                resourceStream = new FileInputStream(relativePath);
            } catch (FileNotFoundException e1) {
                System.err.println("File " + relativePath + " not found.");
                return null;
            }
        }

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            int bytesRead;
            while ((bytesRead = resourceStream.read(temp)) != -1) {
                buffer.write(temp, 0, bytesRead);
            }
            byte[] byteArray = buffer.toByteArray();

            // Store the byte array in the map
            resources.put(relativePath, byteArray);

            // Return a new ByteArrayInputStream from the buffered byte array
            return new ByteArrayInputStream(byteArray);
        } catch (IOException e) {
            System.err.println("Error copying resource");
            return null;
        }
    }

    public static BufferedImage getBufferedImage(String nom) {
        String imgPath = "images/" + nom + ".png";
        InputStream in;
        try {
            in = ResourceLoader.getResourceAsStream(imgPath);
            if (in != null) {
                return ImageIO.read(in);
            }
            Configuration.debug("Attempting to use placeholder for image...");
            in = ResourceLoader.class.getClassLoader().getResourceAsStream("images/notFound.png");
            if (in != null) {
                return ImageIO.read(in);
            }
        } catch (Exception e) {
            System.err.println("Error : could not load image " + nom);
            System.exit(1);
        }
        System.err.println("While handling missing resource " + imgPath + " new error occured :");
        System.err.println("Missing required resource images/notFound.png");
        System.exit(1);
        return null; // should never happen
    }

    public static String getText(String name) {
        String result;
        String folder = name.equals("credits") ? "" : "texts/" + Configuration.getLanguage();
        String relativePath = folder + "/" + name + ".txt";
        InputStream resource = getResourceAsStream(relativePath);
        if (resource == null) {
            resource = getResourceAsStream("texts/notFound.txt");
        }
        BufferedReader buf = new BufferedReader(new InputStreamReader(resource));
        result = buf.lines().collect(Collectors.joining("\n"));
        return result;
    }
}
