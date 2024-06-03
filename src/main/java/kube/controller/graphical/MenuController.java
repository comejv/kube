package kube.controller.graphical;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

import kube.configuration.Config;
import kube.controller.network.NetworkListener;
import kube.controller.network.NetworkSender;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.services.Network;
import kube.services.Server;
import kube.view.components.Buttons.ButtonIcon;

public class MenuController implements ActionListener, MouseListener {
    // TODO : refactor this class to make it more readable
    Queue<Action> toView;
    Queue<Action> toModel;
    Queue<Action> toNetwork;

    Network network;
    Thread networkListenerThread;
    Thread networkSenderThread;

    public MenuController(Queue<Action> toView, Queue<Action> toModel, Queue<Action> toNetwork) {
        this.toView = toView;
        this.toModel = toModel;
        this.toNetwork = toNetwork;
    }

    public void actionPerformed(ActionEvent evt) {
        switch (evt.getActionCommand()) {
            case "local":
                break;
            case "online":
                break;
            case "host":
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    String ipAddress = inetAddress.getHostAddress();
                    Config.setHostIP(ipAddress);
                } catch (UnknownHostException e) {
                    Config.error("Could not get the host IP address");
                }
                try {
                    network = new Server();
                    Config.setHostPort(((Server) network).getPort());
                    NetworkListener networkListener = new NetworkListener(network, toModel);
                    NetworkSender networkSender = new NetworkSender(network, toNetwork, 1);
                    networkListenerThread = new Thread(networkListener);
                    networkSenderThread = new Thread(networkSender);
                    toView.add(new Action(ActionType.HOST));
                } catch (IOException e) {
                    Config.error("Could not create the server.");
                    toView.add(new Action(ActionType.SERVER_ERROR));
                }
                break;
            case "returnHost":
                toNetwork.add(new Action(ActionType.STOP_NETWORK));
                ((Server) network).disconnect();
                break;
            case "refreshConnexion":
                toView.add(new Action(ActionType.REFRESH_CONNEXION, ((Server) network).connect(null, 0)));
                break;
            case "startLocal":
                toView.add(new Action(ActionType.START_LOCAL));
                break;
            case "startOnline":
                // Logique à ajouter :
                // différencier si on est serveur ou client
                // si client récupérer ip et port auquel on se connecte dans Config.getHostIP
                // et Config.getHostPort puis tester si addresse valide, sinon envoyer new
                // action PRINT_INVALID_ADDRESS à la view si oui envoyer START_ONLINE à la vue
                // et gérer la logique dans GUIEventsHandler
                // Si serveur jsp mdr

                toView.add(new Action(ActionType.PRINT_INVALID_ADDRESS));
                // networkListenerThread.start();
                // networkSenderThread.start();
                // toView.add(new Action(ActionType.START_ONLINE));
                break;
            case "rules":
                toView.add(new Action(ActionType.RULES));
                break;
            case "nextRule":
                toView.add(new Action(ActionType.NEXT_RULE));
                break;
            case "previousRule":
                toView.add(new Action(ActionType.PREVIOUS_RULE));
                break;
            case "endRule":
                toView.add(new Action(ActionType.END_RULE));
                break;
            case "quit":
                toView.add(new Action(ActionType.QUIT));
                break;
            case "volume":
                toView.add(new Action(ActionType.VOLUME));
                break;
            case "settings":
                toView.add(new Action(ActionType.SETTINGS));
                break;
            case "confirmed_settings":
                toView.add(new Action(ActionType.CONFIRMED_SETTINGS));
                break;
            default:
                break;
        }
    }

    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof ButtonIcon) {
            ButtonIcon b = (ButtonIcon) source;
            switch (b.getName()) {
                case "settings":
                    Config.debug("Settings clicked");
                    toView.add(new Action(ActionType.SETTINGS));
                    break;

                case "volume":
                    Config.debug("Volume clicked");
                    toView.add(new Action(ActionType.VOLUME));
                    break;

                default:
                    Config.error("Unrecognised buttonIcon action.");
                    break;
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        Object source = e.getSource();
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (source instanceof ButtonIcon) {
                toView.add(new Action(ActionType.SET_BUTTON_PRESSED, source));
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        Object source = e.getSource();
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (source instanceof ButtonIcon) {
                toView.add(new Action(ActionType.SET_BUTTON_RELEASED, source));
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof ButtonIcon) {
            toView.add(new Action(ActionType.SET_BUTTON_HOVERED, source));
        }
    }

    public void mouseExited(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof ButtonIcon) {
            toView.add(new Action(ActionType.SET_BUTTON_DEFAULT, source));
        }
    }
}
