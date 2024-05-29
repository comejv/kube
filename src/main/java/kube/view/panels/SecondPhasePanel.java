package kube.view.panels;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.view.GUI;
import kube.view.GUIColors;
import kube.view.HSL;
import kube.view.components.Buttons;
import kube.view.components.HexIcon;

import java.awt.*;
import java.awt.event.ActionListener;

import kube.controller.graphical.Phase2Controller;
import kube.model.Game;

/*
 * This class extends JPanel and creates the GUI for the second phase of the game.
 */
public class SecondPhasePanel extends JPanel {
    private Phase2Controller controller;
    private Game model;
    private GUI gui;

    public SecondPhasePanel(GUI gui, Game model, Phase2Controller controller) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBackground(GUIColors.GAME_BG.toColor());
        // EAST
        JPanel eastPane = createEastPanel(controller);
        gbc.gridy = 0;
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(0, 10, 0, 10);
        add(eastPane, gbc);
        JPanel gamePanel = gamePanel();
        gamePanel.setBackground(GUIColors.TEXT.toColor());
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



    private JPanel createEastPanel(ActionListener a) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(4, 1));
        //buttons.setPreferredSize(new Dimension(Config.getInitWidth() / 5, Config.getInitHeight() / 5));
        buttons.setOpaque(false);

        JButton optButton = new Buttons.GameFirstPhaseButton("Menu");
        optButton.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        optButton.setActionCommand("menu");
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
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel gamePanel() {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel p1 = pyra(0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(p1, gbc);
        JPanel p2 = pyra(0);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(p2, gbc);
        JPanel base = pyra(-1);
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        gbc.anchor= GridBagConstraints.CENTER;

        gamePanel.add(base,gbc);
        return gamePanel;
    }

    private JPanel pyra(int rowMissing) {
        JPanel constructPanel = new JPanel();
        constructPanel.setOpaque(false);
        constructPanel.setLayout(new GridBagLayout());
        //constructPanel.setBorder(BorderFactory.createLineBorder(Color.RED));

        GridBagConstraints gbc = new GridBagConstraints();
        int i;
        for (i = 1; i <= 6; i++) {
            if (i <= rowMissing) {
                continue;
            }
            JPanel lineHexa = new JPanel();
            lineHexa.setLayout(new GridLayout(1, i));
            lineHexa.setOpaque(false);
            //lineHexa.setBorder(BorderFactory.createLineBorder(Color.BLUE));

            for (int j = 0; j < i; j++) {
                if(rowMissing==-1){
                    lineHexa.add(newHexa(null, true));
                }
                else{
                    lineHexa.add(newHexa(GUIColors.BLUE_HEX, true));
                }
            }
            gbc.gridx = 0;
            gbc.gridy = i;
            // gbc.anchor = GridBagConstraints.CENTER;
            constructPanel.add(lineHexa, gbc);
        }
        return constructPanel;
    }

    public static HexIcon newHexa(HSL c, boolean isActionable) {
        HexIcon hexa;
        if (c == null) {
            hexa = new HexIcon(ResourceLoader.getBufferedImage("wireHexa"), isActionable);
        } else {
            hexa = new HexIcon(ResourceLoader.getBufferedImage("hexaWhiteTextured"), isActionable);
            hexa.recolor(c);
        }
        hexa.resizeIcon(50, 50);
        return hexa;
    }
}
