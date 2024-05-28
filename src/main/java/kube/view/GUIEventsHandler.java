package kube.view;

import kube.configuration.Config;
import kube.controller.graphical.MenuController;
import kube.model.Kube;
import kube.model.action.*;
import kube.view.components.Buttons.ButtonIcon;
import kube.view.panels.RulesPanel;

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
                case LOCAL:
                    break;
                case RULES:
                    //toModel is null because we don't interract with the model in the rules
                    gui.addToOverlay(new RulesPanel(gui, new MenuController(eventsToView, null)));
                    break;
                case NEXT_RULE:
                    RulesPanel rulePanel = (RulesPanel) gui.getOverlay().getComponent(0);
                    rulePanel.nextRule();
                    break;
                case END_RULE:
                    gui.removeAllFromOverlay();
                    break;
                case QUIT:
                    System.exit(0);
                    gui.showPanel(GUI.PHASE1);
                    break;
                case SETTINGS:
                    gui.showPanel(GUI.MENU);
                    break;
                default:
                    Config.debug("Unrecognized action : " + action);
                    break;
            }
        }
    }
}
