package kube.services;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.Queue;

public class Client extends Network {

    private Socket socket;
    
    public Client(Queue<Action> networkToModel,String ip, int port){
        super(networkToModel);
        connect(ip, port);
    }


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean connect(String ip, int port) {
        try {
            setIp(ip);
            setPort(port);
            setSocket(new Socket(ip, port));
            setOut(new ObjectOutputStream(getSocket().getOutputStream()));
            setIn(new ObjectInputStream(getSocket().getInputStream()));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean disconnect() {
        try {
            getOut().close();
            getIn().close();
            getSocket().close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean send(Action data) {
        Config.debug(data);
        try {
            if (getOut() != null) {
                getOut().writeObject(data);
                Config.debug("Client send" + data);

            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

        return true;
    }

    public Action receive() {
        try {
            Action o = (Action) getIn().readObject();
            Config.debug("Client receive" + o);
            return o;
        } catch (Exception e) {
            return null;
        }
    }
}
