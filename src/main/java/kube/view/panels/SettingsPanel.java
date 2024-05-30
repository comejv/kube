package kube.view.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import kube.view.MainFrame;

public class SettingsPanel extends JPanel{
    
    private GUI gui;
    private int width;
    private int height;
    private MenuController buttonListener;
    private JTabbedPane tabbedPanel;
    private int tabNb;
    private String[] res = {"800 x 600", "1280 x 920", "1920 x 1080"};

    public SettingsPanel(GUI gui, MenuController buttonListener){

        this.gui = gui;
        this.buttonListener = buttonListener;
        tabNb = 0;
        width = Config.getWidth()/2;
        height = Math.round(Config.getHeight()/1.33f);

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

    private void addGraphismePanel(){
        JPanel graphismePanel = createTab("Graphisme");
        JComboBox resolution = new JComboBox(res);
        resolution.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch ((String) resolution.getSelectedItem()) {
                    case "800 x 600":
                        Config.setResolution(800, 600);
                        break;
                    case "1280 x 920":
                        Config.setResolution(1280, 920);
                        break;
                    default:
                        break;
                }
                gui.getMainFrame().resize();
            }
        });

        resolution.setPreferredSize(new Dimension(350, 50));
        wrapInJPanel(resolution, null, graphismePanel);
        
        addFillerPanel(graphismePanel);

        JButton saveChanges = new JButton("Save changes");
        saveChanges.setPreferredSize(new Dimension(150, 50));
        saveChanges.addActionListener(buttonListener);
        saveChanges.setActionCommand("confirmed_settings");
        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.anchor = GridBagConstraints.SOUTHEAST;
        wrapInJPanel(saveChanges, elemGBC, graphismePanel);
    }
    
    private JPanel createTab(String name){
        JPanel newPanel = new JPanel(new GridLayout(4,2));
        
        JLabel newLabel = new JLabel(name, SwingConstants.CENTER);
        newLabel.setFont(new Font("Jomhuria", Font.PLAIN, (int) (Config.getHeight() / 12)));
        newLabel.setForeground(GUIColors.ACCENT.toColor());
        newLabel.setPreferredSize(new Dimension(200,50));
        tabbedPanel.addTab(name, newPanel);
        tabbedPanel.setTabComponentAt(getTabNb(), newLabel);
        setTabNb(getTabNb() + 1);

        return newPanel;
    }

    private void addFillerPanel(JPanel container){
        //set to 7 because there's 4 x 2 cells minus one reserved for the saveChanges Button
        int nbToFill = 7 - container.getComponentCount();
        for (int i = 0; i < nbToFill; i++) {
            JPanel filler = new JPanel();
            filler.setBorder(BorderFactory.createLineBorder(Color.red));
            container.add(filler);
        }
    }

    private void wrapInJPanel(Component c, GridBagConstraints elemGBC, 
                              JPanel container) {
        JPanel wraper = new JPanel(new GridBagLayout());
        container.add(wraper);
        wraper.setBorder(BorderFactory.createLineBorder(Color.red));
        wraper.add(c, elemGBC);
    }

    private int getTabNb(){
        return tabNb;
    }

    private void setTabNb(int newTabNb){
        tabNb = newTabNb;
    }
}
