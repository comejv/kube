package kube.controller.graphical;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.view.components.HexIcon;

public class HexDnDController extends MouseAdapter {
    private Queue<Action> toView;

    public HexDnDController(Queue<Action> toView) {
        this.toView = toView;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Config.debug("Mouse released");
        // Get dropoff location
        Component source = (Component) e.getSource();
        Container grid = source.getParent().getParent();
        Config.debug(grid.getComponentCount());
        if (grid != null && grid.getLayout() instanceof GridLayout) {
            Config.debug("mouse x " + e.getX());
            Config.debug("mouse y " + e.getY());
            int gridX = (e.getX()) / (grid.getWidth() / grid.getComponentCount());
            int gridY = (e.getY()) / (grid.getHeight() / grid.getComponentCount());
            Config.debug("Dropped in grid cell (" + gridX + ", " + gridY + ")");
        }
        toView.add(new Action(ActionType.REMOVE_GLASS));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Object source = e.getSource();

        HexIcon h = (HexIcon) source;
        h.setOffset(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Not detected because click in other pannel
        Config.debug("Mouse draggued in overlay");
        Object source = e.getSource();

        HexIcon h = (HexIcon) source;
        h.setOffset(e.getX(), e.getY());
    }
}
