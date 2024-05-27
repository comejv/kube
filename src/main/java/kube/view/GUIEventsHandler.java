package kube.view;

import java.awt.CardLayout;

import javax.swing.JPanel;

import kube.configuration.Config;
import kube.controller.graphical.MenuController;
import kube.model.Kube;
import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.components.Buttons.ButtonIcon;
import kube.view.panels.RulesPanel;

public class GUIEventsHandler implements Runnable {

    private Kube kube;
    private Queue<Action> events;
    private GUI gui;

    public GUIEventsHandler(GUI gui, Queue<Action> events) {
        this.events = events;
        this.gui = gui;
    }

    @Override
    public void run() {
        while (true) {
            Action action = events.remove();
            switch (action.getType()) {
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
                case PLAY_LOCAL:
                    System.out.println("play local");
                    gui.showPanel(GUI.PHASE1);
                    gui.loadPanel(GUI.PHASE2);
                    break;
                case VALIDATE:
                    gui.showPanel(GUI.PHASE2);
                    break;
                case LOCAL:
                    break;
                case RULES:
                    //toModel is null because we don't interract with the model in the rules
                    gui.addToOverlay(new RulesPanel(gui, new MenuController(events, null)));
                    break;
                case NEXT_RULE:
                    RulesPanel rulePanel = (RulesPanel) gui.getOverlay().getComponent(0);
                    rulePanel.nextRule();
                    break;
                case END_RULE:
                    gui.removeAllFromOverlay();
                    break;
                case QUIT:
                    System.exit(0);
                    break;
                default:
                    Config.debug("Unrecognized action : " + action);
                    break;
            }
        }
    }
}
