package kube.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import java.awt.Point;
import kube.model.move.*;

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
        Scanner scanner = new Scanner(System.in);
        String s;
        while(true){
            s = scanner.nextLine();
            System.err.println("Sending: " + s);
            Move p = new MoveMW(new Point(0,0));
            send(p);
            Object data = receive();
            if(data != null){
                System.out.println("Received: " + data);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting client");
        Thread t = new Thread(new Server(1234));
        System.out.println("Starting server on port 1234");
        t.start();
        System.out.println("Server started");
        Client client = new Client();
        Thread t2 = new Thread(client);
        t2.start();
       }
}
