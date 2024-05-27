package kube.configuration;

public class Config {

    private static boolean isJar;
    private static boolean mute = true;
    private static final boolean debug = true;
    private static final boolean showBorders = false;
    public static final String SAVING_PATH_DIRECTORY = "saves/";

    public Config() {
        isJar = System.getProperty("java.class.path").contains(".jar");
    }

    public static boolean isJar() {
        return isJar;
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
