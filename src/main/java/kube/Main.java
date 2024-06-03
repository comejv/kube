package kube;

// Import kube classes
import kube.configuration.ResourceLoader;
import kube.controller.graphical.GUIControllers;
import kube.model.Game;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.GUI;

public class Main {

    /**
     * Main method of the program
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {

        new ResourceLoader();
        Kube kube = new Kube();
        
        Queue<Action> eventsToModel = new Queue<>();
        Queue<Action> eventsToView = new Queue<>();
        Queue<Action> eventsToNetwork = new Queue<>();

        Game model = new Game(Game.LOCAL, kube, eventsToModel, eventsToView, eventsToNetwork);

        Thread modelThread = new Thread(model);

        modelThread.start();

        GUIControllers controllers = new GUIControllers(eventsToView, eventsToModel);
        new GUI(kube, controllers, eventsToView, eventsToModel);
    }
}
