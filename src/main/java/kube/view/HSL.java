package kube.view;

import java.awt.Color;

public class HSL {
    // TODO : refactor this class to make it more readable
    private double hue;
    private double saturation;
    private double luminance;

    public HSL(double h, double s, double l) {
        fromHSL(h, s, l);
    }

    public HSL(double a, double b, double c, boolean RGB) {
        if (!RGB) {
            fromHSL(a, b, c);
        } else {
            fromRGB((int) a, (int) b, (int) c);
        }
    }

    public void fromHSL(double hue, double saturation, double luminance) {
        if (hue > 1) {
            hue /= 360;
        }
        this.hue = hue;
        this.saturation = saturation;
        this.luminance = luminance;
    }

    // public static HSL fromRGB(int red, int green, int blue) {
    public void fromRGB(int red, int green, int blue) {
        double r = red / 255f;
        double g = green / 255f;
        double b = blue / 255f;
        double vmax = Math.max(Math.max(r, g), b);
        double vmin = Math.min(Math.min(r, g), b);
        this.luminance = (vmax + vmin) / 2;

        if (vmax == vmin) { // Achromatic
            this.hue = 0;
            this.luminance = vmax;
            return;
        }

        double d = vmax - vmin;
        this.saturation = this.luminance > 0.5 ? d / (2 - vmax - vmin) : d / (vmax + vmin);

        if (vmax == r) {
            this.hue = (g - b) / d + (g < b ? 6 : 0);
        } else if (vmax == g) {
            this.hue = (b - r) / d + 2;
        } else { // vmax == b
            this.hue = (r - g) / d + 4;
        }

        // Normalize hue to the range [0, 1]
        this.hue /= 6;
    }

    public HSL(int rgb) {
        fromRGB((rgb >> 16) & 0xFF, (rgb >> 8 & 0xFF), rgb & 0xFF);
    }

    public double getHue() {
        return hue;
    }

    public double getSaturation() {
        return saturation;
    }

    public double getLuminance() {
        return luminance;
    }

    public void setHue(double h) {
        this.hue = h;
    }

    public void setSaturation(double s) {
        this.saturation = s;
    }

    public void setLuminance(double l) {
        this.luminance = l;
    }

    public int toRGB() {
        int[] rgbArray = toRGBArray();
        return (rgbArray[0] << 16) | (rgbArray[1] << 8) | rgbArray[2];
    }

    public int[] toRGBArray() {
        int r, g, b;

        if (saturation == 0) {
            r = g = b = (int) Math.round(luminance * 255); // achromatic
        } else {
            double q = luminance < 0.5 ? luminance * (1 + saturation) : luminance + saturation - luminance * saturation;
            double p = 2 * luminance - q;
            r = (int) Math.round(hueToRgb(p, q, hue + 1f / 3f) * 255);
            g = (int) Math.round(hueToRgb(p, q, hue) * 255);
            b = (int) Math.round(hueToRgb(p, q, hue - 1f / 3f) * 255);
        }

        return new int[] { r, g, b };
    }

    public Color toColor() {
        return new Color((0xFF << 24) | toRGB());
    }

    private double hueToRgb(double p, double q, double t) {
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
}
