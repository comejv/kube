package kube.controller.graphical;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import kube.model.ModelColor;
import kube.model.Player;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.CreateMove;
import kube.model.action.Queue;
import kube.view.components.HexIcon;
import kube.view.panels.GlassPanel;

public class Phase2DnD extends Phase1DnD {
    // TODO : refactor this class to make it more readable
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
            if (hex.getColor() == ModelColor.EMPTY && playerFrom != playerTo) {
                toModel.add(new Action(ActionType.CREATE_MOVE,
                        new CreateMove(posFrom, playerFrom, posTo, playerTo, g.getColor())));
            }
        }
        toView.add(new Action(ActionType.DND_STOP));
        g.setCursor(Cursor.getDefaultCursor());
        g.clear();
    }
}
