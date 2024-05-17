package kube.services;

import kube.model.action.Action;
import kube.model.action.Queue;

public class TestConnectionClient {
    public static void main(String[] args) {
        Queue<Action> networkToModel = new Queue<>();
        Client network = new Client(networkToModel,"localhost", 1234);
        System.out.println("In: " + network.getIn());
        System.out.println("Out: " + network.getOut());
        //Thread networkThread = new Thread(network);
        // networkThread.start();
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            network.send(new Action(0));
            System.out.println("prout");
        }
    
}
}
