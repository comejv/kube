package kube.view.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import kube.model.ModelColor;
import kube.view.GUIColors;

public class HexIcon extends Icon {
    private boolean isActionable;
    private boolean isHovered;
    private boolean isPressed;

    private double offsetX;
    private double offsetY;

    private ModelColor color;

    public HexIcon(BufferedImage img, boolean actionable) {
        super(img);
        this.isActionable = actionable;
    }

    public HexIcon(BufferedImage img, boolean actionable, ModelColor color) {
        super(img);
        recolor(color);
        this.isActionable = actionable;
        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isActionable() && isHovered) { // Draw darker image
            float factor = isPressed ? 0.75f : 1.25f;
            float[] scales = { factor }; // Multiply all bands of each pixel by factor
            float[] offsets = new float[4]; // Add to all bands of each pixel an offset of 0
            RescaleOp rop = new RescaleOp(scales, offsets, null);
            g2d.drawImage(getImage(), rop, (int) offsetX, (int) offsetY);
        } else { // Draw the original image
            g2d.drawImage(getImage(), (int) offsetX, (int) offsetY, null);
        }
    }

    public HexIcon clone() {
        return new HexIcon(getImage(), isActionable());
    }

    public void recolor(ModelColor color) {
        switch (color) {
            case WHITE:
                super.recolor(GUIColors.WHITE_HEX);
                break;
            case NATURAL:
                super.recolor(GUIColors.NATURAL_HEX);
                break;
            case RED:
                super.recolor(GUIColors.RED_HEX);
                break;
            case GREEN:
                super.recolor(GUIColors.GREEN_HEX);
                break;
            case BLUE:
                super.recolor(GUIColors.BLUE_HEX);
                break;
            case YELLOW:
                recolor(GUIColors.YELLOW_HEX);
                break;
            case BLACK:
                recolor(GUIColors.BLACK_HEX);
                break;
            default:
                break;
        }
        this.color = color;
    }

    public void setActionable(boolean b) {
        isActionable = b;
    }

    public void setPressed(boolean b) {
        isPressed = b;
        repaint();
    }

    public void setHovered(boolean b) {
        isHovered = b;
        repaint();
    }

    public boolean isPressed() {
        return isPressed;
    }

    public boolean isActionable() {
        return isActionable;
    }

    public void setOffset(double x, double y) {
        offsetX = x - getImage().getWidth() / 2;
        offsetY = y - getImage().getHeight() / 2;
        repaint();
    }

    public double getXOffset() {
        return offsetX;
    }

    public double getYOffset() {
        return offsetY;
    }

    public BufferedImage getImage() {
        return super.getImage();
    }

    public ModelColor getColor() {
        return color;
    }
}
