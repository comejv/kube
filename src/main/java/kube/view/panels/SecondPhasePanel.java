package kube.view.panels;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

/*
 * This class extends JPanel and creates the GUI for the second phase of the game.
 */
public class SecondPhasePanel extends JPanel {
    private ActionListener buttonListener;

    public SecondPhasePanel(ActionListener a) {
        buttonListener = a;
    }
    // b = new JButton()
    // b.addActionListener(buttonListener)
}
