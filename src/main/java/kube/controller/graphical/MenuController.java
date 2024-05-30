package kube.controller.graphical;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import kube.configuration.Config;
import kube.controller.network.NetworkListener;
import kube.controller.network.NetworkSender;
import kube.model.Game;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.view.components.Buttons.ButtonIcon;

import kube.services.Network;
import kube.services.Client;
import kube.services.Server;

public class MenuController implements ActionListener, MouseListener {
    Kube kube;
    Queue<Action> toView;
    Queue<Action> toModel;
    Queue<Action> toNetwork;
    Network network;
    NetworkListener networkListener;
    NetworkSender networkSender;
    Thread networkSenderThread;
    Thread networkListenerThread;

    public MenuController(Kube kube, Queue<Action> toView, Queue<Action> toModel, Queue<Action> toNetwork) {
        this.kube = kube;
        this.toView = toView;
        this.toModel = toModel;
        this.toNetwork = toNetwork;
    }

    public void actionPerformed(ActionEvent evt) {
        switch (evt.getActionCommand()) {
            case "local":
                toView.add(new Action(ActionType.PLAY_LOCAL));
                Thread modelThread = new Thread(new Game(Game.LOCAL, kube, toModel, toView, null));
                modelThread.start();
                break;
            case "play":
                toView.add(new Action(ActionType.START));
                break;
            case "joinGame":
                toView.add(new Action(ActionType.PLAY_ONLINE));
                network = new Client();
                network.connect(Config.getIp(), Config.getPort());
                networkListener = new NetworkListener(network, toModel);
                networkSender = new NetworkSender(network, toNetwork, 2);
                Thread networkThread = new Thread(networkListener);
                networkThread.start();
                Thread networkSenderThread = new Thread(networkSender);
                networkSenderThread.start();

                break;
            case "hostGame":
                toView.add(new Action(ActionType.PLAY_ONLINE));
                network = new Server(Config.getPort());
                networkListener = new NetworkListener(network, toModel);
                networkSender = new NetworkSender(network, toNetwork, 1);
                networkThread = new Thread(networkListener);
                networkThread.start();
                networkSenderThread = new Thread(networkSender);
                networkSenderThread.start();
                break;
            // TODO Handle game settings (AI, nb of players)
            case "rules":
                toView.add(new Action(ActionType.RULES));
                break;
            case "nextRule":
                toView.add(new Action(ActionType.NEXT_RULE));
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
            case "parameters":
                toView.add(new Action(ActionType.PARAMETERS));
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
                    System.err.println("Unrecognised buttonIcon action.");
                    break;
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        Object source = e.getSource();
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (source instanceof JButton || source instanceof ButtonIcon) {
                toView.add(new Action(ActionType.SET_BUTTON_PRESSED, source));
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        Object source = e.getSource();
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (source instanceof JButton || source instanceof ButtonIcon) {
                toView.add(new Action(ActionType.SET_BUTTON_RELEASED, source));
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof JButton || source instanceof ButtonIcon) {
            toView.add(new Action(ActionType.SET_BUTTON_HOVERED, source));
        }
    }

    public void mouseExited(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof JButton || source instanceof ButtonIcon) {
            toView.add(new Action(ActionType.SET_BUTTON_DEFAULT, source));
        }
    }
}
