package kube.view.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import kube.configuration.Config;
import kube.controller.graphical.MenuController;
import kube.view.GUI;
import kube.view.GUIColors;

public class SettingsPanel extends JPanel {

    private GUI gui;
    private int width;
    private int height;
    private MenuController buttonListener;
    private JTabbedPane tabbedPanel;
    private int tabNb;
    private String[] res = { "800 x 600", "1366 x 768", "1600 x 900", "1920 x 1080" };

    public SettingsPanel(GUI gui, MenuController buttonListener) {

        this.gui = gui;
        this.buttonListener = buttonListener;
        tabNb = 0;
        width = Math.round(Config.INIT_WIDTH / 2f);
        height = Math.round(Config.INIT_HEIGHT / 1.33f);

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);

        tabbedPanel = new JTabbedPane();
        tabbedPanel.setPreferredSize(new Dimension(width, height));
        tabbedPanel.setBackground(GUIColors.ACCENT.toColor());
        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.anchor = GridBagConstraints.CENTER;
        add(tabbedPanel, elemGBC);

        addGraphismePanel();

        JPanel audioPanel = createTab("Audio");
        addFillerPanel(audioPanel);
        
        setVisible(true);
    }

    private void addGraphismePanel() {
        JPanel graphismePanel = createTab("Graphisme");
        JComboBox<String> resolutionManager = new JComboBox<>(res);
        resolutionManager.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch ((String) resolutionManager.getSelectedItem()) {
                    // TODO : refactor this switch case : use events and a controller ?
                    case "800 x 600":
                        gui.getMainFrame().setSize(800, 600);
                        break;
                    case "1366 x 768":
                        gui.getMainFrame().setSize(1366, 768);
                        break;
                    case "1600 x 900":
                        gui.getMainFrame().setSize(1600, 900);
                        break;
                    case "1920 x 1080":
                        gui.getMainFrame().setSize(1920, 1080);
                        break;
                    case "2560 x 1440":
                        gui.getMainFrame().setSize(2560, 1440);
                        break;
                    default:
                        break;
                }
                // gui.getMainFrame().resize();
            }
        });

        JButton enlargeButton = new JButton("+");
        enlargeButton.setPreferredSize(new Dimension(50, 50));
        enlargeButton.addActionListener(e -> {
            gui.incrementUIScale(1.1);
        });

        JButton shrinkButton = new JButton("-");
        shrinkButton.setPreferredSize(new Dimension(50, 50));
        shrinkButton.addActionListener(e -> {
            gui.incrementUIScale(0.9);
        });

        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(100, 50));
        resetButton.addActionListener(e -> {
            gui.resetUIScale();
        });

        wrapInJPanel(enlargeButton, null, graphismePanel);
        wrapInJPanel(shrinkButton, null, graphismePanel);
        wrapInJPanel(resetButton, null, graphismePanel);
        resolutionManager.setPreferredSize(new Dimension(350, 50));
        wrapInJPanel(resolutionManager, null, graphismePanel);

        addFillerPanel(graphismePanel);

        JButton saveChanges = new JButton("Save changes");
        saveChanges.setPreferredSize(new Dimension(150, 50));
        saveChanges.addActionListener(buttonListener);
        saveChanges.setActionCommand("confirmed_settings");
        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.anchor = GridBagConstraints.SOUTHEAST;
        wrapInJPanel(saveChanges, elemGBC, graphismePanel);
    }

    private JPanel createTab(String name) {
        JPanel newPanel = new JPanel(new GridLayout(4, 2));

        JLabel newLabel = new JLabel(name, SwingConstants.CENTER);
        newLabel.setFont(new Font("Jomhuria", Font.PLAIN, (int) (Config.INIT_HEIGHT / 12)));
        newLabel.setForeground(GUIColors.ACCENT.toColor());
        newLabel.setPreferredSize(new Dimension(200, 50));
        tabbedPanel.addTab(name, newPanel);
        tabbedPanel.setTabComponentAt(getTabNb(), newLabel);
        setTabNb(getTabNb() + 1);

        return newPanel;
    }

    private void addFillerPanel(JPanel container) {
        // set to 7 because there's 4 x 2 cells minus one reserved for the saveChanges
        // Button
        int nbToFill = 7 - container.getComponentCount();
        for (int i = 0; i < nbToFill; i++) {
            JPanel filler = new JPanel();
            filler.setBorder(BorderFactory.createLineBorder(Color.red));
            container.add(filler);
        }
    }

    public JTabbedPane getTabbedPanel() {
        return tabbedPanel;
    }

    private void wrapInJPanel(Component c, GridBagConstraints elemGBC,
            JPanel container) {
        JPanel wraper = new JPanel(new GridBagLayout());
        container.add(wraper);
        wraper.setBorder(BorderFactory.createLineBorder(Color.red));
        wraper.add(c, elemGBC);
    }

    private int getTabNb() {
        return tabNb;
    }

    private void setTabNb(int newTabNb) {
        tabNb = newTabNb;
    }

    public void loadPanel(){
        tabbedPanel.setSelectedIndex(2);
    }
}
