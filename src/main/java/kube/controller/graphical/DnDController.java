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
import javax.swing.SwingUtilities;

import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.components.HexIcon;
import kube.view.panels.GlassPanel;

public class DnDController implements MouseListener, MouseMotionListener {
    private Queue<Action> toView;

    public DnDController(Queue<Action> toView) {
        this.toView = toView;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        GlassPanel g = (GlassPanel) e.getSource();
        // Get the object that was clicked in the content pane underneath the glass panel
        Container container = ((JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource())).getContentPane();
        Component component = SwingUtilities.getDeepestComponentAt(container, e.getX(), e.getY());
        
        if (component instanceof HexIcon && ((HexIcon) component).isActionable()) {
            Config.debug("HexIcon clicked");
            HexIcon icon = (HexIcon) component;
            g.setImage(icon.getImage());
            g.setPoint(e.getPoint());
            g.setColor(icon.getColor());
            g.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GlassPanel g = ((GlassPanel) e.getSource());
        g.clear();
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
        if (g.getImage() == null) {
            return;
        }
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
