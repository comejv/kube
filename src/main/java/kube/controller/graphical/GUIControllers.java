package kube.controller.graphical;

import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.Queue;

public class GUIControllers {
    MenuController menuController;
    Phase1Controller phase1Controller;
    Phase2Controller phase2Controller;
    Phase1DnD hexPhase1DnD;

    public GUIControllers(Queue<Action> toView, Queue<Action> toModel) {
        menuController = new MenuController(toView, toModel);
        phase1Controller = new Phase1Controller(toView, toModel);
        Config.debug("GUIControllers: Phase1Controller created : ", phase1Controller);
        phase2Controller = new Phase2Controller(toView, toModel);
    }

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
