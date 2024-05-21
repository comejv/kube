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
        gamePanel.setLayout(new BorderLayout());

        // CENTER - CONSTRUCTION OF PLAYER MOUNTAIN
        JPanel constructPanel = new JPanel();
        constructPanel.setBackground(GUIColors.TEXT.toColor());
        constructPanel.setLayout(new GridLayout(6, 6));
        GridBagConstraints gbc = new GridBagConstraints();
        kube.view.components.Icon[][] hexaList = new Icon[6][6];
        gamePanel.add(constructPanel, BorderLayout.CENTER);
        int last = 0;
        // will change to getting base colours from model
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                hexaList[i][j] = newHexa(true);
            }
        }
        for (int i = 0; i < 6; i++) {
            JPanel lineHexa = new JPanel();
            lineHexa.setOpaque(false);
            for (int j = 5; j >= 0; j--) {
                if (hexaList[i][j] != null) {
                    lineHexa.add(hexaList[i][j]);
                }
            }
            if (i != 5) {
                gbc.insets = new Insets(0, 60 / 2, 0, 60 / 2);
            }
            if (i % 2 == 1) {
                last = i - (i - 5) * 2;
            }
            gbc.gridx = last;
            gbc.gridy = i;
            constructPanel.add(lineHexa, gbc);
        }
        return gamePanel;
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
        hexa.resizeIcon(60, 60);
        hexa.recolor(c);
        return hexa;
    }
}
