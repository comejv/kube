package kube.controller;

import kube.model.action.Action;
import kube.model.action.Queue;
import kube.services.Network;

import kube.configuration.*;

public class NetworkSender implements Runnable{

    Network network;
    Queue<Action> modelToNetwork;

    public NetworkSender(Network network, Queue<Action> modelToNetwork){
        this.network = network;
        this.modelToNetwork = modelToNetwork;
    }

    @Override
    public void run() {
        while(true){
            Action action = modelToNetwork.remove();
            Config.debug("Sending action: " + action);
            if(action != null){
                network.send(action);
            }
        }
    }
    
}
