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
import kube.model.Player;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Build;
import kube.model.action.CreateMove;
import kube.model.action.Queue;
import kube.model.action.Remove;
import kube.model.action.Swap;
import kube.view.components.HexIcon;
import kube.view.panels.GlassPanel;

public class Phase2DnD extends Phase1DnD {
    private Queue<Action> toView;
    private Queue<Action> toModel;
    private Component component;

    public Phase2DnD(Queue<Action> eventsToView, Queue<Action> eventsToModel) {
        super(eventsToView, eventsToModel);
        toView = eventsToView;
        toModel = eventsToModel;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GlassPanel g = (GlassPanel) e.getSource();
        // Get the object that was clicked in the content pane underneath the glass
        // panel
        Container container = ((JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource())).getContentPane();
        Component component = SwingUtilities.getDeepestComponentAt(container, e.getX(), e.getY());
        if (g.getHexIcon() != null && component instanceof HexIcon) {
            HexIcon hex = (HexIcon) component;
            Point posFrom = g.getHexIcon().getPosition();
            Player playerFrom = g.getHexIcon().getPlayer();
            Point posTo = hex.getPosition();
            Player playerTo = hex.getPlayer();
            Config.debug(posFrom, playerFrom, posTo, playerTo, g.getColor());
            toModel.add(new Action(ActionType.CREATE_MOVE, new CreateMove(posFrom, playerFrom, posTo, playerTo, g.getColor())));
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
