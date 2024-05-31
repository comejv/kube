package kube.view;

import kube.configuration.Configuration;
import kube.controller.graphical.Phase1DnD;
import kube.controller.graphical.MenuController;
import kube.model.Kube;
import kube.model.action.*;
import kube.view.components.HexIcon;
import kube.view.components.Buttons.ButtonIcon;
import kube.view.panels.RulesPanel;

public class GUIEventsHandler implements Runnable {
    // TODO : refactor this class to make it more readable

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
            Configuration.debug("View receive ", action.getType());
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
                case SET_HEX_DEFAULT:
                    ((HexIcon) action.getData()).setDefault();
                    break;
                case SET_HEX_HOVERED:
                    ((HexIcon) action.getData()).setHovered(true);
                    break;
                case SET_HEX_PRESSED:
                    ((HexIcon) action.getData()).setPressed(true);
                    break;
                case SET_HEX_RELEASED:
                    ((HexIcon) action.getData()).setPressed(false);
                    break;
                case RETURN_TO_MENU:
                    gui.setGlassPanelVisible(false);
                    gui.showPanel(GUI.MENU);
                    break;
                case QUIT:
                    System.exit(0);
                    break;
                case SETTINGS:
                    // TODO : show settings overlay
                    break;
                case PRINT_FORBIDDEN_ACTION:
                    Configuration.debug("Forbidden action : " + action.getData());
                    String message = (String) action.getData() == null ? "You can't do that now."
                            : (String) action.getData();
                    gui.showError("Forbidden action", message);
                    break;
                case PRINT_NOT_YOUR_TURN:
                    Configuration.debug("Not your turn");
                    gui.showWarning("Not your turn", "It's not your turn yet.");
                    break;
                case PRINT_WIN_MESSAGE:
                    Configuration.debug("Win message");
                    gui.showInfo("You won !", "Congratulations, you won the game !");
                    break;

                // MENU
                case START:
                    eventsToModel.add(new Action(ActionType.START, new Start()));
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
                    Configuration.debug("Unrecognized action : " + action);
                    break;
            }
        }
    }
}
