package kube.configuration;

public class Config {
    private static boolean mute = true;
    private static final boolean debug = true;
    private static final boolean showBorders = false;
    public static final String SAVING_PATH_DIRECTORY = "saves/";

    private static String language = "fr_FR";
    private static int width = 1600;
    private static int height = 900;

    public static boolean isJar() {
        return System.getProperty("java.class.path").contains(".jar");
    }

    public static boolean isMute() {
        return mute;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static boolean showBorders() {
        return showBorders;
    }

    public static void toggleMute() {
        mute = !mute;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static String getLanguage() {
        return language;
    }

    public static void setLanguage(String lang) {
        language = lang;
    }

    public static void setResolution(int width, int height){
        setWidth(width);
        setHeight(height);
    }

    public static void setWidth(int newWidth){
        width = newWidth;
    }

    public static void setHeight(int newHeight){
        height = newHeight;
    }

    public static void debug(Object... args) {
        if (debug) {
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

}
