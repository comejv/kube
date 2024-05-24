package kube.view.panels;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;

public class GlassPanel extends JComponent {
    private Component object;
    private Point point;

    public GlassPanel(Component obj, Container contentPane) {
        object = obj;
        point = new Point(0, 0);
        // TODO add mouseListener from the controller
        /*
         * it must take in arg:
         * - a Component (to draw it)
         * - a Container (where to draw it)
         * - a glassPane (who calls it)
         */
    }

    protected void paintComponent(Graphics g) {
    }

    public void setPoint(Point ptn) {
        point = ptn;
    }

    public void setPoint(int x, int y) {
        point = new Point(x, y);
    }

    public void setObject(Component obj) {
        object = obj;
    }

    public Point getPoint() {
        return point;
    }

    public Component getObject() {
        return object;
    }
}
