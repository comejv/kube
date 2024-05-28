package kube.controller.network;

import kube.model.action.Action;
import kube.model.action.Queue;
import kube.services.Network;


public class NetworkSender implements Runnable{

    Network network;
    Queue<Action> modelToNetwork;
    int player;

    public NetworkSender(Network network, Queue<Action> modelToNetwork, int player){
        this.network = network;
        this.modelToNetwork = modelToNetwork;
        this.player = player;
    }

    @Override
    public void run() {
        while(true){
            Action action = modelToNetwork.remove();
            if(action != null){
                action.setPlayer(player);
                network.send(action);
            }
        }
    }
    
}
