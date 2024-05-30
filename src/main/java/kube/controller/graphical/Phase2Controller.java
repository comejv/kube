package kube.controller.graphical;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.configuration.Config;

public class Phase2Controller implements ActionListener, MouseListener {
    private Queue<Action> toView;
    private Queue<Action> toModel;

    public Phase2Controller(Queue<Action> toView, Queue<Action> toModel) {
        this.toView = toView;
        this.toModel = toModel;
    }

    public void actionPerformed(ActionEvent evt) {
        switch (evt.getActionCommand()) {
            case "settings":
                toView.add(new Action(ActionType.SETTINGS));
                break;
            case "updateHist":
                toView.add(new Action(ActionType.MOVE));
                toModel.add(new Action(ActionType.MOVE_NUMBER, 0));
                toModel.add(new Action(ActionType.MOVE_NUMBER, 0));
                toModel.add(new Action(ActionType.MOVE_NUMBER, 0));
                break;
            case "undo":
                toModel.add(new Action(ActionType.UNDO));
                break;
            case "redo":
                toModel.add(new Action(ActionType.REDO));
                break;
            case "quit":
                // TODO : ask if need to save the game
                toModel.add(new Action(ActionType.RESET));
                toView.add(new Action(ActionType.RETURN_TO_MENU));
                break;

            default:
                break;
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
