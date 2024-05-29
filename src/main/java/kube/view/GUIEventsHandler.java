package kube.view;

import java.awt.Container;

import kube.configuration.Config;
import kube.controller.graphical.DnDController;
import kube.controller.graphical.MenuController;
import kube.model.Kube;
import kube.model.action.*;
import kube.view.components.Buttons.ButtonIcon;
import kube.view.components.Buttons.MenuButton;
import kube.view.panels.MenuPanel;
import kube.view.panels.OverlayPanel;
import kube.view.panels.RulesPanel;
import kube.view.panels.SettingsPanel;

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
                    gui.showPanel(GUI.PHASE1);
                    gui.setGlassPaneController(new DnDController(eventsToView, eventsToModel));
                    gui.setGlassPanelVisible(true);
                    gui.loadPanel(GUI.PHASE2);
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
                case END_RULE:
                    gui.removeAllFromOverlay();
                    gui.setGlassPanelVisible(false);
                    break;
                case SETTINGS:
                    gui.addToOverlay(new OverlayPanel(gui, gui.getControllers().getMenuController()
                                                      , action.getType()));
                    gui.setGlassPanelVisible(true);
                    break;
                // FIRST PHASE
                case VALIDATE:
                    gui.setGlassPanelVisible(true);
                    gui.showPanel(GUI.PHASE2);
                    break;
                default:
                    Config.debug("Unrecognized action : " + action);
                    break;
            }
        }
    }
}
