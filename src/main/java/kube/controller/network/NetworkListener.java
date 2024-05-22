package kube.controller.network;

import kube.model.action.Action;
import kube.model.action.Queue;
import kube.services.Network;

public class NetworkListener implements Runnable{


    Network network;
    Queue<Action> networkToModel;

    public NetworkListener(Network network, Queue<Action> networkToModel){
        this.network = network;
        this.networkToModel = networkToModel;
    }

    @Override
    public void run() {
        while(true){
            Action action = network.receive();
            if (action != null) {
                networkToModel.add(action);
            }
        }
    }
    
}
