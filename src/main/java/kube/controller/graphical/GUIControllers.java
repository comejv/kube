package kube.controller.graphical;

import kube.model.action.Action;
import kube.model.action.Queue;

public class GUIControllers {
    MenuController menuController;
    Phase1Controller phase1Controller;
    Phase2Controller phase2Controller;
    DnDController hexDnDController;

    public GUIControllers(Queue<Action> toView, Queue<Action> toModel) {
        menuController = new MenuController(toView, toModel);
        phase1Controller = new Phase1Controller(toView, toModel);
        phase2Controller = new Phase2Controller(toView, toModel);
        hexDnDController = new DnDController(toView);
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

    public DnDController getHexDnDController() {
        return hexDnDController;
    }
}
