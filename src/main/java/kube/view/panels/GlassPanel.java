package kube.view.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPanel;

public class GlassPanel extends JPanel {
    private Component object;
    private Point point;

    public GlassPanel() {
        setLayout(null);
        setOpaque(false);
        point = new Point(0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the superclass method to ensure proper rendering
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.drawRect(point.x, point.y, 10, 10);

        // if (object != null && point != null) {
        //     // Set the bounds of the object to position it correctly
        //     object.setBounds(point.x, point.y, object.getWidth(), object.getHeight());
        //     System.out.println("painting");

        //     // Paint the object onto the glass panel
        //     // object.paint(g);
        // }
    }

    public void setPoint(Point p) {
        repaint(point.x, point.y, 10, 10); // Repaint old area
        point = p;
        repaint(point.x, point.y, 10, 10); // Repaint new area
    }

    public void setPoint(int x, int y) {
        setPoint(new Point(x, y));
    }

    public void setObject(Component obj) {
        object = obj;
        repaint(); // Redraw the panel
    }

    public Point getPoint() {
        return point;
    }

    public Component getObject() {
        return object;
    }
}
