package kube.view.panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import kube.model.ModelColor;

public class GlassPanel extends JPanel {
    private BufferedImage image;
    private Point point;
    private ModelColor color;

    public GlassPanel() {
        setLayout(null);
        setOpaque(false);
        point = new Point(0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the superclass method to ensure proper rendering
        if (getImage() != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(getImage(), point.x, point.y, null);
            g2d.dispose();
        }
    }

    public void setPoint(Point p) {
        repaint(point.x, point.y, getImage().getWidth(), getImage().getHeight()); // Repaint old area
        point = p;
        repaint(point.x, point.y, getImage().getWidth(), getImage().getHeight()); // Repaint new area
    }

    public void setPoint(int x, int y) {
        setPoint(new Point(x, y));
    }

    public void setImage(BufferedImage img) {
        image = img;
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

    public BufferedImage getImage() {
        return image;
    }

    public void clear() {
        setImage(null);
        repaint();
    }
}
