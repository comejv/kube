package kube.controller.graphical;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.view.components.HexIcon;
import kube.view.components.Buttons.ButtonIcon;
import kube.configuration.Config;

public class Phase2Controller implements ActionListener, MouseListener, ComponentListener {
    // TODO : refactor this class to make it more readable
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
                toModel.add(new Action(ActionType.AI_PAUSE, true));
                toModel.add(new Action(ActionType.UNDO));
                break;
            case "redo":
                toModel.add(new Action(ActionType.AI_PAUSE, true));
                toModel.add(new Action(ActionType.REDO));
                break;
            case "quit":
                toView.add(new Action(ActionType.RETURN_TO_MENU));
                break;
            case "return":
                toView.add(new Action(ActionType.RETURN_TO_GAME));
                break;
            case "pauseAI":
                toModel.add(new Action(ActionType.AI_PAUSE, true));
                toView.add(new Action(ActionType.AI_PAUSE, true));
                break;
            case "stoppauseAI":
                toModel.add(new Action(ActionType.AI_PAUSE, false));
                toView.add(new Action(ActionType.AI_PAUSE, false));
                break;
            case "auto":
                toModel.add(new Action(ActionType.AI_MOVE));
                break;
            case "save":
                toModel.add(new Action(ActionType.AI_PAUSE, true));
                toView.add(new Action(ActionType.AI_PAUSE, true));
                toView.add(new Action(ActionType.SAVE, 2));
                break;
            case "load":
                toModel.add(new Action(ActionType.AI_PAUSE, true));
                toView.add(new Action(ActionType.LOAD_PANEL));
                break;
            default:
                break;
        }
    }

    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof JButton) {
            JButton b = (JButton) source;
            if (b.isEnabled()) {
                ActionEvent evt = new ActionEvent(source, ActionEvent.ACTION_PERFORMED,
                        ((JButton) source).getActionCommand());
                actionPerformed(evt);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        Object source = e.getSource();
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (source instanceof ButtonIcon) {
                toView.add(new Action(ActionType.SET_BUTTON_PRESSED, source));
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        Object source = e.getSource();
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (source instanceof ButtonIcon) {
                toView.add(new Action(ActionType.SET_BUTTON_RELEASED, source));
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof ButtonIcon) {
            toView.add(new Action(ActionType.SET_BUTTON_HOVERED, source));
        }
    }

    public void mouseExited(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof ButtonIcon) {
            toView.add(new Action(ActionType.SET_BUTTON_DEFAULT, source));
        }
    }

    public void componentResized(ComponentEvent e) {
        toView.add(new Action(ActionType.UPDATE_HEX_SIZE));
    }

    public void componentMoved(ComponentEvent e) {

    }

    public void componentShown(ComponentEvent e) {

    }

    public void componentHidden(ComponentEvent e) {

    }
}
