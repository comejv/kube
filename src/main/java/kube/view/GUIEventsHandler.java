package kube.view;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;

import kube.configuration.Config;
import kube.model.Kube;
import kube.model.action.*;
import kube.model.ai.MiniMaxAI;
import kube.model.ai.betterConstructV2;
import kube.model.ai.moveSetHeuristique;
import kube.view.components.HexIcon;
import kube.view.components.Buttons.ButtonIcon;
import kube.view.components.Buttons.SelectPlayerButton;
import kube.view.panels.LoadingSavePanel;
import kube.view.panels.OverlayPanel;
import kube.view.panels.RulesPanel;
import kube.view.panels.SettingsPanel;

public class GUIEventsHandler implements Runnable {
    // TODO : refactor this class to make it more readable

    private Kube kube;
    private Queue<Action> eventsToView;
    private Queue<Action> eventsToModel;
    private MouseAdapter savedGlassPaneController;
    private GUI gui;

    public GUIEventsHandler(GUI gui, Queue<Action> eventsToView, Queue<Action> eventsToModel) {
        this.eventsToView = eventsToView;
        this.eventsToModel = eventsToModel;
        this.gui = gui;
    }

    @Override
    public void run() {
        while (true) {
            Action action = eventsToView.remove();
            Config.debug("View receive : ", action);
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
                    HexIcon h = (HexIcon) action.getData();
                    if (h.isActionable()) {
                        h.setDefault();
                        gui.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                    break;
                case SET_HEX_HOVERED:
                    h = (HexIcon) action.getData();
                    if (h.isActionable()) {
                        h.setHovered(true);
                        gui.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }
                    break;
                case SET_HEX_PRESSED:
                    ((HexIcon) action.getData()).setPressed(true);
                    break;
                case SET_HEX_RELEASED:
                    ((HexIcon) action.getData()).setPressed(false);
                    break;
                case RETURN_TO_MENU:
                    if (gui.askForConfirmation("Retourner au menu",
                            "Êtes vous sûr de vouloir abandonner la partie ?")) {
                        gui.setGlassPaneController(null);
                        gui.removeAllFromOverlay();
                        gui.showPanel(GUI.MENU);
                        eventsToModel.add(new Action(ActionType.RESET));
                    }
                    break;
                case RETURN_TO_GAME:
                    gui.setGlassPaneController(null);
                    gui.removeAllFromOverlay();
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
                    while (gui.getOverlay().getComponentCount() > 0) {
                        System.out.print(""); // IDK why but doesn't work whithout, nice java
                    }
                    gui.winMessage(action);
                    break;
                // MENU
                case START:
                    SelectPlayerButton p1 = (SelectPlayerButton) gui.mP.player1;
                    SelectPlayerButton p2 = (SelectPlayerButton) gui.mP.player2;
                    MiniMaxAI iaJ1, iaJ2;
                    if (p1.buttonValue == 0) {
                        iaJ1 = null;
                    } else {
                        iaJ1 = new betterConstructV2();
                    }
                    if (p2.buttonValue == 0) {
                        iaJ2 = null;
                    } else {
                        iaJ2 = new betterConstructV2();
                    }
                    eventsToModel.add(new Action(ActionType.START,
                            new Start(iaJ1, iaJ2)));
                    gui.setGlassPanelVisible(true);
                    break;
                case PLAY_LOCAL:
                    // TODO : maybe tell model about it ?
                    break;
                case PLAY_ONLINE:
                    // TODO : maybe tell model about it ?
                    break;
                case RULES:
                    gui.addToOverlay(new OverlayPanel(gui, gui.getControllers().getMenuController(), action.getType()));
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
                case END_OVERLAY_MENU:
                    gui.removeAllFromOverlay();
                    gui.setGlassPanelVisible(false);
                    break;
                case SETTINGS:
                    OverlayPanel settings = new OverlayPanel(gui, gui.getControllers().getMenuController(),
                            action.getType());
                    gui.addToOverlay(settings);
                    setSavedGlassPaneController(gui.getCurrentListener());
                    gui.setGlassPaneController(gui.getDefaultGlassPaneController());
                    gui.setGlassPanelVisible(true);
                    break;
                case CONFIRMED_SETTINGS:
                    gui.removeAllFromOverlay();
                    gui.setGlassPaneController(getSavedGlassPaneController());
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
                case AI_PAUSE:
                    gui.updateSecondPanel(action);
                    break;
                case LOAD_PANEL:
                    OverlayPanel loadMenu = new OverlayPanel(gui, gui.getControllers().getMenuController(),
                            action.getType());
                    gui.addToOverlay(loadMenu);
                    setSavedGlassPaneController(gui.getCurrentListener());
                    gui.setGlassPaneController(gui.getDefaultGlassPaneController());
                    gui.setGlassPanelVisible(true);
                    break;
                case LOAD_FILE_SELECTED:
                    OverlayPanel op = (OverlayPanel) gui.getOverlay().getComponent(0);
                    LoadingSavePanel lp = (LoadingSavePanel) op.getComponent(0);
                    lp.enableLoadButton();
                    lp.enableDeleteButton();
                    break;
                case UPDATE_HEX_SIZE:
                    gui.updateHexSize();
                    break;
                case PRINT_STATE:
                    break;
                case SAVE:
                    gui.save(action);
                    break;
                default:
                    Config.debug("Unrecognized action : " + action);
                    break;
            }
        }
    }

    public void setSavedGlassPaneController(MouseAdapter ma) {
        savedGlassPaneController = ma;
    }

    public MouseAdapter getSavedGlassPaneController() {
        return savedGlassPaneController;
    }
}
