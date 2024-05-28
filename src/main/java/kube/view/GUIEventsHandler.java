package kube.view;

import kube.model.Kube;
import kube.configuration.Config;
import kube.model.action.*;
import kube.view.components.Buttons.ButtonIcon;

public class GUIEventsHandler implements Runnable {

    Kube kube;
    Queue<Action> eventsToView;
    Queue<Action> eventsToModel;
    GUI gui;

    public GUIEventsHandler(GUI gui, Queue<Action> eventsToView, Queue<Action> eventsToModel) {
        this.eventsToView = eventsToView;
        this.eventsToModel = eventsToModel;
        this.gui = gui;
    }

    @Override
    public void run() {
        while (true) {
            Action action = eventsToView.remove();
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
                    eventsToModel.add(new Action(ActionType.START, new Start()));
                    gui.showPanel(GUI.PHASE1);
                    gui.setGlassPanelVisible(true);
                    gui.loadPanel(GUI.PHASE2);
                    break;
                case VALIDATE:
                    gui.setGlassPanelVisible(true);
                    gui.showPanel(GUI.PHASE2);
                    break;
                case BUILD:
                    gui.updateFirstPanel();
                    break;
                default:
                    Config.debug("Unrecognized action : " + action);
                    break;
            }
        }
    }
}
