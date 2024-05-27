package kube.controller.graphical;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Build;
import kube.model.action.Queue;
import kube.view.components.HexIcon;
import kube.view.panels.GlassPanel;

public class DnDController implements MouseListener, MouseMotionListener {
    private Queue<Action> toView;
    private Queue<Action> toModel;

    public DnDController(Queue<Action> eventsToView, Queue<Action> eventsToModel){
        this.toView = eventsToView;
        this.toModel = eventsToModel;
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
            Config.debug(icon);
            g.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Config.debug("Mouse release");
        GlassPanel g = (GlassPanel) e.getSource();
        // Get the object that was clicked in the content pane underneath the glass panel
        Container container = ((JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource())).getContentPane();
        Component component = SwingUtilities.getDeepestComponentAt(container, e.getX(), e.getY());
        if (g.getImage() != null && component instanceof HexIcon){
            HexIcon hex = (HexIcon) component;
            Config.debug("Mouse release on " + hex);
            if (hex.getPosition() == null){
                return;
            }
            toModel.add(new Action(ActionType.BUILD, new Build(g.getColor(), hex.getPosition())));
        }
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
