package kube.view.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import kube.configuration.Config;
import kube.model.ModelColor;
import kube.view.components.HexIcon;

public class DrawHexPanel extends JPanel {
    // TODO : refactor this class to make it more readable
    private HexIcon hex;
    private Point point;
    private ModelColor color;

    public DrawHexPanel(Point p, HexIcon hex) {
        this.hex = hex;
        this.point = p;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the superclass method to ensure proper rendering
        if (getImage() != null) {
            Point panelLocation = getLocationOnScreen();
            int x = point.x - panelLocation.x;
            int y = point.y - panelLocation.y;
            Config.debug("Paint !", x, y);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(getImage(), x,y,
                    null);
            g2d.dispose();
        }
    }

    public void setPoint(Point p) {
        repaint(point.x - (getImage().getWidth() / 2), point.y - (getImage().getHeight() / 2), getImage().getWidth(),
                getImage().getHeight()); // Repaint old area
        point = p;
        repaint(point.x - (getImage().getWidth() / 2), point.y - (getImage().getHeight() / 2), getImage().getWidth(),
                getImage().getHeight()); // Repaint new area
    }

    public void setPoint(int x, int y) {
        setPoint(new Point(x, y));
    }

    public void setHexIcon(HexIcon hex) {
        this.hex = hex;
        repaint(); // Redraw the panel
    }

    public void setColor(ModelColor c) {
        color = c;
    }

    public ModelColor getColor() {
        return color;
    }

    public Point getPoint() {
        return point;
    }

    public HexIcon getHexIcon() {
        return hex;
    }

    public BufferedImage getImage() {
        if (getHexIcon() != null) {
            return getHexIcon().getImage();
        }
        return null;
    }

    public void clear() {
        setHexIcon(null);
        repaint();
    }
}
