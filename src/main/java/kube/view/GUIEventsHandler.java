package kube.view;

// Import kube classes
import kube.configuration.Config;
import kube.controller.graphical.MenuController;
import kube.model.action.*;
import kube.model.ai.EasyAI;
import kube.model.ai.ExpertAI;
import kube.model.ai.HardAI;
import kube.model.ai.MediumAI;
import kube.model.ai.MiniMaxAI;
import kube.model.ai.betterConstructV2;
import kube.view.components.HexIcon;
import kube.view.components.Buttons.ButtonIcon;
import kube.view.components.Buttons.SelectPlayerButton;
import kube.view.panels.LoadingSavePanel;
import kube.view.panels.OverlayPanel;
import kube.view.panels.RulesPanel;

// Import java classes
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import javax.swing.Timer;

public class GUIEventsHandler implements Runnable {

    /**********
     * ATTRIBUTES
     **********/

    private Queue<Action> eventsToView, eventsToModel;
    private MouseAdapter savedGlassPaneController;
    private GUI gui;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor for the GUIEventsHandler class
     * 
     * @param gui           the GUI object
     * @param eventsToView  the queue of actions to view
     * @param eventsToModel the queue of actions to model
     */
    public GUIEventsHandler(GUI gui, Queue<Action> eventsToView, Queue<Action> eventsToModel) {
        this.eventsToView = eventsToView;
        this.eventsToModel = eventsToModel;
        this.gui = gui;
    }

    /**********
     * SETTER
     **********/

    public void setSavedGlassPaneController(MouseAdapter ma) {
        savedGlassPaneController = ma;
    }

    /**********
     * GETTER
     **********/

    public MouseAdapter getSavedGlassPaneController() {
        return savedGlassPaneController;
    }

    /**********
     * RUN METHOD
     **********/

    @Override
    public void run() {

        Action action;
        String message;
        SelectPlayerButton p1, p2;
        MiniMaxAI iaJ1, iaJ2;
        OverlayPanel overlay, loadMenu, settings;
        RulesPanel rulesPanel;
        LoadingSavePanel loadingSavePanel;
        MenuController menuController;
        HexIcon h;

        while (true) {

            action = eventsToView.remove();
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
                    h = (HexIcon) action.getData();
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
                    gui.setGlassPaneController(null);
                    gui.removeAllFromOverlay();
                    gui.showPanel(GUI.MENU);
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
                    message = (String) action.getData() == null ? "You can't do that now."
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
                    p1 = (SelectPlayerButton) gui.getMenuPanel().player1;
                    p2 = (SelectPlayerButton) gui.getMenuPanel().player2;
                    iaJ1 = null;
                    iaJ2 = null;
                    
                    switch (p1.buttonValue) {
                        case 0:
                            iaJ1 = null;
                            break;
                        case 1:
                            iaJ1 = new EasyAI();
                            break;
                        case 2:
                            iaJ1 = new MediumAI();
                            break;
                        case 3:
                            iaJ1 = new HardAI();
                            break;
                        case 4:
                            iaJ1 = new ExpertAI();
                            break;
                        default:
                            break;
                    }

                    switch (p2.buttonValue) {
                        case 0:
                            iaJ2 = null;
                            break;
                        case 1:
                            iaJ2 = new EasyAI();
                            break;
                        case 2:
                            iaJ2 = new MediumAI();
                            break;
                        case 3:
                            iaJ2 = new HardAI();
                            break;
                        case 4:
                            iaJ2 = new ExpertAI();
                            break;
                        default:
                            break;
                    }

                    eventsToModel.add(new Action(ActionType.START, new Start(iaJ1, iaJ2)));

                    gui.setGlassPanelVisible(true);
                    break;
                case PLAY_LOCAL:
                    // TODO : maybe tell model about it ?
                    break;
                case PLAY_ONLINE:
                    // TODO : maybe tell model about it ?
                    break;
                case RULES:
                    menuController = gui.getControllerManager().getMenuController();
                    gui.addToOverlay(new OverlayPanel(gui, menuController, action.getType()));
                    gui.setGlassPanelVisible(true);
                    break;
                case END_RULE:
                    overlay = (OverlayPanel) gui.getOverlay().getComponent(0);
                    rulesPanel = (RulesPanel) overlay.getComponent(0);
                    for (Timer timer : rulesPanel.getAnimatedRuleTimer()) {
                        timer.stop();
                    }
                    gui.removeAllFromOverlay();
                    gui.setGlassPanelVisible(false);
                    break;
                case NEXT_RULE:
                    overlay = (OverlayPanel) gui.getOverlay().getComponent(0);
                    rulesPanel = (RulesPanel) overlay.getComponent(0);
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
                    menuController = gui.getControllerManager().getMenuController();
                    settings = new OverlayPanel(gui, menuController, action.getType());
                    gui.addToOverlay(settings);
                    setSavedGlassPaneController(gui.getCurrentListener());
                    gui.setGlassPaneController(gui.getDefaultGlassPaneController());
                    gui.setGlassPanelVisible(true);
                    break;
                case CONFIRMED_SETTINGS:
                    gui.removeAllFromOverlay();
                    gui.setGlassPaneController(getSavedGlassPaneController());
                    break;
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
                    menuController = gui.getControllerManager().getMenuController();
                    loadMenu = new OverlayPanel(gui, menuController, action.getType());
                    gui.addToOverlay(loadMenu);
                    setSavedGlassPaneController(gui.getCurrentListener());
                    gui.setGlassPaneController(gui.getDefaultGlassPaneController());
                    gui.setGlassPanelVisible(true);
                    break;
                case LOAD_FILE_SELECTED:
                    overlay = (OverlayPanel) gui.getOverlay().getComponent(0);
                    loadingSavePanel = (LoadingSavePanel) overlay.getComponent(0);
                    loadingSavePanel.enableLoadButton();
                    loadingSavePanel.enableDeleteButton();
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
}