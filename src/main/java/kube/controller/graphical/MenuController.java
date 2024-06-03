package kube.controller.graphical;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.SwingUtilities;

import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.view.components.Buttons.ButtonIcon;

public class MenuController implements ActionListener, MouseListener {
    // TODO : refactor this class to make it more readable
    Queue<Action> toView;
    Queue<Action> toModel;
    String selectedFile;

    public MenuController(Queue<Action> toView, Queue<Action> toModel) {
        this.toView = toView;
        this.toModel = toModel;
        selectedFile = null;
    }

    public void actionPerformed(ActionEvent evt) {
        switch (evt.getActionCommand()) {
            case "local":
                toView.add(new Action(ActionType.PLAY_LOCAL));
                break;
            case "online":
                toView.add(new Action(ActionType.PLAY_ONLINE));
                break;
            case "play":
                toView.add(new Action(ActionType.START));
                break;
            case "rules":
                toView.add(new Action(ActionType.RULES));
                break;
            case "nextRule":
                toView.add(new Action(ActionType.NEXT_RULE));
                break;
            case "previousRule":
                toView.add(new Action(ActionType.PREVIOUS_RULE));
                break;
            case "quit":
                toView.add(new Action(ActionType.QUIT));
                break;
            case "volume":
                toView.add(new Action(ActionType.VOLUME));
                break;
            case "settings":
                toView.add(new Action(ActionType.SETTINGS));
                break;
            case "confirmed_settings":
                toView.add(new Action(ActionType.CONFIRMED_SETTINGS));
                break;
            case "endRule":
            case "return":
                toView.add(new Action(ActionType.END_OVERLAY_MENU));
                break;
            case "load":
                toModel.add(new Action(ActionType.RESET));
                toModel.add(new Action(ActionType.LOAD, selectedFile));
                toView.add(new Action(ActionType.LOAD));
                toView.add(new Action(ActionType.END_OVERLAY_MENU));
                break;
            default:
                break;
        }
    }

    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof ButtonIcon) {
            ButtonIcon b = (ButtonIcon) source;
            switch (b.getName()) {
                case "settings":
                    Config.debug("Settings clicked");
                    toView.add(new Action(ActionType.SETTINGS));
                    break;
                case "volume":
                    Config.debug("Volume clicked");
                    toView.add(new Action(ActionType.VOLUME));
                    break;
                default:
                    Config.error("Unrecognised buttonIcon action.");
                    break;
            }
        }
        else if (source instanceof JList) {
            JList<?> list = (JList<?>) source;
            switch (list.getName()) {
                case "fileList":
                    toView.add(new Action(ActionType.LOAD_FILE_SELECTED));
                    selectedFile = (String) list.getSelectedValue();
                    break;
                default:
                    Config.error("Unrecognised JList action.");
                    break;
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
}
