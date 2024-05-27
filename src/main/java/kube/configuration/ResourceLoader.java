package kube.configuration;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

public class ResourceLoader {
    static public BufferedInputStream getResourceAsStream(String relativePath) {
        InputStream resourceStream = null;
        if (Config.isJar()) {
            resourceStream = ResourceLoader.class.getClassLoader().getResourceAsStream(relativePath);
            if (resourceStream == null) {
                System.err.println("Resource " + relativePath + " not found.");
            }
        } else {
            try {
                resourceStream = new FileInputStream(relativePath);
            } catch (FileNotFoundException e1) {
                System.err.println("File " + relativePath + " not found.");
                resourceStream = null;
            }
        }
        return resourceStream != null ? new BufferedInputStream(resourceStream) : null;
    }

    static public BufferedImage getBufferedImage(String nom) {
        String imgPath = "images/" + nom + ".png";
        InputStream in;
        try {
            in = ResourceLoader.getResourceAsStream(imgPath);
            if (in == null) {
                Config.debug("Attempting to use placeholder for image...");
                in = ResourceLoader.class.getClassLoader().getResourceAsStream("images/notFound.png");
                if (in == null) {
                    System.err.println("While handling missing resource " + imgPath + " new error occured :");
                    System.err.println("Missing required resource images/notFound.png");
                    System.exit(1);
                }
            }

            return ImageIO.read(in);
        } catch (FileNotFoundException e) {
            System.err.println("Erreur: fichier " + imgPath + " introuvable");
        } catch (NullPointerException e) {
            System.err.println("Erreur: ressource " + imgPath + " introuvable");
        } catch (Exception e) {
            System.err.println("ERREUR: impossible de charger l'image " + nom);
        }
        // If loading failed
        return null;
    }

    static public String getText(String name) {
        String result;
        String relativePath = "texts/" + Config.getLanguage() + "/" + name + ".txt";
        BufferedReader buf = new BufferedReader(new InputStreamReader(getResourceAsStream(relativePath)));
        result = buf.lines().collect(Collectors.joining("\n"));
        return result;
    }
}
