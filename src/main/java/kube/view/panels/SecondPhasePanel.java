package kube.view.panels;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.view.GUIColors;
import kube.view.HSL;
import kube.view.components.Buttons;
import kube.view.components.Icon;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

/*
 * This class extends JPanel and creates the GUI for the second phase of the game.
 */
public class SecondPhasePanel extends JPanel {
    //private ActionListener buttonListener;

    public SecondPhasePanel(ActionListener buttonListener) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBackground(GUIColors.GAME_BG.toColor());
        //EAST
        JPanel eastPane = createEastPanel(buttonListener);
        gbc.gridy = 0;
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(0, 10, 0, 10);
        add(eastPane, gbc);
        JPanel gamePanel = gamePanel();
        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(20, 20, 20, 20);
        add(gamePanel, gbc);
    }
    private JPanel createEastPanel(ActionListener a){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(4, 1));
        buttons.setPreferredSize(new Dimension(Config.getInitWidth() / 5, Config.getInitHeight() / 5));
        buttons.setOpaque(false);

        JButton optButton = new Buttons.GameFirstPhaseButton("Menu");
        optButton.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        optButton.setActionCommand("Menu");
        optButton.addActionListener(a);
        buttons.add(optButton);

        JButton sugIaButton = new Buttons.GameFirstPhaseButton("Suggestion IA");
        sugIaButton.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        buttons.add(sugIaButton);

        JButton annulerButton = new Buttons.GameFirstPhaseButton("Annuler");
        annulerButton.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        buttons.add(annulerButton);

        JButton validerButton = new Buttons.GameFirstPhaseButton("Refaire");
        validerButton.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        buttons.add(validerButton);

        panel.add(buttons);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel histPanel = new JPanel();
        histPanel.setLayout(new BoxLayout(histPanel, BoxLayout.Y_AXIS));
        scrollPane.setViewportView(histPanel);
        scrollPane.setPreferredSize(new Dimension(75, 300));
        scrollPane.setOpaque(false);
        JLabel histoText = new JLabel("HISTO");
        histoText.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        histoText.setForeground(GUIColors.TEXT.toColor());
        panel.add(histoText);
        panel.add(scrollPane,BorderLayout.CENTER);

        return panel;
    }

    private JPanel gamePanel() {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(null);
        JPanel p1 = pyra(1,0);
        JPanel p2 = pyra(2,3);
        JPanel base = base();
        int w1= width_p1_mountain+20;
        int w2=width_p2_mountain+20;
        int w3=width_base_mountain+20;

        p1.setBounds(0, 0, 320,w1);
        gamePanel.add(p1);
        p2.setBounds(325, 0, 320,w2);
        gamePanel.add(p2);
        base.setBounds(100, 400, 500,w3);
        gamePanel.add(base);

        return gamePanel;
    }
    int width_p1_mountain;
    int width_p2_mountain;
    int width_base_mountain=450;// if icon size changes: fucked! so don't forget to modify it
    private JPanel pyra(int player, int rowMissing){
        JPanel constructPanel = new JPanel();
        constructPanel.setOpaque(false);
        constructPanel.setLayout(new GridBagLayout());
       constructPanel.setBorder(BorderFactory.createLineBorder(Color.RED));

        GridBagConstraints gbc = new GridBagConstraints();
        int i;
        for ( i = 1; i <= 6; i++) {
            if (i<=rowMissing){
                continue;
            }
            JPanel lineHexa = new JPanel();
            lineHexa.setLayout(new GridLayout(1, i));
            lineHexa.setOpaque(false);
            lineHexa.setBorder(BorderFactory.createLineBorder(Color.BLUE));

            for (int j = 0; j < i; j++) {
                lineHexa.add(newHexa(GUIColors.BLUE_HEX));
            }
            gbc.gridx = 0;
            gbc.gridy = i;
            //gbc.anchor = GridBagConstraints.CENTER;
            constructPanel.add(lineHexa, gbc);
        }
        if(player==1){
            width_p1_mountain=(i-1)*50;
        }
        else if (player==2){
            width_p2_mountain=(i-1-rowMissing)*50;
        }
        return constructPanel;
    }

    private JPanel base (){
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        for (int i=0;i<9;i++){
            topPanel.add(newHexa(GUIColors.GREEN_HEX));
        }
        return topPanel;
    }
    public static Icon newHexa(boolean opt) {
        Icon hexa;
        if (opt) {
            hexa = new Icon(ResourceLoader.getBufferedImage("hexaEmpty"));
        } else {
            hexa = new Icon(ResourceLoader.getBufferedImage("hexaGrayTextured"));
        }
        hexa.resizeIcon(60, 60);
        return hexa;
    }

    public static Icon newHexa(HSL c) {
        Icon hexa = new Icon(ResourceLoader.getBufferedImage("hexaWhiteTextured"));
        hexa.resizeIcon(50, 50);
        hexa.recolor(c);
        return hexa;
    }
}
