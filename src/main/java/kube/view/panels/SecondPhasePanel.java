package kube.view.panels;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * This class extends JPanel and creates the GUI for the second phase of the game.
 */
public class SecondPhasePanel extends JPanel {
    private ActionListener buttonListener;

    public SecondPhasePanel(ActionListener aL) {
        buttonListener = aL;
        add(new JLabel("Phase 2"));
        JButton b = new JButton("Menu");
        b.setActionCommand("menu");
        b.addActionListener(buttonListener);
        add(b);
    }
    // b = new JButton()
    // b.addActionListener(buttonListener)
}
