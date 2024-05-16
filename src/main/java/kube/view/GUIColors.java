package kube.view;

import java.awt.Color;

public class GUIColors {
    public static final Color ACCENT_LIGHT = new Color(0x145591);
    public static final Color ACCENT = new Color(0x003049);
    public static final Color ACCENT_HOVER = new Color(0x002437);
    public static final Color GAME_BG_DARK = new Color(0x696D70);
    public static final Color GAME_BG_LIGHT = new Color(0xEEEEEE);
    public static final Color TEXT_HOVER = new Color(0xC1C1C1);
    public static final Color TEXT = new Color(0xFFFFFF);
    public static final Color GAME_BG = new Color(0x7D939F);

    /**
     * Converts HSL color model to RGB.
     *
     * @param h Hue component in range [0, 1]
     * @param s Saturation component in range [0, 1]
     * @param l Lightness component in range [0, 1]
     * @return RGB components as an array of integers
     */
    public static int[] hslToRgb(double h, double s, double l) {
        int r, g, b;

        if (s == 0) {
            r = g = b = (int) Math.round(l * 255); // achromatic
        } else {
            double q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            double p = 2 * l - q;
            r = (int) Math.round(hueToRgb(p, q, h + 1f / 3f) * 255);
            g = (int) Math.round(hueToRgb(p, q, h) * 255);
            b = (int) Math.round(hueToRgb(p, q, h - 1f / 3f) * 255);
        }

        return new int[] { r, g, b };
    }

    public static int[] hslToRgb(double[] hsl) {
        return hslToRgb(hsl[0], hsl[1], hsl[2]);
    }

    private static double hueToRgb(double p, double q, double t) {
        if (t < 0)
            t += 1;
        if (t > 1)
            t -= 1;
        if (t < 1f / 6f) {
            return p + (q - p) * 6 * t;
        }
        if (t < 1f / 2f) {
            return q;
        }
        if (t < 2f / 3f) {
            return p + (q - p) * (2f / 3f - t) * 6;
        }
        return p;
    }

    /**
     * Converts RGB color model to HSL.
     *
     * @param r Red component in range [0, 255]
     * @param g Green component in range [0, 255]
     * @param b Blue component in range [0, 255]
     * @return HSL components as an array of doubles
     */
    public static double[] rgbToHsl(int ri, int gi, int bi) {
        float r = ri / 255f; // Ensure division is floating-point
        float g = gi / 255f;
        float b = bi / 255f;
        double vmax = Math.max(Math.max(r, g), b);
        double vmin = Math.min(Math.min(r, g), b);
        double h, s, l = (vmax + vmin) / 2;

        if (vmax == vmin) {
            return new double[] { 0, 1, l }; // achromatic
        }

        double d = vmax - vmin;
        s = l > 0.5 ? d / (2 - vmax - vmin) : d / (vmax + vmin);

        if (vmax == r) {
            h = (g - b) / d + (g < b ? 6 : 0);
        } else if (vmax == g) {
            h = (b - r) / d + 2;
        } else { // vmax == b
            h = (r - g) / d + 4;
        }

        // Normalize hue to the range [0, 1]
        h /= 6;

        return new double[] { h, s, l };
    }

    public static double[] rgbToHsl(int rgb) {
        return rgbToHsl((rgb >> 16) & 0xFF, (rgb >> 8 & 0xFF), rgb & 0xFF);
    }
}
