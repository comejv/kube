package kube.controller.network;

import java.io.IOException;

import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.services.Network;

public class NetworkListener implements Runnable {

    Network network;
    Queue<Action> networkToModel;

    public NetworkListener(Network network, Queue<Action> networkToModel) {
        this.network = network;
        this.networkToModel = networkToModel;
    }

    @Override
    public void run() {
        while(true){
            try{
            Action action = network.receive();
            if (action != null) {
                networkToModel.add(action);
            }
        }
        catch(IOException e){
            networkToModel.add(new Action(ActionType.CONNECTION_CLOSED,e));
        }
        }
    }

}
