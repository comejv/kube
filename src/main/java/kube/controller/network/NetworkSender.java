package kube.controller.network;

import java.io.IOException;

import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.services.Network;


public class NetworkSender implements Runnable{

    Network network;
    Queue<Action> modelToNetwork, networkToModel;
    int player;

    public NetworkSender(Network network, Queue<Action> modelToNetwork,Queue<Action> networkToModel, int player){
        this.network = network;
        this.modelToNetwork = modelToNetwork;
        this.networkToModel = networkToModel;
        this.player = player;
    }

    @Override
    public void run() {
        while(true){
            try {
            Action action = modelToNetwork.remove();
            if(action != null){
                action.setPlayer(player);
                network.send(action);
            }
        }
        catch(IOException e){
            networkToModel.add(new Action(ActionType.CONNECTION_CLOSED,e));
        }
        }
    }
    
}
