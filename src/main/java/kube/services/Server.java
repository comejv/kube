package kube.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Network{

    private ServerSocket serverSocket;
    private Socket clientSocket;

    public Server(int port){
        try{
            setServerSocket(new ServerSocket(port));
            setClientSocket(getServerSocket().accept());
            setOut(new PrintWriter(getClientSocket().getOutputStream(), true));
            setIn(new BufferedReader(new InputStreamReader(getClientSocket().getInputStream())));
        }
        catch(Exception e){
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
        try{
            getOut().close();
            getIn().close();
            getClientSocket().close();
            getServerSocket().close();
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
        Object data=null;
        do{
            try{
                data =  receive();
                System.out.println("Received: " + data);
                send("Echo: " + data);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }while (data != null);
    }

    public static void main(String[] args) {
        Server server = new Server(1234);
        server.run();
    }
    
}