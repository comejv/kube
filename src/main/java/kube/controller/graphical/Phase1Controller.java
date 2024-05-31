package kube.controller.graphical;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.view.components.Buttons.ButtonIcon;

public class Phase1Controller implements ActionListener, MouseListener {
    // TODO : refactor this class to make it more readable
    private Queue<Action> toView;
    private Queue<Action> toModel;

    public Phase1Controller(Queue<Action> toView, Queue<Action> toModel) {
        this.toView = toView;
        this.toModel = toModel;
    }

    public void actionPerformed(ActionEvent evt) {
        Config.debug("Action performed in FirstPhaseController");
        switch (evt.getActionCommand()) {
            case "validate":
                toModel.add(new Action(ActionType.VALIDATE));
                break;
            case "settings": // change to opts.
                toView.add(new Action(ActionType.SETTINGS));
                break;
            case "quit":
                toView.add(new Action(ActionType.PRINT_ASK_SAVE_FILE_NAME));
                toModel.add(new Action(ActionType.RESET));
                toView.add(new Action(ActionType.RETURN_TO_MENU));
                break;
            case "AI":
                toModel.add(new Action(ActionType.AI_MOVE));
                break;
            default:
                break;
        }
    }

    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof JButton) {
            ActionEvent evt = new ActionEvent(source, ActionEvent.ACTION_PERFORMED, ((JButton) source).getActionCommand());
            actionPerformed(evt);
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
        Config.debug("Mouse entered" );
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
}
