package kube.view;

import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.components.Buttons.ButtonIcon;

public class GUIEventsHandler implements Runnable {

    Kube kube;
    Queue<Action> events;

    public GUIEventsHandler(GUI gui, Queue<Action> events) {
        this.events = events;
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
                default:
                    break;
            }
        }
    }
}
