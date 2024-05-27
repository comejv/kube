package kube.controller.graphical;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.view.components.HexIcon;
import kube.view.components.Icon;
import kube.view.panels.GlassPanel;

public class DnDController implements MouseListener, MouseMotionListener {
    private Queue<Action> toView;

    public DnDController(Queue<Action> toView) {
        this.toView = toView;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        GlassPanel g = (GlassPanel) e.getSource();
        Icon i = new Icon(ResourceLoader.getBufferedImage("gear"));
        i.resizeIcon(30, 30);
        g.setObject(i);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GlassPanel g = ((GlassPanel) e.getSource());
        // g.removeObject();
        // Get dropoff location
        // Component source = (Component) e.getSource();
        // Container grid = source.getParent().getParent();
        // Config.debug(grid.getComponentCount());
        // if (grid != null && grid.getLayout() instanceof GridLayout) {
        // Config.debug("mouse x " + e.getX());
        // Config.debug("mouse y " + e.getY());
        // int gridX = (e.getX()) / (grid.getWidth() / grid.getComponentCount());
        // int gridY = (e.getY()) / (grid.getHeight() / grid.getComponentCount());
        // Config.debug("Dropped in grid cell (" + gridX + ", " + gridY + ")");
        // }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        GlassPanel g = (GlassPanel) e.getSource();
        g.setPoint(e.getPoint());
    }

    private void redispatchMouseEvent(MouseEvent e) {
        // JPanel glassPane = (JPanel) e.getSource();
        // Container container = ((JFrame) SwingUtilities.getWindowAncestor(
        // (Component) e.getSource())).getContentPane();
        // Component component = SwingUtilities.getDeepestComponentAt(container,
        // e.getX(), e.getY());
        //
        // if (component != null && component != glassPane) {
        // MouseEvent newEvent = new MouseEvent(container, e.getID(), e.getWhen(),
        // e.getModifiersEx(), e.getX(), e.getY(),
        // e.getClickCount(), e.isPopupTrigger(), e.getButton());
        // component.dispatchEvent(newEvent);
        // }
    }
}
