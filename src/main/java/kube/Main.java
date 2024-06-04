package kube;

import kube.configuration.Config;
// Import kube classes
import kube.configuration.ResourceLoader;
import kube.controller.graphical.GUIControllerManager;
import kube.model.Game;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.GUI;
import kube.view.Music;

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

        GUIControllerManager controllers = new GUIControllerManager(eventsToView, eventsToModel, eventsToNetwork);
        new GUI(kube, controllers, eventsToView, eventsToModel);

        Music bgMusic = new Music("Ether-Vox");
        Config.setMusic(bgMusic);
        if (!Config.isMute()) {
            bgMusic.play();
        }
    }
}
