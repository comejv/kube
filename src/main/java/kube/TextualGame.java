package kube;

// Import kube classes
import kube.controller.textual.MenuListener;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.TextualMode;

// Import Java class
import java.util.Scanner;

public class TextualGame {

    /**
     * Main method of the program (textual mode)
     * 
     * @param args the arguments
     */
     public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        Kube kube = new Kube();

        Queue<Action> eventsToModel = new Queue<>();
        Queue<Action> eventsToView = new Queue<>();

        MenuListener menuListener = new MenuListener(kube, eventsToView, eventsToModel, scanner);
        TextualMode view = new TextualMode(kube, eventsToView);

        Thread viewThread = new Thread(view);
        Thread menuThread = new Thread(menuListener);
        
        viewThread.start();
        menuThread.start();
    }
}
