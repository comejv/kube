package kube.services;

import kube.model.action.Action;
import kube.model.action.Queue;

public class TestConnectionServer {
    
    public static void main(String[] args) {
        Queue<Action> networkToModel = new Queue<>();
        Server network = new Server(networkToModel, 1234);
        System.out.println("In: "+ network.getIn());
        System.out.println("Out: "+ network.getOut());
        Thread networkThread = new Thread(network);
        networkThread.start();
        System.out.println("Server launched");
        while(true){
            System.out.println(networkToModel.remove());
        }

}
}
