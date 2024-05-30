package kube.view;

import kube.configuration.Config;
import kube.controller.graphical.Phase1DnD;
import kube.controller.graphical.MenuController;
import kube.controller.graphical.Phase1DnD;
import kube.model.Kube;
import kube.model.action.*;
import kube.model.ai.moveSetHeuristique;
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
            Config.debug("View is waiting ");
            Action action = eventsToView.remove();
            Config.debug("View receive ", action);
            switch (action.getType()) {
                // GLOBAL
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
                case RETURN_TO_MENU:
                    gui.showPanel(GUI.MENU);
                    break;
                case QUIT:
                    System.exit(0);
                    break;
                case SETTINGS:
                    // TODO : show settings overlay
                    break;
                case PRINT_FORBIDDEN_ACTION:
                    Config.debug("Forbidden action : " + action.getData());
                    String message = (String) action.getData() == null ? "You can't do that now."
                            : (String) action.getData();
                    gui.showError("Forbidden action", message);
                    break;
                case PRINT_NOT_YOUR_TURN:
                    Config.debug("Not your turn");
                    gui.showWarning("Not your turn", "It's not your turn yet.");
                    break;
                case PRINT_WIN_MESSAGE:
                    Config.debug("Win message");
                    gui.showInfo("You won !", "Congratulations, you won the game !");
                    break;

                // MENU
                case START:
                    eventsToModel.add(new Action(ActionType.START, new Start()));
                    gui.setGlassPaneController(new Phase1DnD(eventsToView, eventsToModel));
                    gui.setGlassPanelVisible(true);
                    break;
                case RULES:
                    // toModel is null because we don't interract with the model in the rules
                    // TODO : maybe reuse old controller ?
                    gui.addToOverlay(new RulesPanel(gui, new MenuController(eventsToView, null)));
                    break;
                case NEXT_RULE:
                    RulesPanel rulePanel = (RulesPanel) gui.getOverlay().getComponent(0);
                    rulePanel.nextRule();
                    break;
                case END_RULE:
                    gui.removeAllFromOverlay();
                    break;
                case PLAY_LOCAL:
                    // TODO : maybe tell model about it ?
                    break;
                case PLAY_ONLINE:
                    // TODO : maybe tell model about it ?
                    break;

                // FIRST PHASE
                case VALIDATE:
                    gui.setGlassPanelVisible(true);
                    gui.updatePanel();
                    break;
                case BUILD:
                case REMOVE:
                case SWAP:
                case AI_MOVE:
                    gui.updateFirstPanel(action);
                    break;
                case MOVE:
                case UNDO:
                case REDO:
                    gui.updateSecondPanel(action);
                break;
                default:
                    Config.debug("Unrecognized action : " + action);
                    break;
            }
        }
    }
}
