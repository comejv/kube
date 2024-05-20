package kube.controller;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

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

    public MainController() {
        Kube kube = new Kube();
        eventsToView = new Queue<>();
        eventsToModel = new Queue<>();

        // model = new Game(Game.LOCAL, kube, eventsToModel, eventsToView);

        Thread modelThread = new Thread(model);

        modelThread.start();
        gui = new GUI(model, new menuListener(), new phase1Listener(), new hexaListener(), new phase2Listener());
    }

    public class hexaListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            Object source = e.getSource();
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (source instanceof HexIcon) {
                    HexIcon h = (HexIcon) source;
                    h.setPressed(true);
                    h.setOffset(e.getX(), e.getY());
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Object source = e.getSource();
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (source instanceof HexIcon) {
                    HexIcon h = (HexIcon) source;
                    h.setPressed(false);
                    h.setGrabbed(false);

                    // Get dropoff location
                    Container parent = h.getParent();
                    if (parent != null && parent.getLayout() instanceof GridLayout) {
                        int gridX = (h.getX() + h.getXOffset()) / (parent.getWidth() / parent.getComponentCount());
                        int gridY = (h.getY() + h.getYOffset()) / (parent.getHeight() / parent.getComponentCount());

                        System.out.println("Dropped at grid cell: (" + gridX + ", " + gridY + ")");
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            Object source = e.getSource();
            if (source instanceof HexIcon) {
                HexIcon h = (HexIcon) source;
                h.setHovered(true);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Object source = e.getSource();

            if (source instanceof HexIcon) {
                HexIcon h = (HexIcon) source;
                if (h.isPressed()) {
                    int newX = h.getX() + e.getX() - h.getXOffset();
                    int newY = h.getY() + e.getY() - h.getYOffset();

                    h.setLocation(newX, newY);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Object source = e.getSource();
            if (source instanceof ButtonIcon) {
                ButtonIcon b = (ButtonIcon) source;
                b.setHovered(false);
            }
        }
    }

    // menu listeners
    public class menuListener implements ActionListener, MouseListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            switch (evt.getActionCommand()) {
                case "play":
                    gui.showPanel(GUI.PHASE1);
                    break;

                default:
                    break;
            }
        }

        @Override
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

        @Override
        public void mousePressed(MouseEvent e) {
            Object source = e.getSource();
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (source instanceof ButtonIcon) {
                    ButtonIcon b = (ButtonIcon) source;
                    b.setPressed(true);
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Object source = e.getSource();
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (source instanceof ButtonIcon) {
                    ButtonIcon b = (ButtonIcon) source;
                    b.setPressed(false);
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            Object source = e.getSource();
            if (source instanceof ButtonIcon) {
                ButtonIcon b = (ButtonIcon) source;
                b.setHovered(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Object source = e.getSource();
            if (source instanceof ButtonIcon) {
                ButtonIcon b = (ButtonIcon) source;
                b.setHovered(false);
            }
        }
    }

    // phase 1 listener
    public class phase1Listener implements ActionListener {
        @Override
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
    }

    // phase 2 listener
    public class phase2Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            gui.showPanel(GUI.MENU);
        }
    }
}
