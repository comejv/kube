package kube.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import kube.model.action.Action;

public class Client extends Network{

    private Socket socket;


    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    

    public boolean connect(String ip, int port) {
        try{
            setIp(ip);
            setPort(port);
            setSocket(new Socket(ip, port));
            setOut(new PrintWriter(getSocket().getOutputStream(), true));
            setIn(new BufferedReader(new InputStreamReader(getSocket().getInputStream())));
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    public boolean disconnect() {
        try{
            getOut().close();
            getIn().close();
            getSocket().close();
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    public boolean send(Object data) {
        try{
            getOut().println(data);
        }
        catch(Exception e){
            return false;
        }
        return true;
    }

    public Object receive() {
        try{
            return getIn().readLine();
        }
        catch(Exception e){
            return null;
        }
    }

    @Override
    public void run() {
         
    }
}
