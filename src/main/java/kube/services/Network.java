package kube.services;

// Import model class
import kube.model.action.Action;

// Import java classes
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class Network {

    /**********
     * ATTRIBUTES
     **********/

    @SuppressWarnings("unused")
    private String ip;

    @SuppressWarnings("unused")
    private int port;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**********
     * SETTERS
     **********/

    public final void setIp(String ip) {
        this.ip = ip;
    }

    public final void setPort(int port) {
        this.port = port;
    }

    public final void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public final void setIn(ObjectInputStream in) {
        this.in = in;
    }

    /**********
     * GETTERS
     **********/

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    /**********
     * ABSTRACT METHODS
     **********/

    /**
     * Connect to the server
     * 
     * @param ip the IP address
     * @param port the port
     * @return true if the connection is successful, false otherwise
     */
    public abstract boolean connect(String ip, int port);

    /**
     * Disconnect from the server
     * 
     * @return true if the disconnection is successful, false otherwise
     */
    public abstract boolean disconnect();

    /**********
     * OTHER METHODS
     **********/

    /**
     * Check if the current instance is a server
     * 
     * @return false
     */
    public boolean isServer() {
        return false;
    }

    /**
     * Send an action to the server
     * 
     * @param action the action to send
     * @return true if the data is sent, false otherwise
     */
    public boolean send(Action action) {

        try {
            if (getOut() != null) {
                getOut().writeObject(action);
                getOut().flush();
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public Action receive() throws IOException {
        try {
            Action action;
            action = (Action) getIn().readObject();
            return action;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
