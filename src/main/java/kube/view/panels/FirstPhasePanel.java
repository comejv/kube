package kube.view.panels;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

/*
 * This class extends JPanel and creates the GUI for the first phase of the game.
 */
public class FirstPhasePanel extends JPanel {
    private ActionListener buttonListener;

    public FirstPhasePanel(ActionListener a) {
        buttonListener = a;
    }
    // b = new JButton()
    // b.addActionListener(buttonListener)
}
