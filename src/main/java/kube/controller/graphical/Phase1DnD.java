package kube.controller.graphical;

// Import kube classes
import kube.model.action.*;
import kube.view.components.HexIcon;
import kube.view.panels.GlassPanel;

// Import java classes
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Phase1DnD extends MouseAdapter {

    /**********
     * ATTRIBUTES
     **********/

    private Queue<Action> toView, toModel;
    private Component oldComponent;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor for Phase1DnD
     * 
     * @param toView        queue of actions to view
     * @param eventsToModel queue of actions to model
     */
    public Phase1DnD(Queue<Action> toView, Queue<Action> eventsToModel) {
        this.toView = toView;
        this.toModel = eventsToModel;
    }

    /**********
     * SETTERS
     **********/

    public final void setOldComponent(Component component) {
        this.oldComponent = component;
    }

    /**********
     * GETTERS
     **********/

    public Queue<Action> getToView() {
        return toView;
    }

    public Queue<Action> getToModel() {
        return toModel;
    }

    public Component getOldComponent() {
        return oldComponent;
    }

    /**********
     * METHODS
     **********/

    /**
     * Perform action when mouse is pressed
     *
     * @param event mouse event
     * @return void
     */
    @Override
    public void mousePressed(MouseEvent event) {

        GlassPanel glassPanel;
        JFrame frame;
        Container container;
        Component component;
        HexIcon icon, newIcon;
        double scale;

        glassPanel = (GlassPanel) event.getSource();

        // Get the object that is clicked in the content pane underneath
        frame = (JFrame) SwingUtilities.getWindowAncestor((Component) event.getSource());
        container = frame.getContentPane();

        component = SwingUtilities.getDeepestComponentAt(container, event.getX(), event.getY());

        if (component instanceof HexIcon && ((HexIcon) component).isActionable()) {
            // Set cursor to drag
            glassPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            icon = (HexIcon) component;
            newIcon = icon.clone();
            scale = icon.getScale() < 2 ? 1.2 : 0.8;
            newIcon.setScale(scale * icon.getScale());
            glassPanel.setHexIcon(newIcon);
            glassPanel.setPoint(event.getPoint());
            glassPanel.setColor(newIcon.getColor());
            getToView().add(new Action(ActionType.DND_START, newIcon));
            glassPanel.repaint();
        }
    }

    /**
     * Perform action when mouse is released
     * 
     * @param event mouse event
     * @return void
     */
    public void mouseReleased(MouseEvent event) {

        GlassPanel g;
        Container container;
        Component component;
        HexIcon hex;
        Point to, from;

        g = (GlassPanel) event.getSource();
        
        // Get the object that was clicked in the content pane underneath
        container = ((JFrame) SwingUtilities.getWindowAncestor((Component) event.getSource())).getContentPane();
        component = SwingUtilities.getDeepestComponentAt(container, event.getX(), event.getY());

        if (g.getHexIcon() != null) {
            hex = null;
            to = null;
            if (component instanceof HexIcon) {
                hex = (HexIcon) component;
                to = hex.getPosition();
            }
            from = g.getHexIcon().getPosition();
            if (from == null && to != null) {
                getToModel().add(new Action(ActionType.BUILD, new Build(g.getColor(), hex.getColor(), to)));
            } else if (from != null && to != null) {
                getToModel().add(new Action(ActionType.SWAP, new Swap(from, to)));
            } else if (from != null && to == null) {
                getToModel().add(new Action(ActionType.REMOVE, new Remove(g.getColor(), from)));
            }
        }

        g.setCursor(Cursor.getDefaultCursor());
        g.clear();
    }

    /**
     * Perform action when mouse is moved
     * 
     * @param event mouse event
     * @return void
     */
    public void mouseMoved(MouseEvent event) {
        redispatchMouseEvent(event);
    }

    /**
     * Perform action when mouse wheel is moved
     * 
     * @param event mouse wheel event
     * @return void
     */
    public void mouseWheelMoved(MouseWheelEvent event) {
        redispatchMouseWheelEvent(event);
    }

    /**
     * Perform action when mouse is entered
     * 
     * @param event mouse event
     * @return void
     */
    public void mouseEntered(MouseEvent event) {
    }

    /**
     * Perform action when mouse is exited
     * 
     * @param event mouse event
     * @return void
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Perform action when mouse is clicked
     * 
     * @param event mouse event
     * @return void
     */
    public void mouseClicked(MouseEvent event) {
        redispatchMouseEvent(event);
    }

    /**
     * Perform action when mouse is dragged
     * 
     * @param event mouse event
     * @return void
     */
    public void mouseDragged(MouseEvent event) {

        GlassPanel g;

        g = (GlassPanel) event.getSource();
        if (g.getImage() == null) {
            redispatchMouseEvent(event);
            return;
        }

        g.setPoint(event.getPoint());
    }

    /**
     * Redispatch mouse event to the component underneath the glass pane
     * 
     * @param e mouse event
     * @return void
     */
    public void redispatchMouseEvent(MouseEvent e) {

        Point glassPanePoint;
        Container glassPane, contentPane, container;
        Point containerPoint;
        Component newComponent;
        MouseEvent event;

        glassPanePoint = e.getPoint();
        glassPane = (Container) e.getSource();
        contentPane = ((JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource())).getContentPane();
        container = contentPane;
        containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, contentPane);
        newComponent = SwingUtilities.getDeepestComponentAt(container, containerPoint.x, containerPoint.y);

        if (newComponent != null && newComponent != glassPane) {

            event = SwingUtilities.convertMouseEvent(glassPane, e, newComponent);
            if (newComponent != oldComponent) {

                if (newComponent instanceof HexIcon) {
                    getToView().add(new Action(ActionType.SET_HEX_HOVERED, newComponent));
                } else {
                    newComponent.dispatchEvent(
                            new MouseEvent(newComponent, MouseEvent.MOUSE_ENTERED, e.getWhen(), e.getModifiersEx(),
                                    e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
                }

                if (oldComponent != null) {

                    if (oldComponent instanceof HexIcon) {
                        getToView().add(new Action(ActionType.SET_HEX_DEFAULT, oldComponent));
                    } else {
                        getOldComponent().dispatchEvent(
                                new MouseEvent(oldComponent, MouseEvent.MOUSE_EXITED, e.getWhen(), e.getModifiersEx(),
                                        e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton()));
                    }
                }
            }

            newComponent.dispatchEvent(event);
            setOldComponent(newComponent);
        }
    }

    /**
     * Redispatch mouse wheel event to the component underneath the glass pane
     * 
     * @param e mouse wheel event
     */
    public void redispatchMouseWheelEvent(MouseWheelEvent e) {

        Point glassPanePoint;
        Container glassPane, contentPane, container;
        Point containerPoint;
        Component newComponent;
        MouseWheelEvent event;

        glassPanePoint = e.getPoint();
        glassPane = (Container) e.getSource();
        contentPane = ((JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource())).getContentPane();
        container = contentPane;
        containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, contentPane);
        newComponent = SwingUtilities.getDeepestComponentAt(container, containerPoint.x, containerPoint.y);

        if (newComponent != null && newComponent != glassPane) {

            event = new MouseWheelEvent(newComponent, e.getID(), e.getWhen(), e.getModifiersEx(),
                    containerPoint.x, containerPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getScrollType(),
                    e.getScrollAmount(), e.getWheelRotation());

            newComponent.dispatchEvent(event);
        }
    }
}