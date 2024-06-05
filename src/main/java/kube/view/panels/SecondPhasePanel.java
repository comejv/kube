package kube.view.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import kube.configuration.Config;
import kube.controller.graphical.Phase2Controller;
import kube.model.Game;
import kube.model.Kube;
import kube.model.ModelColor;
import kube.model.Player;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.move.*;
import kube.view.GUI;
import kube.view.GUIColors;
import kube.view.animations.HexGlow;
import kube.view.animations.Message;
import kube.view.animations.PanelGlow;
import kube.view.components.Buttons;
import kube.view.components.HexIcon;

/*
 * This class extends JPanel and creates the GUI for the second phase of the game.
 */
public class SecondPhasePanel extends JPanel {

    private Phase2Controller controller;
    private Kube k3;
    private GUI gui;
    private JTextPane editorPane;
    private JPanel[][] k3Panels;
    private JPanel[][] p1Panels;
    private JPanel[][] p2Panels;
    private JPanel[] k3Invisibles;
    private JPanel leftWhiteDrop;
    private JPanel rightWhiteDrop;
    public JPanel gamePanel, p1Additionals, p2Additionals, p1, p2, base;
    private JButton undoButton, redoButton, pauseAi, sugAIButton, saveButton, loadButton;
    private int gameType;
    public HexGlow animationHexGlow;
    public PanelGlow animationPanelGlow;

