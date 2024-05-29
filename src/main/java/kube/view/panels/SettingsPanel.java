package kube.view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import kube.configuration.Config;
import kube.controller.graphical.MenuController;
import kube.view.GUI;
import kube.view.GUIColors;

public class SettingsPanel extends JPanel{
    
    private GUI gui;
    private int width;
    private int height;
    private MenuController buttonListener;
    private JTabbedPane tabbedPanel;
    private int tabNb;

    public SettingsPanel(GUI gui, MenuController buttonListener){

        this.gui = gui;
        this.buttonListener = buttonListener;
        tabNb = 0;
        width = Config.getInitWidth()/2;
        height = Config.getInitHeight()/2;

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);

        tabbedPanel = new JTabbedPane();
        tabbedPanel.setPreferredSize(new Dimension(width, height));
        tabbedPanel.setBackground(GUIColors.ACCENT.toColor());
        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.anchor = GridBagConstraints.CENTER;
        add(tabbedPanel, elemGBC);

        JPanel graphismePanel = createTab("Graphisme");

        JPanel audioPanel = createTab("Audio");

    }

    private JPanel createTab(String name){
        JPanel newPanel = new JPanel(new GridBagLayout());
        newPanel.setPreferredSize(getSize());
        newPanel.setOpaque(true);

        JButton saveChanges = new JButton("Save changes");
        saveChanges.setSize(new Dimension(200, 50));
        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.gridx = 0;
        elemGBC.gridy = 1;
        elemGBC.anchor = GridBagConstraints.LAST_LINE_END;
        elemGBC.insets = new Insets(0, 0, 30, 30);
        newPanel.add(saveChanges, elemGBC);

        JLabel newLabel = new JLabel(name, SwingConstants.CENTER);
        newLabel.setFont(new Font("Jomhuria", Font.PLAIN, (int) (Config.getInitHeight() / 12)));
        newLabel.setForeground(GUIColors.ACCENT.toColor());
        newLabel.setPreferredSize(new Dimension(200,50));
        tabbedPanel.addTab(name, newPanel);
        tabbedPanel.setTabComponentAt(getTabNb(), newLabel);
        setTabNb(getTabNb() + 1);

        return newPanel;
    }

    private int getTabNb(){
        return tabNb;
    }

    private void setTabNb(int newTabNb){
        tabNb = newTabNb;
    }
}
