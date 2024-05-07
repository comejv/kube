package kube.view.panels;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

/*
 * This class extends JPanel and creates the main menu of the app.
 */
public class MenuPanel extends JPanel {
    private ActionListener buttonListener;

    public MenuPanel(ActionListener a) {
        buttonListener = a;
    }
    // b = new JButton()
    // b.addActionListener(buttonListener)
}
