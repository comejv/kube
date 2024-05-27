package kube.controller.graphical;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.components.HexIcon;
import kube.view.panels.GlassPanel;

public class DnDController implements MouseListener, MouseMotionListener {
    private Queue<Action> toView;
    private Component component;

    public DnDController(Queue<Action> toView) {
        this.toView = toView;
    }

    public void mousePressed(MouseEvent e) {
        GlassPanel g = (GlassPanel) e.getSource();
        // Get the object that was clicked in the content pane underneath the glass
        // panel
        Container container = ((JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource())).getContentPane();
        Component component = SwingUtilities.getDeepestComponentAt(container, e.getX(), e.getY());

        if (component instanceof HexIcon && ((HexIcon) component).isActionable()) {
            // Set cursor to drag
            g.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            HexIcon icon = (HexIcon) component;
            g.setImage(icon.getImage());
            g.setPoint(e.getPoint());
            g.setColor(icon.getColor());
            g.repaint();
        }
    }

    public void mouseReleased(MouseEvent e) {
        GlassPanel g = ((GlassPanel) e.getSource());
        g.setCursor(Cursor.getDefaultCursor());
        g.clear();
    }

    public void mouseMoved(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    public void mouseDragged(MouseEvent e) {
        GlassPanel g = (GlassPanel) e.getSource();
        if (g.getImage() == null) {
            return;
        }
        g.setPoint(e.getPoint());
    }

    private void redispatchMouseEvent(MouseEvent e) {
        JPanel glassPane = (JPanel) e.getSource();
        Container container = ((JFrame) SwingUtilities.getWindowAncestor(glassPane)).getContentPane();
        Component newComponent = SwingUtilities.getDeepestComponentAt(container, e.getX(), e.getY());

        if (newComponent != null) {
            if (component != newComponent) {
                if (newComponent instanceof HexIcon) {
                    HexIcon icon = (HexIcon) newComponent;
                    if (icon.isActionable()) {
                        icon.setHovered(true);
                    }
                } else {
                    if (component != null) {
                        component.dispatchEvent(new MouseEvent(component, MouseEvent.MOUSE_EXITED, e.getWhen(),
                                e.getModifiersEx(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger()));
                    }
                }
                if (component instanceof HexIcon) {
                    ((HexIcon) component).setHovered(false);
                } else {
                    newComponent.dispatchEvent(new MouseEvent(newComponent, MouseEvent.MOUSE_ENTERED, e.getWhen(),
                            e.getModifiersEx(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger()));
                }
            }
            if (e.getID() == MouseEvent.MOUSE_CLICKED && newComponent instanceof JButton) {
                JButton b = (JButton) newComponent;
                b.doClick();
            }
            // newComponent.dispatchEvent(newEvent);
        }
        component = newComponent;
    }
}