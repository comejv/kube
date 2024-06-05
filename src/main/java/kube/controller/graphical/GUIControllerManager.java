package kube.controller.graphical;

// Import kube classes
import kube.model.action.Action;
import kube.model.action.Queue;

public class GUIControllerManager {

    /**********
     * ATTRIBUTES
     **********/

    private MenuController menuController;
    private Phase1Controller phase1Controller;
    private Phase2Controller phase2Controller;
    private Phase1DnD hexPhase1DnD;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor for GUIControllers
     * 
     * @param toView  queue of actions to view
     * @param toModel queue of actions to model
     */
    public GUIControllerManager(Queue<Action> toView, Queue<Action> toModel, Queue<Action> toNetwork) {
        menuController = new MenuController(toView, toModel, toNetwork);
        phase1Controller = new Phase1Controller(toView, toModel);
        phase2Controller = new Phase2Controller(toView, toModel);
    }

    /**********
     * GETTERS
     **********/

    public MenuController getMenuController() {
        return menuController;
    }

    public Phase1Controller getPhase1Controller() {
        return phase1Controller;
    }

    public Phase2Controller getPhase2Controller() {
        return phase2Controller;
    }

    public Phase1DnD getHexPhase1DnD() {
        return hexPhase1DnD;
    }
}
