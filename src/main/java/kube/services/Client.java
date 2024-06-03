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
        try {
            connect(IP, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public final boolean connect(String ip, int port) throws IOException {
        try {
            setIp(ip);
            setPort(port);
            setSocket(new Socket(ip, port));
            setOut(new ObjectOutputStream(getSocket().getOutputStream()));
            setIn(new ObjectInputStream(getSocket().getInputStream()));
        } catch (IOException e) {
            throw new IOException(e.getMessage());
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
