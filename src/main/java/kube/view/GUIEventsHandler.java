package kube.view;

import kube.model.Kube;
import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.components.Buttons.ButtonIcon;

public class GUIEventsHandler implements Runnable {

    Kube kube;
    Queue<Action> events;
    GUI gui;

    public GUIEventsHandler(GUI gui, Queue<Action> events) {
        this.events = events;
        this.gui = gui;
    }

    @Override
    public void run() {
        while (true) {
            Action action = events.remove();
            switch (action.getType()) {
                case SET_BUTTON_DEFAULT:
                    ((ButtonIcon) action.getData()).setDefault();
                    break;
                case SET_BUTTON_HOVERED:
                    ((ButtonIcon) action.getData()).setHovered(true);
                    break;
                case SET_BUTTON_PRESSED:
                    ((ButtonIcon) action.getData()).setPressed(true);
                    break;
                case SET_BUTTON_RELEASED:
                    ((ButtonIcon) action.getData()).setPressed(false);
                    break;
                case PLAY_LOCAL:
                    gui.showPanel(GUI.PHASE1);
                    gui.loadPanel(GUI.PHASE2);
                    break;
                case VALIDATE:
                    gui.showPanel(GUI.PHASE2);
                    break;
                default:
                    Config.debug("Unrecognized action : " + action);
                    break;
            }
        }
    }
}
