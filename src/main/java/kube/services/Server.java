package kube.services;

// Import java classes
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;

public class Server extends Network {

    /**********
     * ATTRIBUTES
     **********/
    private Queue<Action> eventsToView;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private ExecutorService executorService;
    private Thread acceptThread;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the class Server
     * 
     * @param port the port
     */
    public Server(int port, Queue<Action> eventsToView) throws IOException {
        try {
            setEventsToView(eventsToView);
            init(port);
            setWaitingForConnection(true);
        } catch (IOException e) {
            throw new IOException("Could not initialize the server.");
        }
    }

    /*
     * Constructor of the class Server that finds a free port
     */
    public Server(Queue<Action> eventsToView) throws IOException {
        try {
            setEventsToView(eventsToView);
            init(0);
            setWaitingForConnection(true);
        } catch (IOException e) {
            throw new IOException("Could not initialize the server.");
        }
    }

    /**********
     * INITIALIZATION
     **********/

    /**
     * Initialize the server
     * 
     * @param port the port, if port = 0, the server will find a free port
     */
    public final void init(int port) throws IOException {
        try {
            setServerSocket(new ServerSocket(port));
            setClientSocket(null);
            setOut(null);
            setIn(null);

            executorService = Executors.newSingleThreadExecutor();
            acceptThread = new Thread(() -> {
                try {
                    setClientSocket(getServerSocket().accept());
                    Config.debug("Client connected on port " + getPort() + ".");
                    setOut(new ObjectOutputStream(getClientSocket().getOutputStream()));
                    setIn(new ObjectInputStream(getClientSocket().getInputStream()));
                    if (eventsToView != null){
                        eventsToView.add(new Action(ActionType.PRINT_CONNECTION_ETABLISHED));
                    }
                } catch (IOException e) {
                    Config.error("Could not accept the client.");
                    e.printStackTrace();
                }
                Config.debug("Connection " + getClientSocket().getInetAddress().getHostAddress() + " on port "
                        + getClientSocket().getPort() + " accepted.");
            });
            executorService.submit(acceptThread);
        } catch (IOException e) {
            throw new IOException("Could not listen on port: " + port + ".");
        }
    }

    /**********
     * SETTERS
     **********/
    public void setEventsToView(Queue<Action> eventsToView) {
        this.eventsToView = eventsToView;
    }

    public final void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public final void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**********
     * GETTERS
     **********/

    public Queue<Action> getEventsToView() {
        return eventsToView;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public int getPort() {
        return getServerSocket().getLocalPort();
    }

    /**********
     * METHODS
     **********/

    /**
     * Is client connected to the server ?
     * 
     * @param ip   the IP address
     * @param port the port
     * @return true if the connection is successful, false otherwise
     */
    @Override
    public boolean connect(String ip, int port) {
        return !(clientSocket == null) && clientSocket.isConnected();
    }

    /**
     * Close client socket.
     * 
     * @return true if the disconnection is successful, false otherwise
     */
    @Override
    public boolean disconnect() {

        try {
            getOut().close();
            getIn().close();
            getClientSocket().close();
            getServerSocket().close();
        } catch (IOException | NullPointerException e) {
            Config.error("Failed to close connexion", e);
            return false;
        }

        return true;
    }

    /**
     * Check if the current instance is a server
     * 
     * @return true
     */
    @Override
    public boolean isServer() {
        return true;
    }
}
