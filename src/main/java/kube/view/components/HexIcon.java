package kube.view.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class HexIcon extends Icon {
    private boolean isActionable;
    private boolean isHovered;
    private boolean isPressed;

    private int offsetX;
    private int offsetY;

    public HexIcon(BufferedImage img, boolean actionable) {
        super(img);
        this.isActionable = actionable;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isActionable() && isHovered) { // Draw darker image
            float factor = isPressed ? 0.75f : 1.25f;
            float[] scales = { factor };
            float[] offsets = new float[4];
            RescaleOp rop = new RescaleOp(scales, offsets, null);
            g2d.drawImage(getImage(), rop, offsetX, offsetY);
        } else { // Draw the original image
            g2d.drawImage(getImage(), offsetX, offsetY, null);
        }

    }

    public HexIcon clone() {
        return new HexIcon(getImage(), isActionable());
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

    public void setOffset(int x, int y) {
        offsetX = x;
        offsetY = y;
        repaint();
    }

    public int getXOffset() {
        return offsetX;
    }

    public int getYOffset() {
        return offsetY;
    }
}