    public SecondPhasePanel(GUI gui, Kube k3, Phase2Controller controller) {
        this.gui = gui;
        this.k3 = k3;
        this.controller = controller;
        this.animationHexGlow = new HexGlow();
        this.animationPanelGlow = new PanelGlow(k3);
        int k3BaseSize = k3.getMountain().getBaseSize();
        int playerBaseSize = k3.getP1().getMountain().getBaseSize();
        k3Panels = new JPanel[k3BaseSize][k3BaseSize];
        p1Panels = new JPanel[playerBaseSize][playerBaseSize];
        p2Panels = new JPanel[playerBaseSize][playerBaseSize];

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBackground(GUIColors.GAME_BG.toColor());
        // EAST
        JPanel eastPane = createEastPanel(controller);
        gbc.gridy = 0;
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 5, 20, 5);
        add(eastPane, gbc);
        gamePanel = gamePanel();
        gamePanel.setBackground(GUIColors.TEXT.toColor());
        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(20, 15, 20, 15);
        add(gamePanel, gbc);
    }

    public void resetPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // EAST
        JPanel eastPane = createEastPanel(controller);
        gbc.gridy = 0;
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 5, 20, 5);
        add(eastPane, gbc);
        gamePanel = gamePanel();
        gamePanel.setBackground(GUIColors.TEXT.toColor());
        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(20, 15, 20, 15);
        add(gamePanel, gbc);
        updateAll();
    }

    private JPanel createEastPanel(Phase2Controller a) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // gbc.insets = new Insets(10, 0, 0, 0);

        JButton quitButton = new Buttons.GamePhaseButton("Quitter la partie");
        quitButton.setActionCommand("quit");
        quitButton.addMouseListener(a);
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(quitButton, gbc);

        JButton optButton = new Buttons.GamePhaseButton("Paramètres");
        optButton.setActionCommand("settings");
        optButton.addMouseListener(a);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(optButton, gbc);

        sugAIButton = new Buttons.GamePhaseButton("Coup auto");
        sugAIButton.setActionCommand("auto");
        sugAIButton.addMouseListener(a);
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(sugAIButton, gbc);

        saveButton = new Buttons.GamePhaseButton("Sauvegarder");
        saveButton.setActionCommand("save");
        saveButton.addMouseListener(a);
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(saveButton, gbc);

        loadButton = new Buttons.GamePhaseButton("Charger");
        loadButton.setActionCommand("load");
        loadButton.addMouseListener(a);
        loadButton.setEnabled(true);
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(loadButton, gbc);

        pauseAi = new Buttons.GamePhaseButton("Pause Kubot");
        pauseAi.setVisible(false);
        pauseAi.setActionCommand("pauseAI");
        pauseAi.addMouseListener(a);
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(pauseAi, gbc);

        undoButton = new Buttons.GamePhaseButton("Annuler le coup");
        undoButton.setActionCommand("undo");
        undoButton.addMouseListener(a);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 7;
        panel.add(undoButton, gbc);

        redoButton = new Buttons.GamePhaseButton("Rejouer le coup");
        redoButton.setActionCommand("redo");
        redoButton.addMouseListener(a);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 8;
        panel.add(redoButton, gbc);

        JScrollPane histo = getHisto();
        histo.setMinimumSize(new Dimension(Config.INIT_WIDTH / 7, Config.INIT_HEIGHT));
        gbc.gridy = 4;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(histo, gbc);
        return panel;
    }

    public void updateActionable() {
        // Reset all actionable to false
        for (int i = 0; i < k3Panels.length; i++) {
            for (int j = 0; j < i + 1; j++) {
                HexIcon hex = (HexIcon) k3Panels[i][j].getComponent(0);
                hex.setActionable(false);
            }
        }
        for (int i = 0; i < p1Panels.length; i++) {
            for (int j = 0; j < i + 1; j++) {
                HexIcon hex = (HexIcon) p1Panels[i][j].getComponent(0);
                hex.setActionable(false);
                hex = (HexIcon) p2Panels[i][j].getComponent(0);
                hex.setActionable(false);
            }
        }
        for (Component c : p1Additionals.getComponents()) {
            HexIcon hex = (HexIcon) c;
            hex.setActionable(false);
        }
        for (Component c : p2Additionals.getComponents()) {
            HexIcon hex = (HexIcon) c;
            hex.setActionable(false);
        }
        if (k3.getCurrentPlayer().isAI() || (gameType != Game.LOCAL && k3.getCurrentPlayer().getId() != gameType)) {
            return; // Do not set actionable to true if it's the AI turn
        }

        JPanel[][] mountainPan = null;
        JPanel additionals;
        Player player;
        if ((k3.getCurrentPlayer() == k3.getP1() && !k3.getPenalty())
                || (k3.getCurrentPlayer() == k3.getP2() && k3.getPenalty())) {
            player = k3.getP1();
            mountainPan = p1Panels;
            additionals = p1Additionals;
        } else {
            player = k3.getP2();
            mountainPan = p2Panels;
            additionals = p2Additionals;
        }
        ArrayList<HexIcon> hexToGlow = new ArrayList<>();
        ArrayList<ModelColor> playableColors = new ArrayList<>();
        for (ModelColor c : ModelColor.getAllColoredAndJokers()) {
            if (k3.getMountain().compatible(c).size() > 0) {
                playableColors.add(c);
            }
        }
        for (Point p : player.getMountain().removable()) {
            HexIcon hex = (HexIcon) mountainPan[p.x][p.y].getComponent(0);
            if (hex.getColor() == ModelColor.WHITE || k3.getPenalty() || playableColors.contains(hex.getColor())) {
                hex.setActionable(true);
                hexToGlow.add(hex);
            }
        }
        for (Component c : additionals.getComponents()) {
            HexIcon hex = (HexIcon) c;
            if (hex.getColor() != ModelColor.EMPTY) {
                if (hex.getColor() == ModelColor.WHITE || k3.getPenalty() || playableColors.contains(hex.getColor())) {
                    hex.setActionable(true);
                    hexToGlow.add(hex);
                }
            }
        }
        animationHexGlow.setToRedraw(hexToGlow);
    }

    public void updateVisible() {
        if (k3.getP1().isAI() || k3.getP2().isAI()) {
            pauseAi.setVisible(true);
        }
        JPanel[][][] toUpdate = { k3Panels, p1Panels, p2Panels };
        Boolean atLeastOneNotEmpty;
        for (JPanel[][] pan : toUpdate) {
            for (int i = 0; i < pan.length; i++) {
                atLeastOneNotEmpty = false;
                for (int j = 0; j < i + 1; j++) {
                    HexIcon hex = (HexIcon) pan[i][j].getComponent(0);
                    if (hex.getColor() == ModelColor.EMPTY) {
                        hex.setVisible(false);
                    } else {
                        atLeastOneNotEmpty = true;
                    }
                }
                if (pan == k3Panels && i > 0) {
                    HexIcon hex = (HexIcon) k3Invisibles[i - 1].getComponent(0);
                    hex.setVisible(atLeastOneNotEmpty);
                }
            }
        }
        for (Component c : p1Additionals.getComponents()) {
            HexIcon hex = (HexIcon) c;
            if (hex.getColor() == ModelColor.EMPTY) {
                hex.setVisible(false);
            }
        }
        for (Component c : p2Additionals.getComponents()) {
            HexIcon hex = (HexIcon) c;
            if (hex.getColor() == ModelColor.EMPTY) {
                hex.setVisible(false);
            }
        }
    }

    public void updateAdditionals(Player p) {
        updateAdditionals(p, false);
    }

    public void updateAdditionals(Player p, boolean addWire) {
        JPanel additionalsPanel = null;
        if (p == k3.getP1()) {
            additionalsPanel = p1Additionals;
        } else {
            additionalsPanel = p2Additionals;
        }
        additionalsPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        HexIcon hex = new HexIcon(null, false, p);
        hex.setVisible(true);
        additionalsPanel.add(hex, gbc);

        int n = 0;
        for (ModelColor c : p.getAdditionals()) {
            if (n > 5) {
                gbc.gridy = 1;
            }
            additionalsPanel.add(new HexIcon(c, false, p), gbc);
            n++;
        }

        gbc.gridy = 0;
        hex = new HexIcon(null, false, p);
        hex.setVisible(true);
        additionalsPanel.add(hex, gbc);

        if (addWire) {
            additionalsPanel.add(new HexIcon(ModelColor.EMPTY, false, p), gbc);
        }
        additionalsPanel.revalidate();
        additionalsPanel.repaint();
    }

    public void update(Action a) {
        gameType = k3.getGameType();
        sugAIButton.setEnabled(false);
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);
        if (a.getType() == ActionType.AI_PAUSE) {
            Boolean pause = (Boolean) a.getData();
            pauseAi.setText(pause ? "Reprendre Kubot" : "Pause Kubot");
            pauseAi.setActionCommand(pause ? "stoppauseAI" : "pauseAI");
        } else {
            Move move = (Move) a.getData();
            if (move instanceof MoveAA) {
                updateAdditionals(k3.getP1());
                updateAdditionals(k3.getP2());
                updateMountain(k3.getP1(), new Point(0, 0));
                updateMountain(k3.getP2(), new Point(0, 0));
            } else if (move instanceof MoveAM) {
                MoveAM am = (MoveAM) move;
                updateAdditionals(am.getPlayer());
                updateMountain(null, am.getTo());
            } else if (move instanceof MoveAW) {
                MoveAW aw = (MoveAW) move;
                updateAdditionals(aw.getPlayer());
            } else if (move instanceof MoveMA) {
                MoveMA ma = (MoveMA) move;
                updateAdditionals(ma.getPlayer());
                if (ma.getPlayer() == k3.getP1()) {
                    updateMountain(k3.getP2(), ma.getFrom());
                } else {
                    updateMountain(k3.getP1(), ma.getFrom());
                }
            } else if (move instanceof MoveMM) {
                MoveMM mm = (MoveMM) move;
                updateMountain(mm.getPlayer(), mm.getFrom());
                updateMountain(null, mm.getTo());
            } else if (move instanceof MoveMW) {
                MoveMW mw = (MoveMW) move;
                updateMountain(mw.getPlayer(), mw.getFrom());
            }
        }
        updateActionable();
        updateHisto();
        updateText();
        updateVisible();
        updatePanelGlow(false);
        Config.debug(k3.getCurrentPlayer().getId(), k3.getGameType());
        if (k3.getHistory().canUndo()
                && (gameType == Game.LOCAL || k3.getCurrentPlayer().getId() != k3.getGameType())) {
            undoButton.setEnabled(true);
        }
        if (k3.getHistory().canRedo() && gameType == Game.LOCAL) {
            redoButton.setEnabled(true);
        }
        if (a.getType() != ActionType.UNDO && a.getType() != ActionType.AI_PAUSE && k3.getPenalty()) {
            penaltyMessage();
        }
        if (!k3.getCurrentPlayer().isAI()
                && (gameType == Game.LOCAL || k3.getCurrentPlayer().getId() == k3.getGameType())) {
            sugAIButton.setEnabled(true);
        }
    }

    private void updateText() {
        if (gameType == Game.LOCAL) {
            if (k3.getPenalty()) {
                gamePanel.setBorder(
                        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                                k3.getCurrentPlayer().getName() + " : volez une pièce à votre adversaire",
                                TitledBorder.CENTER, TitledBorder.TOP,
                                new Font("Jomhuria", Font.PLAIN, 70), GUIColors.ACCENT.toColor()));
            } else {
                gamePanel.setBorder(
                        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                                k3.getCurrentPlayer().getName() + " : jouez sur la montagne commune",
                                TitledBorder.CENTER, TitledBorder.TOP,
                                new Font("Jomhuria", Font.PLAIN, 70), GUIColors.ACCENT.toColor()));
            }
        } else if (gameType != k3.getCurrentPlayer().getId()) {
            if (k3.getPenalty()) {
                gamePanel.setBorder(
                        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                                "L'adversaire doit vous voler une pièce",
                                TitledBorder.CENTER, TitledBorder.TOP,
                                new Font("Jomhuria", Font.PLAIN, 70), GUIColors.ACCENT.toColor()));
            } else {
                gamePanel.setBorder(
                        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                                "L'adversaire doit jouer sur la montagne commune",
                                TitledBorder.CENTER, TitledBorder.TOP,
                                new Font("Jomhuria", Font.PLAIN, 70), GUIColors.ACCENT.toColor()));
            }
        } else {
            if (k3.getPenalty()) {
                gamePanel.setBorder(
                        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                                "A vous de voler une pièce à l'adversaire",
                                TitledBorder.CENTER, TitledBorder.TOP,
                                new Font("Jomhuria", Font.PLAIN, 70), GUIColors.ACCENT.toColor()));
            } else {
                gamePanel.setBorder(
                        BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                                "A vous de jouer sur la montagne commune",
                                TitledBorder.CENTER, TitledBorder.TOP,
                                new Font("Jomhuria", Font.PLAIN, 70), GUIColors.ACCENT.toColor()));
            }
        }

    }

    private JScrollPane getHisto() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                "Historique", TitledBorder.CENTER, TitledBorder.TOP));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorPane = new JTextPane(); // Assign the editorPane to the reference
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        scrollPane.setViewportView(editorPane);
        scrollPane.setPreferredSize(new Dimension(75, 300));
        scrollPane.setOpaque(false);

        return scrollPane;
    }

    public synchronized void updateHisto() {
        StringBuilder htmlContent = new StringBuilder();

        for (int i = 0; i < k3.getHistory().getUndone().size(); i++) {
            htmlContent.append("<font color = 'gray'>").append(k3.getHistory().getUndone().get(i).toHTML())
                    .append("</font><br>");
        }

        for (int i = k3.getHistory().getDone().size(); i > 0; i--) {
            htmlContent.append(k3.getHistory().getDone().get(i - 1).toHTML()).append("<br>");
        }
        editorPane.setText(htmlContent.toString());
        editorPane.repaint();
    }

    public void updateAll() {
        gameType = k3.getGameType();
        pauseAi.setText("Pause Kubot");
        pauseAi.setActionCommand("pauseAI");
        Player[] toUpdate = { null, k3.getP1(), k3.getP2() };
        for (Player p : toUpdate) {
            for (int i = 0; i < (p == null ? k3.getBaseSize() : p.getMountain().getBaseSize()); i++) {
                for (int j = 0; j < i + 1; j++) {
                    updateMountain(p, i, j);
                }
            }
        }
        loadButton.setEnabled(gameType == Game.LOCAL);
        if (!k3.getCurrentPlayer().isAI()
                && (gameType == Game.LOCAL || k3.getCurrentPlayer().getId() == k3.getGameType())) {
            sugAIButton.setEnabled(true);
        } else {
            sugAIButton.setEnabled(false);
        }
        updateHisto();
        updateAdditionals(k3.getP1());
        updateAdditionals(k3.getP2());
        updateActionable();
        updateText();
        updateVisible();
        updatePanelGlow(false);
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);
    }

    public void updateMountain(Player p, int i, int j) {
        updateMountain(p, new Point(i, j));
    }

    public void updateMountain(Player p, Point pos) {
        ModelColor c;
        JPanel panel;
        if (p == null) {
            c = k3.getMountain().getCase(pos);
            panel = k3Panels[pos.x][pos.y];
        } else if (p == k3.getP1()) {
            c = p.getMountain().getCase(pos);
            panel = p1Panels[pos.x][pos.y];
        } else {
            c = p.getMountain().getCase(pos);
            panel = p2Panels[pos.x][pos.y];
        }
        HexIcon hex = new HexIcon(c, true, p);
        hex.setPosition(pos);
        panel.removeAll();
        panel.add(hex);
        panel.revalidate();
        panel.repaint();
    }

    private JPanel gamePanel() {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        p1Additionals = new JPanel();
        p1Additionals.setOpaque(false);
        p1Additionals.setLayout(new GridBagLayout());
        p1Additionals
                .setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Pieces additionnelles du Joueur 1 ", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Jomhuria", Font.PLAIN, 40), GUIColors.ACCENT.toColor()));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(p1Additionals, gbc);

        p2Additionals = new JPanel();
        p2Additionals.setOpaque(false);
        p2Additionals.setLayout(new GridBagLayout());
        p2Additionals
                .setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Pieces additionnelles du Joueur 2 ", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Jomhuria", Font.PLAIN, 40), GUIColors.ACCENT.toColor()));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(p2Additionals, gbc);

        p1 = initMountain(0, k3.getP1().getMountain().getBaseSize(), k3.getP1());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(p1, gbc);

        p2 = initMountain(0, k3.getP2().getMountain().getBaseSize(), k3.getP2());
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(p2, gbc);

        base = initMountain(-1, k3.getMountain().getBaseSize(), null);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        base.setBorder(BorderFactory.createLineBorder(GUIColors.GAME_BG_LIGHT.toColor(), 5));

        gamePanel.add(base, gbc);

        return gamePanel;
    }

    private JPanel initMountain(int rowMissing, int base, Player p) {
        if (p == null) {
            k3Invisibles = new JPanel[9];
        }
        JPanel constructPanel = new JPanel();
        constructPanel.setOpaque(false);
        constructPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        int i;
        for (i = 1; i <= base; i++) {
            if (i <= rowMissing) {
                continue;
            }
            JPanel lineHexa = new JPanel();
            lineHexa.setLayout(new GridLayout(1, i));
            lineHexa.setOpaque(false);
            if (p == null) {

                if (i == 8) {
                    leftWhiteDrop = new JPanel();
                    leftWhiteDrop.setOpaque(false);
                    HexIcon hex = new HexIcon(ModelColor.EMPTY, false, p);
                    leftWhiteDrop.add(hex);
                    leftWhiteDrop.setVisible(false);
                    lineHexa.add(leftWhiteDrop);
                }

                JPanel hexa = new JPanel();
                hexa.setOpaque(false);
                HexIcon hex = new HexIcon(null, false, p);
                hex.setVisible(false);
                hexa.add(hex);
                lineHexa.add(hexa);
            }
            for (int j = 0; j < i; j++) {
                JPanel hexa = new JPanel();
                hexa.setOpaque(false);
                hexa.add(new HexIcon(ModelColor.EMPTY, false, p));
                if (p == null) {
                    k3Panels[i - 1][j] = hexa;
                } else if (p == k3.getP1()) {
                    p1Panels[i - 1][j] = hexa;
                } else {
                    p2Panels[i - 1][j] = hexa;
                }
                lineHexa.add(hexa);
            }
            if (p == null) {
                JPanel hexa = new JPanel();
                hexa.setOpaque(false);
                hexa.add(new HexIcon(null, false, p));
                k3Invisibles[i - 1] = hexa;
                lineHexa.add(hexa);
                if (i == 8) {
                    rightWhiteDrop = new JPanel();
                    rightWhiteDrop.setOpaque(false);
                    HexIcon hex = new HexIcon(ModelColor.EMPTY, false, p);
                    rightWhiteDrop.add(hex);
                    rightWhiteDrop.setVisible(false);
                    lineHexa.add(rightWhiteDrop);
                }

            }
            gbc.gridx = 0;
            gbc.gridy = i;
            // gbc.anchor = GridBagConstraints.CENTER;
            constructPanel.add(lineHexa, gbc);
        }
        return constructPanel;
    }

    public void updateDnd(Action a) {
        switch (a.getType()) {
            case DND_START:
                HexIcon hex = (HexIcon) a.getData();
                updatePanelGlow(true);
                if (hex == null) {
                    updateVisible();
                } else if (k3.getPenalty()) {
                    updateAdditionals(k3.getCurrentPlayer(), true);
                } else {
                    if (hex.getColor() != ModelColor.WHITE) {
                        for (Point p : k3.getMountain().compatible(hex.getColor())) {
                            HexIcon h = (HexIcon) k3Panels[p.x][p.y].getComponent(0);
                            h.setVisible(true);
                        }
                    } else {
                        if (k3.getCurrentPlayer() == k3.getP1()) {
                            leftWhiteDrop.setVisible(true);
                        } else {
                            rightWhiteDrop.setVisible(true);
                        }
                    }
                }
                break;
            case DND_STOP:
                updateVisible();
                updatePanelGlow(false);
                leftWhiteDrop.setVisible(false);
                rightWhiteDrop.setVisible(false);
                break;
            default:
                break;
        }

    }

    private void updatePanelGlow(boolean isDragging) {
        HashMap<JPanel, String> glowPan = new HashMap<>();
        if (k3.getPenalty()) {
            if (isDragging) {
                if (k3.getCurrentPlayer() == k3.getP1()) {
                    glowPan.put(p1Additionals, k3.getP1().getName());
                } else {
                    glowPan.put(p2Additionals, k3.getP2().getName());
                }
            } else {
                if (k3.getCurrentPlayer() == k3.getP1()) {
                    if (k3.getP2().getAdditionals().size() > 0) {
                        glowPan.put(p2Additionals, k3.getP2().getName());
                    }
                    glowPan.put(p2, "");
                } else {
                    if (k3.getP1().getAdditionals().size() > 0) {
                        glowPan.put(p1Additionals, k3.getP1().getName());
                    }
                    glowPan.put(p1, "");
                }
            }
        } else {
            if (isDragging) {
                glowPan.put(base, "");
            } else {
                if (k3.getCurrentPlayer() == k3.getP1()) {
                    if (k3.getP1().getAdditionals().size() > 0) {
                        glowPan.put(p1Additionals, k3.getP1().getName());
                    }
                    glowPan.put(p1, "");
                } else {
                    if (k3.getP2().getAdditionals().size() > 0) {
                        glowPan.put(p2Additionals, k3.getP2().getName());
                    }
                    glowPan.put(p2, "");
                }
            }
        }
        JPanel[] pans = new JPanel[] { p1, p2, base };
        for (JPanel pan : pans) {
            if (!glowPan.keySet().contains(pan)) {
                pan.setBorder(BorderFactory.createLineBorder(GUIColors.GAME_BG_LIGHT.toColor(), 10));
                pan.repaint();
            }
        }
        if (!glowPan.keySet().contains(p1Additionals)) {
            LineBorder line = new LineBorder(GUIColors.GAME_BG_LIGHT.toColor(), 10);
            TitledBorder title = BorderFactory.createTitledBorder(line,
                    "Pieces additionnelles du " + k3.getP1().getName(), TitledBorder.CENTER, TitledBorder.TOP,
                    new Font("Jomhuria", Font.PLAIN, 40), GUIColors.ACCENT.toColor());
            p1Additionals.setBorder(title);
            p1Additionals.repaint();
        }

        if (!glowPan.keySet().contains(p2Additionals)) {
            LineBorder line = new LineBorder(GUIColors.GAME_BG_LIGHT.toColor(), 10);
            TitledBorder title = BorderFactory.createTitledBorder(line,
                    "Pieces additionnelles du " + k3.getP2().getName(), TitledBorder.CENTER, TitledBorder.TOP,
                    new Font("Jomhuria", Font.PLAIN, 40), GUIColors.ACCENT.toColor());
            p2Additionals.setBorder(title);
            p2Additionals.repaint();
        }

        if (!glowPan.keySet().contains(p1)) {
            p1.repaint();
        }

        if (!glowPan.keySet().contains(p2)) {
            p2.repaint();
        }

        if (!glowPan.keySet().contains(base)) {
            base.repaint();
        }

        animationPanelGlow.setToRedraw(glowPan);
    }

    public void startMessage() {
        TransparentPanel transparentPanel = new TransparentPanel("");
        transparentPanel.setPreferredSize(gui.getMainFrame().getSize());
        transparentPanel.setVisible(false);
        gui.addToOverlay(transparentPanel);
        if (k3.getGameType() == Game.LOCAL) {
            new Message(transparentPanel, "Le " + k3.getCurrentPlayer().getName() + " commence !", gui,
                    animationHexGlow);
        } else if (k3.getCurrentPlayer().getId() == k3.getGameType()) {
            new Message(transparentPanel, "A vous de commencer !", gui,
                    animationHexGlow);
        } else {
            new Message(transparentPanel, "A l'adversaire de commencer !", gui,
                    animationHexGlow);
        }
    }

    public void penaltyMessage() {
        TransparentPanel transparentPanel = new TransparentPanel("", true);
        transparentPanel.setPreferredSize(gui.getMainFrame().getSize());
        transparentPanel.setVisible(false);
        gui.addToOverlay(transparentPanel);
        boolean aiAlreadyPaused = pauseAi.getText() == "Reprendre Kubot";
        if (k3.getGameType() == Game.LOCAL) {
            new Message(transparentPanel, "Pénalité à l'avantage du " + k3.getCurrentPlayer().getName(), gui,
                    animationHexGlow, false, aiAlreadyPaused);
        } else if (k3.getCurrentPlayer().getId() == k3.getGameType()) {
            new Message(transparentPanel, "Pénalité à votre avantage", gui,
                    animationHexGlow, false, aiAlreadyPaused);
        } else {
            new Message(transparentPanel, "Pénalité à l'avantage de l'adversaire", gui,
                    animationHexGlow, false, aiAlreadyPaused);
        }

    }

    public void winMessage(Action a) {
        Player winner = (Player) a.getData();
        winPanel panel = new winPanel();
        panel.setPreferredSize(gui.getMainFrame().getSize());
        panel.setVisible(false);
        gui.addToOverlay(panel);
        if (k3.getGameType() == Game.LOCAL) {
            new winMsg(panel, gui, "Victoire du " + winner.getName(), controller);
        } else if (winner.getId() == k3.getGameType()) {
            new winMsg(panel, gui, "Vous avez gagné !", controller);
        } else {
            new winMsg(panel, gui, "Vous avez perdu !", controller);
        }
        revalidate();
        repaint();
    }

    public void updateHexSize() {
        Dimension newSize = this.getSize();
        // if (isSignificantChange(oldSize, newSize)) {
        // Update the static size of HexIcon based on new size
        int newHexSize = calculateNewHexSize(newSize);
        HexIcon.setStaticSize(newHexSize);
        JPanel panel;
        HexIcon h;
        // Loop through panels and update hex size
        for (int i = 0; i < k3.getCurrentPlayer().getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                panel = p1Panels[i][j];
                h = (HexIcon) panel.getComponent(0);
                h.updateSize();
                panel.removeAll();
                panel.add(h);
                panel = p2Panels[i][j];
                h = (HexIcon) panel.getComponent(0);
                h.updateSize();
                panel.removeAll();
                panel.add(h);
            }
        }
        for (int i = 0; i < k3Panels.length; i++) {
            for (int j = 0; j < i + 1; j++) {
                panel = k3Panels[i][j];
                h = (HexIcon) panel.getComponent(0);
                h.updateSize();
                panel.removeAll();
                panel.add(h);
            }
        }
        for (Component c : p1Additionals.getComponents()) {
            h = (HexIcon) c;
            h.updateSize();
        }
        for (Component c : p2Additionals.getComponents()) {
            h = (HexIcon) c;
            h.updateSize();
        }
        try {
            h = (HexIcon) leftWhiteDrop.getComponent(0);
            h.updateSize();
            leftWhiteDrop.removeAll();
            leftWhiteDrop.setBorder(null);

            leftWhiteDrop.add(h);

            h = (HexIcon) rightWhiteDrop.getComponent(0);
            h.updateSize();
            rightWhiteDrop.removeAll();
            rightWhiteDrop.setBorder(null);

            rightWhiteDrop.add(h);
        } catch (Exception e) {
            Config.debug("leftWhiteDrop or rightWhiteDrop doesn't exist");
        }
        // Update the old size to the new size
        revalidate();
        repaint();
        gui.getOverlay().repaint();
    }

    private int calculateNewHexSize(Dimension newSize) {
        double scaleFactor = newSize.getHeight() / (double) Config.INIT_HEIGHT;
        int newHexSize = (int) (40 * scaleFactor);
        if (newHexSize == 0) {
            Config.error("Tried resizing hexa to 0 width");
            return 20;
        }
        return Math.max(newHexSize, 20);
    }
}
