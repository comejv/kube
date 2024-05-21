package kube.services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import kube.model.action.Action;

public class Server extends Network {

    private ServerSocket serverSocket;
    private Socket clientSocket;

    public Server(int port) {    
        init(port);
    }

    public final void init(int port) {
        try {
            setServerSocket(new ServerSocket(port));
            setClientSocket(getServerSocket().accept());
            setOut(new ObjectOutputStream(getClientSocket().getOutputStream()));
            setIn(new ObjectInputStream(getClientSocket().getInputStream()));
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port +"\n" + e.getMessage());
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


    @Override
    public boolean connect(String ip, int port) {
        return clientSocket.isConnected();
    }

    @Override
    public boolean disconnect() {
        try {
            getOut().close();
            getIn().close();
            getClientSocket().close();
            getServerSocket().close();
        } catch (IOException e) {
            System.err.println("Could not close the connection\n" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean send(Action data) {
        try {
            if (getOut() != null) {
                getOut().writeObject(data);
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public Action receive() {
        try {
            Action o = (Action) getIn().readObject();
            return o;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public boolean isServer() {
        return true;
    }



}
