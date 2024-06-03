package kube.controller.network;

// Import kube classes
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.services.Network;

public class NetworkSender implements Runnable {

    /**********
     * ATTRIBUTES
     **********/

    Network network;
    Queue<Action> modelToNetwork;
    int player;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the class
     * 
     * @param network        the network object
     * @param modelToNetwork the queue of actions to send to the network
     * @param player         the player number
     */
    public NetworkSender(Network network, Queue<Action> modelToNetwork, int player) {
        this.network = network;
        this.modelToNetwork = modelToNetwork;
        this.player = player;
    }

    /**********
     * METHOD
     **********/

    @Override
    public void run() {
        while (true) {
            Action action = modelToNetwork.remove();
            if (action != null) {
                action.setPlayer(player);
                network.send(action);
            }
        }
    }

}
