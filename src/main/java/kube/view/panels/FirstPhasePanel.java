package kube.view.panels;

import kube.view.components.Buttons;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * This class extends JPanel and creates the GUI for the first phase of the game.
 */
public class FirstPhasePanel extends JPanel {
    private ActionListener buttonListener;

    public FirstPhasePanel(ActionListener aL) {
        buttonListener = aL;
        add(new JLabel("Phase 1"));
        JButton b = new Buttons.GameFirstPhaseButton("Valider");

        b.setActionCommand("phase2");
        b.addActionListener(buttonListener);
        add(b);
    }
    // b = new JButton()
    // b.addActionListener(buttonListener)
}
