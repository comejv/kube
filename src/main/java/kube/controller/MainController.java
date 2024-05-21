package kube.controller;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;
import java.awt.Component;

import kube.view.GUI;
import kube.configuration.Config;
import kube.view.components.HexIcon;
import kube.view.components.Buttons.ButtonIcon;
import kube.model.Kube;
import kube.model.Game;
import kube.model.action.*;

/*
 * This class will create the view and handle user inputs through listeners implemented as subclasses.
 */
public class MainController {
    private GUI gui;
    private Game model;
    Queue<Action> eventsToModel;
    Queue<Action> eventsToView;

    public menuListener menuListener;
    public overlayedHexaListener overlayedHexaListener;
    public phase1Listener phase1Listener;
    public phase2Listener phase2Listener;

    public MainController() {
        menuListener = new menuListener();
        overlayedHexaListener = new overlayedHexaListener();
        phase1Listener = new phase1Listener();
        phase2Listener = new phase2Listener();

        Kube kube = new Kube();
        eventsToView = new Queue<>();
        eventsToModel = new Queue<>();

        // model = new Game(Game.LOCAL, kube, eventsToModel, eventsToView);

        Thread modelThread = new Thread(model);

        modelThread.start();
        gui = new GUI(model, this);
    }

    private class overlayedHexaListener extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            Config.debug("Mouse released");
            Object source = e.getSource();
            HexIcon h = (HexIcon) source;
            h.setPressed(false);

            gui.removeOverlay();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Config.debug("Mouse dragged in hex");
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Config.debug("Mouse moved in hex");
            Object source = e.getSource();

            HexIcon h = (HexIcon) source;
            h.setOffset(e.getX(), e.getY());
            h.repaint();
        }
    }

    // menu listeners
    private class menuListener implements ActionListener, MouseListener {
        public void actionPerformed(ActionEvent evt) {
            switch (evt.getActionCommand()) {
                case "play":
                    gui.showPanel(GUI.PHASE1);
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
                        break;

                    case "volume":
                        Config.debug("Volume clicked");
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
                if (source instanceof ButtonIcon) {
                    ButtonIcon b = (ButtonIcon) source;
                    b.setPressed(true);
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            Object source = e.getSource();
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (source instanceof ButtonIcon) {
                    ButtonIcon b = (ButtonIcon) source;
                    b.setPressed(false);
                }
            }
        }

        public void mouseEntered(MouseEvent e) {
            Object source = e.getSource();
            if (source instanceof ButtonIcon) {
                ButtonIcon b = (ButtonIcon) source;
                b.setHovered(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Object source = e.getSource();
            if (source instanceof ButtonIcon) {
                ButtonIcon b = (ButtonIcon) source;
                b.setHovered(false);
            }
        }
    }

    // phase 1 listener
    private class phase1Listener implements ActionListener, MouseListener, MouseMotionListener {
        public void actionPerformed(ActionEvent evt) {
            switch (evt.getActionCommand()) {
                case "phase2":
                    gui.showPanel(GUI.PHASE2);
                    break;
                case "menu": // change to opts.
                    gui.showPanel(GUI.MENU);

                default:
                    break;
            }
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            Object source = e.getSource();
            if (source instanceof HexIcon) {
                Config.debug("Hexa pressed");
                HexIcon h = (HexIcon) source;
                HexIcon clone = h.clone();
                clone.setOffset(e.getX(), e.getY());
                clone.addMouseListener(overlayedHexaListener);
                clone.addMouseMotionListener(overlayedHexaListener);
                gui.addOverlay(clone);
            }
        }

        public void mouseMoved(MouseEvent e) {

        }

        public void mouseDragged(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {
            Config.debug("Mouse released");
            if (e.getSource() instanceof Component) {
                // Get dropoff location
                Component source = (Component) e.getSource();
                Container grid = source.getParent().getParent();
                Config.debug(grid.getComponentCount());
                if (grid != null && grid.getLayout() instanceof GridLayout) {
                    Config.debug("mouse x " + e.getX());
                    Config.debug("mouse y " + e.getY());
                    int gridX = (e.getX()) / (grid.getWidth() / grid.getComponentCount());
                    int gridY = (e.getY()) / (grid.getHeight() / grid.getComponentCount());
                    Config.debug("Dropped in grid cell (" + gridX + ", " + gridY + ")");
                }
            }
            gui.removeOverlay();
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    // phase 2 listener
    private class phase2Listener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            gui.showPanel(GUI.MENU);
        }
    }
}
