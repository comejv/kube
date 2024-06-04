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
import kube.view.components.HexIcon;
import kube.view.components.Buttons.ButtonIcon;
import kube.view.components.Buttons.SelectPlayerButton;
import kube.view.panels.LoadingSavePanel;
import kube.view.panels.MenuPanel;
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

    public Queue<Action> getEventsToView() {
        return eventsToView;
    }

    public Queue<Action> getEventsToModel() {
        return eventsToModel;
    }

    public GUI getGUI() {
        return gui;
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

            action = getEventsToView().remove();
            switch (action.getType()) {
                // GLOBAL
                case SET_BUTTON_DEFAULT:
                    ((ButtonIcon) action.getData()).setDefault();
                    getGUI().getMainFrame().getGlassPane()
                            .setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    break;
                case SET_BUTTON_HOVERED:
                    ((ButtonIcon) action.getData()).setHovered(true);
                    getGUI().getMainFrame().getGlassPane()
                            .setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
                        getGUI().getMainFrame().getGlassPane()
                                .setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                    break;
                case SET_HEX_HOVERED:
                    h = (HexIcon) action.getData();
                    if (h.isActionable()) {
                        h.setHovered(true);
                        getGUI().getMainFrame().getGlassPane()
                                .setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }
                    break;
                case SET_HEX_PRESSED:
                    ((HexIcon) action.getData()).setPressed(true);
                    break;
                case SET_HEX_RELEASED:
                    ((HexIcon) action.getData()).setPressed(false);
                    break;
                case RETURN_TO_MENU:
                    if (getGUI().askForConfirmation("Abandonner la partie",
                            "Êtes-vous sûr de vouloir quitter la partie ?")) {
                        getGUI().setGlassPaneController(null);
                        getGUI().removeAllFromOverlay();
                        getGUI().showPanel(getGUI().MENU);
                        eventsToModel.add(new Action(ActionType.RESET));
                    }
                    break;
                case RETURN_TO_GAME:
                    getGUI().removeAllFromOverlay();
                    break;
                case QUIT:
                    System.exit(0);
                    break;
                case PRINT_FORBIDDEN_ACTION:
                    Config.debug("Forbidden action : " + action.getData());
                    message = (String) action.getData() == null ? "You can't do that now."
                            : (String) action.getData();
                    getGUI().showError("Forbidden action", message);
                    break;
                case PRINT_INVALID_SAVE:
                    getGUI().showError("Invalid save file",
                            "Please select another save file, this one is invalid, too old or corrupted.");
                    break;
                case PRINT_NOT_YOUR_TURN:
                    Config.debug("Not your turn");
                    getGUI().showWarning("Not your turn", "It's not your turn yet.");
                    break;
                case PRINT_WIN_MESSAGE:
                    Config.debug("Win message");
                    while (getGUI().getOverlay().getComponentCount() > 0) {
                        System.out.print(""); // IDK why but doesn't work whithout, nice java
                    }
                    getGUI().winMessage(action);
                    break;
                case PRINT_INVALID_ADDRESS:
                    getGUI().showError("Connexion impossible",
                            "Impossible de se connecter à l'hôte, veuillez vérifier l'adresse et le port.");
                    break;
                // MENU
                case START_LOCAL:
                    MenuPanel menu = (MenuPanel) getGUI().getPanel(GUI.MENU);
                    p1 = (SelectPlayerButton) menu.player1;
                    p2 = (SelectPlayerButton) menu.player2;
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
                            iaJ1 = null;
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
                            iaJ2 = null;
                            break;
                    }
                    getEventsToModel().add(new Action(ActionType.START, new Start(iaJ1, iaJ2)));
                    getGUI().setGlassPanelVisible(true);
                    break;
                case START_ONLINE:
                    Config.debug("Starting online game");
                    break;
                case HOST:
                    MenuPanel mp = (MenuPanel) getGUI().getPanel(GUI.MENU);
                    mp.showHostMenu();
                    break;
                case RULES:
                    menuController = getGUI().getControllerManager().getMenuController();
                    getGUI().addToOverlay(new OverlayPanel(gui, menuController, action.getType()));
                    getGUI().setGlassPanelVisible(true);
                    break;
                case END_RULE:
                    overlay = (OverlayPanel) getGUI().getOverlay().getComponent(0);
                    rulesPanel = (RulesPanel) overlay.getComponent(0);
                    for (Timer timer : rulesPanel.getAnimatedRuleTimer()) {
                        timer.stop();
                    }
                    getGUI().removeAllFromOverlay();
                    getGUI().setGlassPanelVisible(false);
                    break;
                case NEXT_RULE:
                    overlay = (OverlayPanel) getGUI().getOverlay().getComponent(0);
                    rulesPanel = (RulesPanel) overlay.getComponent(0);
                    rulesPanel.nextRule();
                    break;
                case PREVIOUS_RULE:
                    overlay = (OverlayPanel) getGUI().getOverlay().getComponent(0);
                    rulesPanel = (RulesPanel) overlay.getComponent(0);
                    rulesPanel.previousRule();
                    break;
                case END_OVERLAY_MENU:
                    getGUI().removeAllFromOverlay();
                    getGUI().setGlassPanelVisible(false);
                    break;
                case SETTINGS:
                    menuController = getGUI().getControllerManager().getMenuController();
                    settings = new OverlayPanel(gui, menuController, action.getType());
                    getGUI().addToOverlay(settings);
                    setSavedGlassPaneController(getGUI().getCurrentListener());
                    getGUI().setGlassPaneController(getGUI().getDefaultGlassPaneController());
                    getGUI().setGlassPanelVisible(true);
                    break;
                case CONFIRMED_SETTINGS:
                    getGUI().removeAllFromOverlay();
                    getGUI().setGlassPaneController(getSavedGlassPaneController());
                    break;
                case REFRESH_CONNEXION:
                    getGUI().enableHostStartButton((boolean) action.getData());
                    break;
                // FIRST PHASE
                case VALIDATE:
                    getGUI().updatePanel();
                    break;
                case DND_START:
                case DND_STOP:
                    getGUI().updateDnd(action);
                    break;
                case BUILD:
                    Sounds.playSound("build");
                    getGUI().updateFirstPanel(action);
                    break;
                case REMOVE:
                    Sounds.playSound("remove");
                    getGUI().updateFirstPanel(action);
                    break;
                case SWAP:
                    Sounds.playSound("swap");
                    getGUI().updateFirstPanel(action);
                    break;
                case AI_MOVE:
                    getGUI().updateFirstPanel(action);
                    break;
                case MOVE:
                    Sounds.playSound("build");
                    getGUI().updateSecondPanel(action);
                    break;
                case UNDO:
                    Sounds.playSound("remove");
                    getGUI().updateSecondPanel(action);
                    break;
                case REDO:
                    Sounds.playSound("build");
                    getGUI().updateSecondPanel(action);
                    break;
                case AI_PAUSE:
                    getGUI().updateSecondPanel(action);
                    break;
                case LOAD_PANEL:
                    menuController = getGUI().getControllerManager().getMenuController();
                    loadMenu = new OverlayPanel(gui, menuController, action.getType());
                    getGUI().addToOverlay(loadMenu);
                    setSavedGlassPaneController(getGUI().getCurrentListener());
                    getGUI().setGlassPaneController(getGUI().getDefaultGlassPaneController());
                    getGUI().setGlassPanelVisible(true);
                    break;
                case LOAD_FILE_SELECTED:
                    overlay = (OverlayPanel) getGUI().getOverlay().getComponent(0);
                    loadingSavePanel = (LoadingSavePanel) overlay.getComponent(0);
                    loadingSavePanel.enableLoadButton();
                    loadingSavePanel.enableDeleteButton();
                    break;
                case UPDATE_HEX_SIZE:
                    getGUI().updateHexSize();
                    break;
                case PRINT_STATE:
                    break;
                case SAVE:
                    getGUI().save(action);
                    break;
                default:
                    Config.debug("Unrecognized action : " + action);
                    break;
            }
        }
    }
}
