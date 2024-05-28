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
import kube.view.components.HexIcon;
import kube.view.components.Buttons.ButtonIcon;

public class Phase1Controller implements ActionListener, MouseListener {
    private Queue<Action> toView;
    private Queue<Action> toModel;

    public Phase1Controller(Queue<Action> toView, Queue<Action> toModel) {
        this.toView = toView;
        this.toModel = toModel;
    }

    public DnDController getDragNDropController() {
        return new DnDController(toView);
    }

    public void actionPerformed(ActionEvent evt) {
        Config.debug("Action ", evt.getActionCommand(), " received.");
        switch (evt.getActionCommand()) {
            case "phase2":
                toView.add(new Action(ActionType.VALIDATE));
                toModel.add(new Action(ActionType.VALIDATE));
                break;
            case "menu": // change to opts.
                toView.add(new Action(ActionType.SETTINGS));

            default:
                break;
        }
    }

    public void mouseClicked(MouseEvent e) {
        Config.debug("Mouse clicked in " + e.getSource());
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
        Config.debug("Mouse entered in " + source);
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
