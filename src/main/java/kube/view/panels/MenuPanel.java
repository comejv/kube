package kube.view.panels;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * This class extends JPanel and creates the main menu of the app.
 */
public class MenuPanel extends JPanel {
    private ActionListener buttonListener;

    public MenuPanel(ActionListener aL) {
        buttonListener = aL;
        add(new JLabel("Menu"));
        JButton b = new JButton("Phase 1");
        b.setActionCommand("phase1");
        b.addActionListener(buttonListener);
        add(b);
    }
    // b = new JButton()
    // b.addActionListener(buttonListener)
}
