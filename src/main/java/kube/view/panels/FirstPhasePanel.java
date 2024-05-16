package kube.view.panels;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.view.GUIColors;
import kube.view.components.Buttons;
import kube.view.components.Icon;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

/*
 * This class extends JPanel and creates the GUI for the first phase of the game.
 */
public class FirstPhasePanel extends JPanel {
    private GraphicsEnvironment ge;

    public FirstPhasePanel(ActionListener buttonListener) {
        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font buttonsFont = Font.createFont(Font.TRUETYPE_FONT,
                    ResourceLoader.getResourceAsStream("fonts/Jomhuria-Regular.ttf"));
            ge.registerFont(buttonsFont);
            ge.getAvailableFontFamilyNames();
        } catch (IOException | FontFormatException e) {
            Config.debug("Error : ");
            System.err.println("Could not load buttons font, using default.");
        }

        setLayout(new GridBagLayout());
        setBackground(GUIColors.GAME_BG);

        /* Buttons panel construction */
        JPanel eastColPanel = new JPanel();
        eastColPanel.setBackground(GUIColors.GAME_BG);
        JPanel buttonsPanel = createButtons(buttonListener);
        eastColPanel.add(buttonsPanel, BorderLayout.NORTH);
        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridy = 0;
        c1.gridx = 3;
        c1.anchor = GridBagConstraints.NORTHEAST;
        c1.fill = GridBagConstraints.VERTICAL;
        c1.insets = new Insets(0, 10, 0, 10);

        add(eastColPanel, c1);

        /* Game panel construction */
        JPanel gamePanel = gamePanel();
        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridy = 0;
        c2.gridx = 0;
        c2.gridwidth = 2;
        c2.fill = GridBagConstraints.BOTH;
        c2.anchor = GridBagConstraints.CENTER;
        c2.weightx = 1;
        c2.weighty = 1;
        c2.insets = new Insets(20, 20, 20, 20);
        add(gamePanel, c2);

    }

    private JPanel gamePanel() {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());

        // TOP BAR - GAME BASE
        JPanel basePanel = new JPanel();
        basePanel.setBackground(GUIColors.GAME_BG_DARK);
        JLabel baseLabel = new JLabel("Base Centrale: ");
        baseLabel.setFont(new Font("Jomhuria", Font.PLAIN, 30));
        baseLabel.setForeground(GUIColors.TEXT);
        basePanel.add(baseLabel);
        for (int i = 0; i < 9; i++) {
            basePanel.add(newHexa(Color.RED));
        }
        gamePanel.add(basePanel, BorderLayout.NORTH);

        // CENTER - CONSTRUCTION OF PLAYER MOUNTAIN
        JPanel constructPanel = new JPanel();
        constructPanel.setBackground(GUIColors.TEXT);
        constructPanel.setLayout(new GridLayout(6, 6));
        GridBagConstraints cc = new GridBagConstraints();
        Icon[][] hexaList = new Icon[6][6];
        gamePanel.add(constructPanel, BorderLayout.CENTER);
        int last = 0;
        //RANDOM LIST;
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                hexaList[i][j] = newHexa(true);
            }
        }
        for (int i = 0; i < 6; i++) {
            JPanel lineHexa = new JPanel();
            lineHexa.setBackground(GUIColors.TEXT);
            for (int j = 5; j >= 0; j--) {
                if (hexaList[i][j] != null) {
                    lineHexa.add(hexaList[i][j]);
                }
            }
            if (i != 5) {
                cc.insets = new Insets(0, 60 / 2, 0, 60 / 2);
            }
            if (i % 2 == 1) {
                last = i - (i - 5) * 2;
            }
            cc.gridx = last;
            cc.gridy = i;
            constructPanel.add(lineHexa, cc);
        }

        // SIDE BAR - PIECES AVAILABLE
        // for this part need: getAvailableColors() and loop on it
        JPanel piecesPanel = new JPanel();
        piecesPanel.setBackground(GUIColors.TEXT_HOVER);
        piecesPanel.setLayout(new GridLayout(4, 2));
        GridBagConstraints c = new GridBagConstraints();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                JPanel mini = new JPanel();
                mini.setBackground(GUIColors.TEXT_HOVER);
                JLabel numOfPieces = new JLabel("x3");
                numOfPieces.setFont(new Font("Jomhuria", Font.PLAIN, 20));
                mini.add(newHexa(false));
                mini.add(numOfPieces);
                c.gridy = j;
                c.gridx = i;
                piecesPanel.add(mini);
            }
        }
        gamePanel.add(piecesPanel, BorderLayout.EAST);
        return gamePanel;
    }

    private JPanel createButtons(ActionListener a) {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(4, 1));
        buttons.setPreferredSize(new Dimension(Config.getInitWidth() / 5, Config.getInitHeight() / 5));
        buttons.setBackground(GUIColors.GAME_BG);

        JButton optButton = new Buttons.GameFirstPhaseButton("Options");
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

        JButton validerButton = new Buttons.GameFirstPhaseButton("Valider");
        validerButton.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        validerButton.setActionCommand("phase2");
        validerButton.addActionListener(a);
        buttons.add(validerButton);
        return buttons;
    }

    public static Icon newHexa(boolean opt ) {
        Icon hexa;
        if (opt){
             hexa = new Icon(ResourceLoader.getBufferedImage("hexaVide"));
        }
        else{
             hexa = new Icon(ResourceLoader.getBufferedImage("hexaBlanc"));
        }
        hexa.resizeIcon(60, 60);
        return hexa;
    }

    public static Icon newHexa(Color c) {
        Icon hexa = new Icon(ResourceLoader.getBufferedImage("hexaBlanc"));
        hexa.resizeIcon(60, 60);
        hexa.recolor(c);
        return hexa;
    }
}
