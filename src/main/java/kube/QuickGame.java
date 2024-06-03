package kube;

// Import kube classes
import kube.controller.textual.CommandListener;
import kube.model.Game;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.TextualMode;

// Import java class
import java.util.Scanner;

public class QuickGame {

    /**
     * Main method of the program (quick game mode)
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Kube kube = new Kube();

        Queue<Action> eventsToModel = new Queue<>();
        Queue<Action> eventsToView = new Queue<>();
        Queue<Action> eventsToNetwork = new Queue<>();

        Game model = new Game(Game.LOCAL, kube, eventsToModel, eventsToView, eventsToNetwork);
        TextualMode view = new TextualMode(kube, eventsToView);
        CommandListener controller = new CommandListener(eventsToModel, eventsToView, scanner);

        Thread modelThread = new Thread(model);
        Thread controllerThread = new Thread(controller);
        Thread viewThread = new Thread(view);

        modelThread.start();
        viewThread.start();
        controllerThread.start();
    }

}
