package kube.services;

// import java classes
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Network {

    /**********
     * ATTRIBUTE
     **********/
    
    private Socket socket;
    
    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the class Client
     */
    public Client() {
        setWaitingForConnection(true);
    }
    
    /**
     * Constructor of the class Client
     * 
     * @param IP the IP address
     * @param port the port
     */
    public Client(String IP, int port) {
        setIp(IP);
        setPort(port);
            connect(IP, port);
    }

    /**********
     * SETTER
     **********/

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**********
     * GETTER
     **********/

    public Socket getSocket() {
        return socket;
    }

    /**********
     * METHODS
     **********/

    /**
     * Connect to the server
     * 
     * @param ip the IP address
     * @param port the port
     * @return true if the connection is successful, false otherwise
     */
    @Override
    public final boolean connect(String ip, int port){
        try {
            setIp(ip);
            setPort(port);
            setSocket(new Socket(ip, port));
            //getSocket().setSoTimeout(5000); // Set a timeout of 5 seconds
            setOut(new ObjectOutputStream(getSocket().getOutputStream()));
            setIn(new ObjectInputStream(getSocket().getInputStream()));
        } catch (IOException e) {
           return false;
        }
        return true;
    }

    /**
     * Disconnect from the server
     * 
     * @return true if the disconnection is successful, false otherwise
     */
    @Override
    public boolean disconnect() {
        try {
            getOut().close();
            getIn().close();
            getSocket().close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }    
}
