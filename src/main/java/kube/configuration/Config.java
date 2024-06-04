package kube.configuration;

import kube.model.Game;

public class Config {

    /**********
     * CONFIGURATION CONSTANTS
     **********/

    public static final boolean DEBUG = true;
    public static final boolean SHOW_BORDERS = false;
    public static final String SAVING_PATH_DIRECTORY = "saves/";
    public static final String SAVING_FILE_EXTENSION = ".ser";
    public static final String TEXTURED_MODE = "Textured";
    public static final String SYMBOL_MODE = "Symbol";

    public static final int INIT_WIDTH = 1600;
    public static final int INIT_HEIGHT = 900;

    /**********
     * CONFIGURATION ATTRIBUTES
     **********/

    private static String language = "fr_FR";
    private static String mode = TEXTURED_MODE;
    private static double UIScale = 1;
    private static boolean mute = true;

    private static String serverAddress;
    private static int serverPort;

    /**********
     * CONFIGURATION SETTER
     **********/

    public static void setLanguage(String lang) {
        language = lang;
    }

    public static void setUIScale(double size) {
        UIScale = size;
    }

    public static void setMode(String str) {
        mode = str;
    }

    public static void resetUIScale() {
        UIScale = 1;
    }

    public static void setHostIP(String address) {
        serverAddress = address;
    }

    public static void setHostPort(int port) {
        serverPort = port;
    }

    /**********
     * CONFIGURATION GETTER
     **********/

    public static String getLanguage() {
        return language;
    }

    public static String getMode() {
        return mode;
    }

    public static double getUIScale() {
        return UIScale;
    }

    public static String getHostIP() {
        return serverAddress;
    }

    public static int getHostPort() {
        return serverPort;
    }

    /**********
     * CONFIGURATION METHODS
     **********/

    /**
     * Check if the program is running from a jar
     * 
     * @return true if the program is running from a jar, false otherwise
     */
    public static boolean isJar() {
        return System.getProperty("java.class.path").contains(".jar");
    }

    /**
     * Check if the program is currently muted
     * 
     * @return true if the program is muted, false otherwise
     */
    public static boolean isMute() {
        return mute;
    }

    /**
     * Toggle the mute state
     */
    public static void toggleMute() {
        mute = !mute;
    }

    /**
     * Print debug message
     * 
     * @param args the message to print
     */
    public static void debug(Object... args) {
        if (DEBUG) {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            if (stackTraceElements.length > 2) {
                System.out.print("Debug at : " + stackTraceElements[2].getClassName() + " : "
                        + stackTraceElements[2].getMethodName() + "(" + stackTraceElements[2].getLineNumber() + ")"
                        + " : ");
            }
            // Print args
            for (Object arg : args) {
                System.out.print(arg + " ");
            }

            System.out.println();
        }
    }

    /**
     * Print error message
     * 
     * @param args the message to print
     */
    public static void error(Object... args) {
        if (DEBUG) {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            if (stackTraceElements.length > 2) {
                System.out.print("Error at : " + stackTraceElements[2].getClassName() + " : "
                        + stackTraceElements[2].getMethodName() + "(" + stackTraceElements[2].getLineNumber() + ")"
                        + " : ");
            }
            // Print args
            for (Object arg : args) {
                System.out.print(arg + " ");
            }

            System.out.println();
        }
    }
}
