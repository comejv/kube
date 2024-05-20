package kube.view.panels;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kube.controller.MainController;

/*
 * This class extends JPanel and creates the GUI for the second phase of the game.
 */
public class SecondPhasePanel extends JPanel {
    private MainController controller;

    public SecondPhasePanel(MainController controller) {
        this.controller = controller;
        add(new JLabel("Phase 2"));
        JButton b = new JButton("Menu");
        b.setActionCommand("menu");
        b.addActionListener(controller.phase2Listener);
        add(b);
    }
    // b = new JButton()
    // b.addActionListener(buttonListener)
}
