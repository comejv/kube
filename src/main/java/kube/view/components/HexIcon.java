package kube.view.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.RescaleOp;
import kube.configuration.Config;


import kube.configuration.ResourceLoader;
import kube.model.ModelColor;
import kube.model.Player;

public class HexIcon extends Icon {

    private boolean isActionable;
    private boolean isHovered;
    private boolean isPressed;
    private Point position;
    private double offsetX;
    private double offsetY;

    private ModelColor color;
    private Player player;

    private static int WIDTH = 40;
    private static int HEIGHT = 40;
    private static final int SCALE = 1;

    private double scale;
    private float brightness;

    public HexIcon(ModelColor color, boolean actionable, Player player, int width, int height, double scale) {
        super(ResourceLoader.getBufferedImage(getImageName(color)));
        this.color = color;
        this.isActionable = actionable;
        this.player = player;
        this.brightness = 1.0f;
        setActionable(actionable);
        this.scale = scale;
        if (color == null) {
            resizeIcon(1, (int) (height * scale));
        } else {
            resizeIcon((int) (width * scale), (int) (height * scale));
        }
    }

    public HexIcon(ModelColor color, boolean actionable, Player player) {
        this(color, actionable, player, WIDTH, HEIGHT, SCALE);
    }

    public HexIcon(ModelColor color, boolean actionable) {
        this(color, actionable, null, WIDTH, HEIGHT, SCALE);
    }

    public HexIcon(ModelColor color) {
        this(color, false, null, WIDTH, HEIGHT, SCALE);
    }

    public HexIcon() {
        this(ModelColor.EMPTY, false, null, WIDTH, HEIGHT, SCALE);
    }

    public HexIcon(ModelColor color, int width, int height) {
        this(ModelColor.EMPTY, false, null, width, height, SCALE);
    }

    public HexIcon(ModelColor color, boolean actionable, double scale) {
        this(color, actionable, null, WIDTH, HEIGHT, scale);
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

        } else if (isActionable()) {
            float[] scales = { brightness }; // Multiply all bands of each pixel by factor
            float[] offsets = new float[4]; // Add to all bands of each pixel an offset of 0
            RescaleOp rop = new RescaleOp(scales, offsets, null);
            g2d.drawImage(getImage(), rop, (int) offsetX, (int) offsetY);
        } else if (color != null) { // Draw the original image
            g2d.drawImage(getImage(), (int) offsetX, (int) offsetY, null);
        }
    }

    private static String getImageName(ModelColor color) {
        if (color == null) {
            return "hexaInvisible";
        }
        String mode = Config.getMode();
        switch (color) {
            case WHITE:
                return "hexaWhite"+mode;
            case NATURAL:
                return "hexaBrown"+mode;
            case RED:
                return "hexaRed"+mode;
            case GREEN:
                return "hexaGreen"+mode;
            case BLUE:
                return "hexaBlue"+mode;
            case YELLOW:
                return "hexaYellow"+mode;
            case BLACK:
                return "hexaBlack"+mode;
            case EMPTY:
                return "hexaWire";
            default:
                return "hexaInvisible";
        }
    }

    public HexIcon clone() {
        HexIcon clone = new HexIcon(getColor(), isActionable(), getPlayer(), getImage().getWidth(),
                getImage().getHeight(), getScale());
        clone.setPosition(position);
        return clone;
    }

    public final void setScale(double scale) {
        this.scale = scale;
        if (color == null) {
            resizeIcon(1, (int) (HEIGHT * scale));
        } else {
            resizeIcon((int) (WIDTH * scale), (int) (HEIGHT * scale));
        }
    }

    public final void updateSize() {
        if (color == null) {
            resizeIcon(1, (int) (HEIGHT * scale));
        } else {
            resizeIcon((int) (WIDTH * scale), (int) (HEIGHT * scale));
        }
    }

    public final double getScale() {
        return scale;
    }

    public final void setActionable(boolean b) {
        isActionable = b;
    }

    public final void setPressed(boolean b) {
        isPressed = b;
        repaint();
    }

    public final void setHovered(boolean b) {
        isHovered = b;
        repaint();
    }

    public final void setDefault() {
        isPressed = false;
        isHovered = false;
        repaint();
    }

    public void setBrightness(float brightness) {
        if (getColor() == ModelColor.RED) {
            this.brightness = brightness * 1.2f;
        } else {
            this.brightness = brightness;
        }
    }

    public boolean isPressed() {
        return isPressed;
    }

    public boolean isActionable() {
        return isActionable;
    }

    public final void setOffset(double x, double y) {
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

    public ModelColor getColor() {
        return color;
    }

    public final void setPosition(Point p) {
        position = p;
    }

    public Point getPosition() {
        return position;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player p) {
        player = p;
    }

    public static final void setStaticSize(int s) {
        HexIcon.HEIGHT = s;
        HexIcon.WIDTH = s;
    }

    public String toString() {
        return "HexIcon, isActionable: " + isActionable + "\nposition: " + getPosition() + "\ncolor: " + getColor()
                + "\nicon size : " + getImage().getWidth() + " " + getImage().getHeight() + "\nscale : " + scale;
    }
}
