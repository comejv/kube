package kube.view;

// Import kube classes
import kube.controller.graphical.GUIControllerManager;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.panels.FirstPhasePanel;
import kube.view.panels.SecondPhasePanel;

// Import java class
import javax.swing.JPanel;

public class PanelLoader implements Runnable {

    /**********
     * ATTRIBUTES
     **********/

    private GUI gui;
    private String panelName;
    private GUIControllerManager controllerManager;
    private Kube k3;
    private Queue<Action> eventsToView, eventsToModel;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor for PanelLoader class
     * 
     * @param gui               the GUI object
     * @param panelName         the name of the panel
     * @param k3                the Kube object
     * @param controllerManager the GUIControllers object
     * @param eventsToView      the queue of actions to view
     * @param eventsToModel     the queue of actions to model
     */
    public PanelLoader(GUI gui, String panelName, Kube k3, GUIControllerManager controllerManager, Queue<Action> eventsToView,
            Queue<Action> eventsToModel) {
        this.gui = gui;
        this.panelName = panelName;
        this.controllerManager = controllerManager;
        this.k3 = k3;
        this.eventsToView = eventsToView;
        this.eventsToModel = eventsToModel;
    }

    /**********
     * GETTERS
     **********/

    public final GUI getGui() {
        return gui;
    }

    public final String getPanelName() {
        return panelName;
    }

    public final GUIControllerManager getControllerManager() {
        return controllerManager;
    }

    public final Kube getK3() {
        return k3;
    }

    public final Queue<Action> getEventsToView() {
        return eventsToView;
    }

    public final Queue<Action> getEventsToModel() {
        return eventsToModel;
    }

    /**********
     * METHODS
     **********/

    @Override
    public void run() {

        JPanel panel;

        switch (panelName) {
            case GUI.PHASE1:
                panel = new FirstPhasePanel(getGui(), getK3(), getControllerManager().getPhase1Controller(),
                        getEventsToView(), getEventsToModel());
                break;
            case GUI.PHASE2:
                panel = new SecondPhasePanel(getGui(), getK3(), getControllerManager().getPhase2Controller());
                break;
            default:
                panel = null;
                break;
        }

        getGui().setPanel(getPanelName(), panel);

        synchronized (getGui()) {
            getGui().notify();
        }
    }
}