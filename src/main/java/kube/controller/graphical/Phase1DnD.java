package kube.controller.graphical;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import kube.model.action.*;
import kube.view.components.HexIcon;
import kube.view.panels.GlassPanel;

public class Phase1DnD implements MouseListener, MouseMotionListener, MouseWheelListener {
    // TODO : refactor this class to make it more readable
    private Queue<Action> toView;
    private Queue<Action> toModel;
    private Component component;
    private Phase1Controller controller;

    public Phase1DnD(Queue<Action> eventsToView, Queue<Action> eventsToModel) {
        this.toView = eventsToView;
        this.toModel = eventsToModel;
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
            toView.add(new Action(ActionType.DND_START, newIcon));
            g.repaint();
        }
    }

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

    public void mouseMoved(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        redispatchMouseWheelEvent(e);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    public void mouseDragged(MouseEvent e) {
        GlassPanel g = (GlassPanel) e.getSource();
        if (g.getImage() == null) {
            redispatchMouseEvent(e);
            return;
        }
        g.setPoint(e.getPoint());
    }

    public void redispatchMouseEvent(MouseEvent e) {
        Point glassPanePoint = e.getPoint();
        Container glassPane = (Container) e.getSource();
        Container contentPane = ((JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource())).getContentPane();
        Container container = contentPane;
        Point containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, contentPane);
        Component newComponent = SwingUtilities.getDeepestComponentAt(container, containerPoint.x, containerPoint.y);

        if (newComponent != null && newComponent != glassPane) {
            MouseEvent event = SwingUtilities.convertMouseEvent(glassPane, e, newComponent);
            if (newComponent != component) { 
                if (newComponent instanceof HexIcon) {
                    ((HexIcon) newComponent).setHovered(true);
                } else {
                    newComponent.dispatchEvent(
                            new MouseEvent(newComponent, MouseEvent.MOUSE_ENTERED, e.getWhen(), e.getModifiersEx(),
                                    e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
                }
                if (component != null) {
                    if (component instanceof HexIcon) {
                        ((HexIcon) component).setHovered(false);
                    } else {
                        component.dispatchEvent(
                                new MouseEvent(component, MouseEvent.MOUSE_EXITED, e.getWhen(), e.getModifiersEx(),
                                        e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
                    }
                }
            }
            newComponent.dispatchEvent(event);
            component = newComponent;
        }
    }

    // Does not work
    public void redispatchMouseWheelEvent(MouseWheelEvent e) {
        Point glassPanePoint = e.getPoint();
        Container glassPane = (Container) e.getSource();
        Container contentPane = ((JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource())).getContentPane();
        Container container = contentPane;
        Point containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, contentPane);
        Component newComponent = SwingUtilities.getDeepestComponentAt(container, containerPoint.x, containerPoint.y);

        if (newComponent != null && newComponent != glassPane) {
            MouseWheelEvent event = new MouseWheelEvent(newComponent, e.getID(), e.getWhen(), e.getModifiersEx(),
                    containerPoint.x, containerPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getScrollType(),
                    e.getScrollAmount(), e.getWheelRotation());
            newComponent.dispatchEvent(event);
        }
    }
}
