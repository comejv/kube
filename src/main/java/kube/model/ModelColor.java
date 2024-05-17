package kube.model;

import java.util.Random;


import java.util.Comparator;

public enum ModelColor {

    /**********
     * ENUMERATION
     **********/

    EMPTY(0),
    WHITE(1),
    NATURAL(2),
    RED(3),
    GREEN(4),
    BLUE(5),
    YELLOW(6),
    BLACK(7);

    /**********
     * ATTRIBUTE
     **********/

    int colorCode;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the Color
     * 
     * @param colorCode
     */
    ModelColor(int colorCode) {
        setColorCode(colorCode);
    }

    /**********
     * SETTER
     **********/

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    /**********
     * GETTER
     **********/

    public int getColorCode() {
        return this.colorCode;
    }

    /**********
     * METHODS
     **********/

    /**
     * Give the color corresponding to the color code
     * 
     * @param colorCode the color code
     * @return the color that corresponds to the color code
     */
    public static ModelColor getColor(int colorCode) {
        for (ModelColor c : ModelColor.values()) {
            if (c.getColorCode() == colorCode) {
                return c;
            }
        }
        return EMPTY;
    }

    /**
     * Give all the colored colors
     * 
     * @return all the colored colors
     */
    public static ModelColor[] getAllColored() {
        return new ModelColor[] { RED, GREEN, BLUE, YELLOW, BLACK };
    }

    public static ModelColor[] getAllColoredAndJokers() {
        return new ModelColor[] { RED, GREEN, BLUE, YELLOW, BLACK, WHITE, NATURAL };
    }

    /**
     * Give a random color
     * 
     * @return a random color
     */
    public static ModelColor getRandomColor() {
        Random r = new Random();
        return ModelColor.values()[r.nextInt(ModelColor.values().length)];
    }

    /**
     * Compare the colors by their color code
     */
    public static Comparator<ModelColor> compareByValue = new Comparator<ModelColor>() {

        /**
         * Compare the colors by their color code
         * 
         * @param c1 first color
         * @param c2 second color
         * @return the difference between the color codes
         */
        @Override
        public int compare(ModelColor c1, ModelColor c2) {
            return c1.getColorCode() - c2.getColorCode();
        }
    };

    /**
     * Display the color
     * 
     * @return the String that represents the color
     */
    public String forDisplay() {

        switch (this) {
            case EMPTY:
                return " ";
            case WHITE:
                return "\u001B[37m" + "▣" + "\u001B[0m";
            case NATURAL:
                return "\u001B[90m" + "▣" + "\u001B[0m";
            case RED:
                return "\u001B[31m" + "▣" + "\u001B[0m";
            case GREEN:
                return "\u001B[32m" + "▣" + "\u001B[0m";
            case BLUE:
                return "\u001B[34m" + "▣" + "\u001B[0m";
            case YELLOW:
                return "\u001B[33m" + "▣" + "\u001B[0m";
            case BLACK:
                return "\u001B[35m" + "▣" + "\u001B[0m";
            default:
                return " ";
        }
    }

    /**
     * Give a string representation of the color for saving
     * 
     * @return a string representation of the color for saving
     */
    public String forSave() {
        return getColorCode() + "";
    }

    public static ModelColor fromSave(String save) {
        return getColor(Integer.parseInt(save));
    }
}