package kube.view.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import kube.configuration.ResourceLoader;
import kube.model.ModelColor;
import kube.model.Player;

public class HexIcon extends Icon {


    // TODO : refactor this class to make it more readable
    private boolean isActionable;
    private boolean isHovered;
    private boolean isPressed;
    private Point position;
    private double offsetX;
    private double offsetY;

    private ModelColor color;
    private Player player;

    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    private static final int SCALE = 1;

    private double scale;

    public HexIcon(ModelColor color, boolean actionable, Player player, int width, int height, double scale) {
        super(ResourceLoader.getBufferedImage(getImageName(color)));
        this.isActionable = actionable;
        this.color = color;
        this.scale = scale;
        this.player = player;
        resizeIcon((int) (width * scale), (int) (height * scale));
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
        } else { // Draw the original image
            g2d.drawImage(getImage(), (int) offsetX, (int) offsetY, null);
        }
    }

    private static String getImageName(ModelColor color) {
        switch (color) {
            case WHITE:
                return "hexaWhiteTextured";
            case NATURAL:
                return "hexaBrownTextured";
            case RED:
                return "hexaRedTextured";
            case GREEN:
                return "hexaGreenTextured";
            case BLUE:
                return "hexaBlueTextured";
            case YELLOW:
                return "hexaYellowTextured";
            case BLACK:
                return "hexaBlackTextured";
            case EMPTY:
                return "hexaWire";
            default:
                System.err.println("Color not found: " + color);
                return null;
        }
    }

    public HexIcon clone() {
        HexIcon clone = new HexIcon(color, isActionable, scale);
        clone.setPosition(position);
        clone.setPlayer(getPlayer());
        return clone;
    }

    public final void setScale(double scale) {
        this.scale = scale;
        resizeIcon((int) (WIDTH * scale), (int) (HEIGHT * scale));
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

    public BufferedImage getImage() {
        return super.getImage();
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

    public String toString() {
        return "HexIcon, isActionable: " + isActionable + "\nposition: " + getPosition() + "\ncolor: " + getColor();
    }
}
