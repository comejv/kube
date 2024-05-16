package kube.services;

import java.io.BufferedReader;
import java.io.PrintWriter;


public abstract class Network implements Runnable {
    
    private String ip;
    private int port;
    private PrintWriter out;
    private BufferedReader in;


    public abstract boolean connect(String ip, int port);
    public abstract boolean disconnect();
    public abstract boolean send(Object data);
    public abstract Object receive();
    
    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public boolean isServer() {
        return false;
    }
}
