package kube.view.panels;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.controller.graphical.DnDController;
import kube.controller.graphical.Phase1Controller;
import kube.model.Kube;
import kube.model.ModelColor;
import kube.model.action.Queue;
import kube.model.action.Action;
import kube.view.GUI;
import kube.view.GUIColors;
import kube.view.components.Buttons;
import kube.view.components.HexIcon;

import java.awt.*;

import javax.swing.*;

/*
 * This class extends JPanel and creates the GUI for the first phase of the game.
 */
public class FirstPhasePanel extends JPanel {
    private Kube k3;
    private Phase1Controller controller;
    private GUI gui;
    private JPanel constructPanel;

    public FirstPhasePanel(GUI gui, Kube k3, Phase1Controller controller, Queue<Action> eventsToView, Queue<Action> eventsToModel) {
        this.gui = gui;
        this.k3 = k3;
        this.controller = controller;
        setLayout(new GridBagLayout());
        setBackground(GUIColors.GAME_BG.toColor());
        gui.createGlassPane();
        gui.setGlassPaneController(new DnDController(eventsToView, eventsToModel));

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

        topPanel.add(newHexa(ModelColor.YELLOW, false));
        topPanel.add(newHexa(ModelColor.BLACK, false));
        topPanel.add(newHexa(ModelColor.BLUE, false));
        topPanel.add(newHexa(ModelColor.RED, false));
        topPanel.add(newHexa(ModelColor.GREEN, false));
        topPanel.add(newHexa(ModelColor.RED, false));
        topPanel.add(newHexa(ModelColor.BLUE, false));
        topPanel.add(newHexa(ModelColor.BLUE, false));
        topPanel.add(newHexa(ModelColor.YELLOW, false));

        gamePanel.add(topPanel, BorderLayout.NORTH);

        // CENTER - CONSTRUCTION OF PLAYER MOUNTAIN
        constructPanel = new JPanel();
        constructPanel.setOpaque(false);
        constructPanel.setLayout(new GridBagLayout());
        constructPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        gamePanel.add(constructPanel);
        updateGrid();
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
                    mini.add(newHexa(ModelColor.NATURAL, true));
                } else {
                    mini.add(newHexa(ModelColor.WHITE, true));
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

    public void updateGrid(){
        Config.debug("Start recolor");
        constructPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();

        for (int i = 1; i <= 6; i++) {
            JPanel lineHexa = new JPanel();
            lineHexa.setLayout(new GridLayout(1, i));
            lineHexa.setOpaque(false);
            lineHexa.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            for (int j = 0; j < i; j++) {
                HexIcon hex = newHexa(k3.getPlayerCase(k3.getCurrentPlayer(), i-1, j), false);
                hex.setPosition(new Point(i-1, j));
                lineHexa.add(hex);
            }
            gbc.gridx = 0;
            gbc.gridy = i;
            // gbc.anchor = GridBagConstraints.CENTER;
            constructPanel.add(lineHexa, gbc);
        }
        Config.debug("End recolor");

    }

    public static HexIcon newHexa(ModelColor c, boolean isActionable) {
        HexIcon hexa;
        if (c == null) {
            hexa = new HexIcon(ResourceLoader.getBufferedImage("wireHexa"), isActionable);
        } else {
            hexa = new HexIcon(ResourceLoader.getBufferedImage("hexaWhiteTextured"), isActionable, c);
            hexa.recolor(c);
        }
        hexa.resizeIcon(80, 80);
        return hexa;
    }
}
