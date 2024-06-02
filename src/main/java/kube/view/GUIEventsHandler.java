package kube.view;

import kube.configuration.Config;
import kube.model.Kube;
import kube.model.action.*;
import kube.model.ai.moveSetHeuristique;
import kube.view.components.HexIcon;
import kube.view.components.Buttons.ButtonIcon;
import kube.view.panels.OverlayPanel;
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
            Config.debug("View receive ", action.getType());
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
                    gui.setGlassPaneController(null);
                    gui.showPanel(GUI.MENU);
                    break;
                case QUIT:
                    System.exit(0);
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
                    gui.winMessage(action);
                    break;
                // MENU
                case START:
                    eventsToModel.add(new Action(ActionType.START, new Start()));
                    gui.setGlassPanelVisible(true);
                    break;
                case PLAY_LOCAL:
                    // TODO : maybe tell model about it ?
                    break;
                case PLAY_ONLINE:
                    // TODO : maybe tell model about it ?
                    break;
                case RULES:
                    gui.addToOverlay(new OverlayPanel(gui, gui.getControllers().getMenuController()
                                                      , action.getType()));
                    gui.setGlassPanelVisible(true);
                    break;
                case NEXT_RULE:
                    OverlayPanel overlay = (OverlayPanel) gui.getOverlay().getComponent(0);
                    RulesPanel rulesPanel = (RulesPanel) overlay.getComponent(0);
                    rulesPanel.nextRule();
                    break;
                case PREVIOUS_RULE:
                    overlay = (OverlayPanel) gui.getOverlay().getComponent(0);
                    rulesPanel = (RulesPanel) overlay.getComponent(0);
                    rulesPanel.previousRule();
                    break;
                case END_RULE:
                    gui.removeAllFromOverlay();
                    gui.setGlassPanelVisible(false);
                    break;
                case SETTINGS:
                    gui.addToOverlay(new OverlayPanel(gui, gui.getControllers().getMenuController()
                                                      , action.getType()));
                    gui.setGlassPanelVisible(true);
                    break;
                case CONFIRMED_SETTINGS:
                    gui.removeAllFromOverlay();
                    gui.setGlassPanelVisible(false);
                    break;
                // FIRST PHASE
                case VALIDATE:
                    gui.updatePanel();
                    break;
                case DND_START:
                case DND_STOP:
                    gui.updateDnd(action);
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
                case PRINT_STATE: // Ignore this action
                    break;
                default:
                    Config.debug("Unrecognized action : " + action);
                    break;
            }
        }
    }
}
