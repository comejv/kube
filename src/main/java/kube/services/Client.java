package kube.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import kube.configuration.Config;

public class Client extends Network {

    private Socket socket;
    
    public Client(String ip, int port){
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

    public boolean send(Object data) {
        try {
            Config.debug("Client send" +  data);
            if (getOut() != null) {
                getOut().writeObject(data);
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Object receive() {
        try {
            Object o = getIn().readObject();
            Config.debug("Client receive");
            return o;
        } catch (Exception e) {
            return null;
        }
    }

}
