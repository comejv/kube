package kube.controller.graphical;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Build;
import kube.model.action.Queue;
import kube.model.action.Remove;
import kube.model.action.Swap;
import kube.view.components.HexIcon;
import kube.view.panels.GlassPanel;

public class Phase2DnD  extends Phase1DnD {
    private Queue<Action> toView;
    private Queue<Action> toModel;
    private Component component;

    public Phase2DnD(Queue<Action> eventsToView, Queue<Action> eventsToModel) {
        super(eventsToView, eventsToModel);
    }

    @Override
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
            HexIcon newIcon = icon.clone();
            double scale = icon.getScale() < 2 ? 1.2 : 0.8;
            newIcon.setScale(scale * icon.getScale());
            g.setHexIcon(newIcon);
            g.setPoint(e.getPoint());
            g.setColor(newIcon.getColor());
            g.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GlassPanel g = (GlassPanel) e.getSource();
        // Get the object that was clicked in the content pane underneath the glass
        // panel
        Container container = ((JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource())).getContentPane();
        Component component = SwingUtilities.getDeepestComponentAt(container, e.getX(), e.getY());
        if (g.getHexIcon() != null) {
            HexIcon hex = null;
            Point to = null;
            if (component instanceof HexIcon) {
                hex = (HexIcon) component;
                to = hex.getPosition();
            }
            Point from = g.getHexIcon().getPosition();
            if (from == null && to != null) {
                toModel.add(new Action(ActionType.BUILD, new Build(g.getColor(), hex.getColor(), to)));
            } else if (from != null && to != null) {
                toModel.add(new Action(ActionType.SWAP, new Swap(from, to)));
            } else if (from != null && to == null) {
                toModel.add(new Action(ActionType.REMOVE, new Remove(g.getColor(), from)));
            }
        }
        g.setCursor(Cursor.getDefaultCursor());
        g.clear();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        redispatchMouseEvent(e);
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
            } else {
                MouseEvent newEvent = SwingUtilities.convertMouseEvent(glassPane, e, newComponent);
                newComponent.dispatchEvent(newEvent);
            }
        }
        component = newComponent;
    }
}
