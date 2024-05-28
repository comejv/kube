package kube.view.panels;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.controller.graphical.DnDController;
import kube.controller.graphical.Phase1Controller;
import kube.model.Game;
import kube.view.GUI;
import kube.view.GUIColors;
import kube.view.HSL;
import kube.view.components.Buttons;
import kube.view.components.HexIcon;
import kube.view.components.Icon;

import java.awt.*;

import javax.swing.*;

/*
 * This class extends JPanel and creates the GUI for the first phase of the game.
 */
public class FirstPhasePanel extends JPanel {
    private Game model;
    private Phase1Controller controller;
    private GUI gui;

    public FirstPhasePanel(GUI gui, Game model, Phase1Controller controller) {
        this.gui = gui;
        this.model = model;
        this.controller = controller;
        setLayout(new GridBagLayout());
        setBackground(GUIColors.GAME_BG.toColor());
        gui.createGlassPane();
        gui.setGlassPaneController(new DnDController(null));

        /* Buttons panel construction */
        JPanel buttonsPanel = createButtons();
        buttonsPanel.setBackground(GUIColors.GAME_BG.toColor());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(0, 10, 0, 10);

        add(buttonsPanel, gbc);

        /* Game panel construction */
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

    private JPanel gamePanel() {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());

        // TOP BAR - GAME BASE
        JPanel topPanel = new JPanel();
        topPanel.setBackground(GUIColors.GAME_BG_DARK.toColor());
        JLabel baseLabel = new JLabel("Base Centrale: ");
        baseLabel.setFont(new Font("Jomhuria", Font.PLAIN, 30));
        baseLabel.setForeground(GUIColors.TEXT.toColor());
        topPanel.add(baseLabel);

        topPanel.add(newHexa(GUIColors.YELLOW_HEX, false));
        topPanel.add(newHexa(GUIColors.BLACK_HEX, false));
        topPanel.add(newHexa(GUIColors.BLUE_HEX, false));
        topPanel.add(newHexa(GUIColors.RED_HEX, false));
        topPanel.add(newHexa(GUIColors.GREEN_HEX, false));
        topPanel.add(newHexa(GUIColors.RED_HEX, false));
        topPanel.add(newHexa(GUIColors.BLUE_HEX, false));
        topPanel.add(newHexa(GUIColors.BLUE_HEX, false));
        topPanel.add(newHexa(GUIColors.YELLOW_HEX, false));

        gamePanel.add(topPanel, BorderLayout.NORTH);

        // CENTER - CONSTRUCTION OF PLAYER MOUNTAIN
        JPanel constructPanel = new JPanel();
        constructPanel.setBackground(GUIColors.TEXT.toColor());
        constructPanel.setLayout(new GridLayout(6, 6));
        GridBagConstraints gbc = new GridBagConstraints();
        Icon[][] hexaList = new Icon[6][6];
        gamePanel.add(constructPanel, BorderLayout.CENTER);
        int last = 0;
        // will change to getting base colours from model
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                hexaList[i][j] = newHexa(null, false);
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

        // SIDE BAR - PIECES AVAILABLE
        // for this part need: getAvailableColors() and loop on it
        JPanel piecesPanel = new JPanel();
        piecesPanel.setBackground(GUIColors.TEXT_HOVER.toColor());
        piecesPanel.setLayout(new GridLayout(4, 2));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                JPanel mini = new JPanel();
                mini.setOpaque(false);
                JLabel numOfPieces = new JLabel("x3");
                numOfPieces.setFont(new Font("Jomhuria", Font.PLAIN, 20));
                if (i == 2) {
                    mini.add(newHexa(GUIColors.NATURAL_HEX, true));
                } else {
                    mini.add(newHexa(GUIColors.WHITE_HEX, true));
                }
                mini.add(numOfPieces);
                piecesPanel.add(mini);
            }
        }
        gamePanel.add(piecesPanel, BorderLayout.EAST);
        return gamePanel;
    }

    private JPanel createButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(4, 1));
        buttons.setPreferredSize(new Dimension(Config.getInitWidth() / 5, Config.getInitHeight() / 5));
        buttons.setBackground(GUIColors.GAME_BG.toColor());

        JButton optButton = new Buttons.GameFirstPhaseButton("Menu");
        optButton.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        optButton.setActionCommand("Menu");
        optButton.addActionListener(controller);
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
        validerButton.addActionListener(controller);
        buttons.add(validerButton);
        return buttons;
    }

    public static HexIcon newHexa(HSL c, boolean isActionable) {
        HexIcon hexa;
        if (c == null) {
            hexa = new HexIcon(ResourceLoader.getBufferedImage("wireHexa"), isActionable);
        } else {
            hexa = new HexIcon(ResourceLoader.getBufferedImage("hexaWhiteTextured"), isActionable);
            hexa.recolor(c);
        }
        hexa.resizeIcon(60, 60);
        return hexa;
    }
}
