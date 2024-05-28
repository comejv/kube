package kube.view.panels;

import kube.configuration.Config;
import kube.controller.graphical.Phase1Controller;
import kube.model.Kube;
import kube.model.ModelColor;
import kube.model.action.Queue;
import kube.model.action.Remove;
import kube.model.action.Swap;
import kube.model.action.Action;
import kube.model.action.Build;
import kube.view.GUI;
import kube.view.GUIColors;
import kube.view.components.Buttons;
import kube.view.components.HexIcon;

import java.util.HashMap;
import java.awt.*;

import javax.swing.*;

/*
 * This class extends JPanel and creates the GUI for the first phase of the game.
 */
public class FirstPhasePanel extends JPanel {
    private Kube k3;
    private Phase1Controller controller;
    private GUI gui;
    private JPanel constructPanel, piecesPanel, gamePanel;
    private HashMap<ModelColor, JPanel> sidePanels;
    private JPanel[][] moutainPanels;

    public FirstPhasePanel(GUI gui, Kube k3, Phase1Controller controller, Queue<Action> eventsToView,
            Queue<Action> eventsToModel) {
        this.gui = gui;
        this.k3 = k3;
        this.controller = controller;
        setLayout(new GridBagLayout());
        setBackground(GUIColors.GAME_BG.toColor());

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
        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());

        // TOP BAR - GAME BASE
        JPanel topPanel = new JPanel();
        topPanel.setBackground(GUIColors.GAME_BG_DARK.toColor());
        JLabel baseLabel = new JLabel("Base Centrale: ");
        baseLabel.setFont(new Font("Jomhuria", Font.PLAIN, 30));
        baseLabel.setForeground(GUIColors.TEXT.toColor());
        topPanel.add(baseLabel);
        for (int i = 0; i < k3.getK3().getBaseSize(); i++) {
            topPanel.add(new HexIcon(k3.getK3().getCase(k3.getK3().getBaseSize() - 1, i), false));
        }

        gamePanel.add(topPanel, BorderLayout.NORTH);

        // CENTER - CONSTRUCTION OF PLAYER MOUNTAIN
        initGrid();
        gamePanel.add(constructPanel);
        // SIDE BAR - PIECES AVAILABLE
        initSide();
        gamePanel.add(piecesPanel, BorderLayout.EAST);
        return gamePanel;
    }

    private JPanel createButtons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(4, 1));
        buttons.setPreferredSize(new Dimension(Config.getInitWidth() / 5, Config.getInitHeight() / 5));
        buttons.setBackground(GUIColors.GAME_BG.toColor());

        JButton quitButton = new Buttons.GameFirstPhaseButton("Quitter la partie");
        quitButton.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        quitButton.setActionCommand("quit");
        quitButton.addActionListener(controller);
        buttons.add(quitButton);

        JButton optButton = new Buttons.GameFirstPhaseButton("Paramètres");
        optButton.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        optButton.setActionCommand("settings");
        optButton.addActionListener(controller);
        buttons.add(optButton);

        JButton sugIaButton = new Buttons.GameFirstPhaseButton("Suggestion IA");
        sugIaButton.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        buttons.add(sugIaButton);

        JButton validerButton = new Buttons.GameFirstPhaseButton("Valider");
        validerButton.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        validerButton.setActionCommand("phase2");
        validerButton.addActionListener(controller);
        buttons.add(validerButton);
        return buttons;
    }

    public void initGrid() {
        moutainPanels = new JPanel[6][6];
        constructPanel = new JPanel();
        constructPanel.setOpaque(false);
        constructPanel.setLayout(new GridBagLayout());
        constructPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        GridBagConstraints gbc = new GridBagConstraints();

        for (int i = 1; i <= 6; i++) {
            JPanel lineHexa = new JPanel();
            lineHexa.setLayout(new GridLayout(1, i));
            lineHexa.setOpaque(false);
            lineHexa.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            for (int j = 0; j < i; j++) {
                JPanel hexPanel = new JPanel();
                HexIcon hex = new HexIcon(k3.getPlayerCase(k3.getCurrentPlayer(), i - 1, j), false, 2);
                hex.setPosition(new Point(i - 1, j));
                hexPanel.add(hex);
                lineHexa.add(hexPanel);
                moutainPanels[i - 1][j] = hexPanel;
            }
            gbc.gridx = 0;
            gbc.gridy = i;
            // gbc.anchor = GridBagConstraints.CENTER;
            constructPanel.add(lineHexa, gbc);
        }
        constructPanel.revalidate();
        constructPanel.repaint();
    }

    public void initSide() {
        sidePanels = new HashMap<>();
        piecesPanel = new JPanel();
        piecesPanel.setBackground(GUIColors.TEXT_HOVER.toColor());
        piecesPanel.setLayout(new GridLayout(4, 2));
        for (ModelColor c : ModelColor.getAllColoredAndJokers()) {
            JPanel mini = new JPanel();
            mini.setOpaque(false);
            int numberOfPieces = k3.getCurrentPlayer().getAvailableToBuild().get(c);
            JLabel numOfPieces = new JLabel("x" + numberOfPieces);
            numOfPieces.setFont(new Font("Jomhuria", Font.PLAIN, 40));
            boolean actionable = numberOfPieces > 0;
            mini.add(new HexIcon(c, actionable));
            mini.add(numOfPieces);
            piecesPanel.add(mini);
            sidePanels.put(c, mini);
        }
        piecesPanel.revalidate();
        piecesPanel.repaint();
    }

    public void updateGrid(Point pos) {
        Config.debug("Update the Point ", pos, "of the grid");
        ModelColor c = k3.getPlayerCase(k3.getCurrentPlayer(), pos.x, pos.y);
        boolean actionable = c != ModelColor.EMPTY;
        HexIcon hex = new HexIcon(c, actionable, 2);
        hex.setPosition(pos);
        JPanel panel = moutainPanels[pos.x][pos.y];
        panel.removeAll();
        panel.add(hex);
        panel.revalidate();
        panel.repaint();
        Config.debug("End update");

    }

    public void updateSide(ModelColor c) {
        Config.debug("Update the color ", c, "of the side");

        JPanel mini = sidePanels.get(c);
        mini.removeAll();
        int numberOfPieces = k3.getCurrentPlayer().getAvailableToBuild().get(c);
        JLabel numOfPieces = new JLabel("x" + numberOfPieces);
        numOfPieces.setFont(new Font("Jomhuria", Font.PLAIN, 40));
        mini.add(new HexIcon(c, numberOfPieces > 0));
        mini.add(numOfPieces);
        mini.revalidate();
        mini.repaint();
        Config.debug("End update");

    }

    public void update(Action a) {
        switch (a.getType()) {
            case BUILD:
                Build b = (Build) a.getData();
                updateGrid(b.getPos());
                updateSide(b.getModelColor());
                break;
            case REMOVE:
                Remove r = (Remove) a.getData();
                updateGrid(r.getPos());
                updateSide(r.getModelColor());
                break;
            case SWAP:
                Swap s = (Swap) a.getData();
                updateGrid(s.getPos1());
                updateGrid(s.getPos2());
                break;
            default:
                break;
        }

    }
}
