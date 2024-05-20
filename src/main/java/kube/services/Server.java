package kube.services;

import kube.model.action.Action;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Network {

    private ServerSocket serverSocket;
    private Socket clientSocket;

    public Server(int port) {
        
        init(port);
    }

    public void init(int port) {
        try {
            setServerSocket(new ServerSocket(port));
            setClientSocket(getServerSocket().accept());
            setOut(new ObjectOutputStream(getClientSocket().getOutputStream()));
            setIn(new ObjectInputStream(getClientSocket().getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public boolean connect(String ip, int port) {
        return false;
    }

    public boolean disconnect() {
        try {
            getOut().close();
            getIn().close();
            getClientSocket().close();
            getServerSocket().close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean send(Action data) {
        try {
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

    public Action receive() {
        try {
            Action o = (Action) getIn().readObject();
            return o;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isServer() {
        return true;
    }



}
