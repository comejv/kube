package kube.services;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import kube.model.action.Action;
import kube.model.action.Queue;


public abstract class Network implements Runnable{
    
    private String ip;
    private int port;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Queue<Action> networkToModel;

    Network(Queue<Action> networkToModel) {
        this.networkToModel = networkToModel;
    }

    public abstract boolean connect(String ip, int port);
    public abstract boolean disconnect();
    public abstract boolean send(Action data);
    public abstract Action receive();
    
    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public boolean isServer() {
        return false;
    }
    
    @Override
    public void run() {
        while (true) {
            Action action = receive();
            if (action != null) {
                System.out.println("Received action: " + action);
                networkToModel.add(action);
            }
        }
    }
    
}
