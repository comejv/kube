package kube.services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import kube.model.action.Action;

public abstract class Network {

    private String ip;
    private int port;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public abstract boolean connect(String ip, int port);

    public abstract boolean disconnect();

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

    public boolean send(Action data) {
        try {
            if (getOut() != null) {
                getOut().writeObject(data);
                getOut().flush();
            } else {
                return false;
            }
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }

        return true;
    }

    public Action receive() throws IOException {
        try {
            Action o = (Action) getIn().readObject();
            return o;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
