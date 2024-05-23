package kube.view.panels;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kube.controller.graphical.Phase2Controller;
import kube.model.Game;

/*
 * This class extends JPanel and creates the GUI for the second phase of the game.
 */
public class SecondPhasePanel extends JPanel {
    private Phase2Controller controller;
    private Game model;

    public SecondPhasePanel(Game model, Phase2Controller controller) {
        this.model = model;
        this.controller = controller;
        add(new JLabel("Phase 2"));
        JButton b = new JButton("Menu");
        b.setActionCommand("menu");
        b.addActionListener(controller);
        add(b);
    }
}
