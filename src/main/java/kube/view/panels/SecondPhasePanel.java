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
    // TODO : refactor this class to make it more readable
    private Phase2Controller controller;
    private Kube k3;
    private GUI gui;
    private JTextPane editorPane;
    private JPanel[][] k3Panels;
    private JPanel[][] p1Panels;
    private JPanel[][] p2Panels;
    private JPanel[] k3invisibles;
    private JPanel leftWhiteDrop;
    private JPanel rightwhiteDrop;
    public JPanel gamePanel, p1Additionnals, p2Additionnals, p1, p2, base;
    private JButton undoButton, redoButton, pauseAi, sugAIButton, saveButton;
    private Dimension oldSize;

    public HexGlow animationHexGlow;
    public PanelGlow animationPanelGlow;
    // TODO : set hex in middle of pyra not actionable

    public SecondPhasePanel(GUI gui, Kube k3, Phase2Controller controller) {
        this.gui = gui;
        this.k3 = k3;
        this.controller = controller;
        this.animationHexGlow = new HexGlow();
        this.animationPanelGlow = new PanelGlow(k3);
        int k3BaseSize = k3.getK3().getBaseSize();
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

        oldSize = getSize();
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
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(sugAIButton, gbc);

        saveButton = new Buttons.GamePhaseButton("Sauvegarder");
        saveButton.setActionCommand("save");
        saveButton.addMouseListener(a);
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(saveButton, gbc);

        JButton loadButton = new Buttons.GamePhaseButton("Charger");
        loadButton.setActionCommand("load");
        loadButton.addMouseListener(a);
        loadButton.setEnabled(true);
        gbc.gridy = 4;
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
        gbc.gridy = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(histo, gbc);
        return panel;
    }

    public void updateActionnable() {
        // Reset all actionnable to false
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
        for (Component c : p1Additionnals.getComponents()) {
            HexIcon hex = (HexIcon) c;
            hex.setActionable(false);
        }
        for (Component c : p2Additionnals.getComponents()) {
            HexIcon hex = (HexIcon) c;
            hex.setActionable(false);
        }
        if (k3.getCurrentPlayer().isAI()) {
            return; // Do not set actionnable to true if it's the ai turn
        }

        JPanel[][] moutainPan = null;
        JPanel additionnals;
        Player player;
        if ((k3.getCurrentPlayer() == k3.getP1() && !k3.getPenality())
                || (k3.getCurrentPlayer() == k3.getP2() && k3.getPenality())) {
            player = k3.getP1();
            moutainPan = p1Panels;
            additionnals = p1Additionnals;
        } else {
            player = k3.getP2();
            moutainPan = p2Panels;
            additionnals = p2Additionnals;
        }
        ArrayList<HexIcon> hexToGlow = new ArrayList<>();
        ArrayList<ModelColor> playableColors = new ArrayList<>();
        for (ModelColor c : ModelColor.getAllColoredAndJokers()) {
            if (k3.getK3().compatible(c).size() > 0) {
                playableColors.add(c);
            }
        }
        for (Point p : player.getMountain().removable()) {
            HexIcon hex = (HexIcon) moutainPan[p.x][p.y].getComponent(0);
            if (hex.getColor() == ModelColor.WHITE || k3.getPenality() || playableColors.contains(hex.getColor())) {
                hex.setActionable(true);
                hexToGlow.add(hex);
            }
        }
        for (Component c : additionnals.getComponents()) {
            HexIcon hex = (HexIcon) c;
            if (hex.getColor() != ModelColor.EMPTY) {
                if (hex.getColor() == ModelColor.WHITE || k3.getPenality() || playableColors.contains(hex.getColor())) {
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
                    HexIcon hex = (HexIcon) k3invisibles[i - 1].getComponent(0);
                    hex.setVisible(atLeastOneNotEmpty);
                }
            }
        }
        for (Component c : p1Additionnals.getComponents()) {
            HexIcon hex = (HexIcon) c;
            if (hex.getColor() == ModelColor.EMPTY) {
                hex.setVisible(false);
            }
        }
        for (Component c : p2Additionnals.getComponents()) {
            HexIcon hex = (HexIcon) c;
            if (hex.getColor() == ModelColor.EMPTY) {
                hex.setVisible(false);
            }
        }
    }

    public void updateAdditionnals(Player p) {
        updateAdditionnals(p, false);
    }

    public void updateAdditionnals(Player p, boolean addWire) {
        JPanel additionnalsPanel = null;
        if (p == k3.getP1()) {
            additionnalsPanel = p1Additionnals;
        } else {
            additionnalsPanel = p2Additionnals;
        }
        additionnalsPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        HexIcon hex = new HexIcon(null, false, p);
        hex.setVisible(true);
        additionnalsPanel.add(hex, gbc);

        int n = 0;
        for (ModelColor c : p.getAdditionals()) {
            if (n > 5) {
                gbc.gridy = 1;
            }
            additionnalsPanel.add(new HexIcon(c, false, p), gbc);
            n++;
        }

        gbc.gridy = 0;
        hex = new HexIcon(null, false, p);
        hex.setVisible(true);
        additionnalsPanel.add(hex, gbc);

        if (addWire) {
            additionnalsPanel.add(new HexIcon(ModelColor.EMPTY, false, p), gbc);
        }
        additionnalsPanel.revalidate();
        additionnalsPanel.repaint();
    }

    public void update(Action a) {
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
                updateAdditionnals(k3.getP1());
                updateAdditionnals(k3.getP2());
                updateMoutain(k3.getP1(), new Point(0, 0));
                updateMoutain(k3.getP2(), new Point(0, 0));
            } else if (move instanceof MoveAM) {
                MoveAM am = (MoveAM) move;
                updateAdditionnals(am.getPlayer());
                updateMoutain(null, am.getTo());
            } else if (move instanceof MoveAW) {
                MoveAW aw = (MoveAW) move;
                updateAdditionnals(aw.getPlayer());
            } else if (move instanceof MoveMA) {
                MoveMA ma = (MoveMA) move;
                updateAdditionnals(ma.getPlayer());
                if (ma.getPlayer() == k3.getP1()) {
                    updateMoutain(k3.getP2(), ma.getFrom());
                } else {
                    updateMoutain(k3.getP1(), ma.getFrom());
                }
            } else if (move instanceof MoveMM) {
                MoveMM mm = (MoveMM) move;
                updateMoutain(mm.getPlayer(), mm.getFrom());
                updateMoutain(null, mm.getTo());
            } else if (move instanceof MoveMW) {
                MoveMW mw = (MoveMW) move;
                updateMoutain(mw.getPlayer(), mw.getFrom());
            }
        }
        updateActionnable();
        updateHisto();
        updateText();
        updateVisible();
        updatePanelGlow(false);
        if (k3.getHistory().canUndo()) {
            undoButton.setEnabled(true);
        }
        if (k3.getHistory().canRedo()) {
            redoButton.setEnabled(true);
        }
        if (a.getType() != ActionType.UNDO && a.getType() != ActionType.AI_PAUSE && k3.getPenality()) {
            penalityMessage();
        }
        if (!k3.getCurrentPlayer().isAI()) {
            sugAIButton.setEnabled(true);
        }
    }

    private void updateText() {
        if (k3.getPenality()) {
            gamePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                    k3.getCurrentPlayer().getName() + " : volez une pièce à votre adversaire",
                    TitledBorder.CENTER, TitledBorder.TOP,
                    new Font("Jomhuria", Font.PLAIN, 70), GUIColors.ACCENT.toColor()));
        } else {
            gamePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                    k3.getCurrentPlayer().getName() + " : jouez sur la montagne commune",
                    TitledBorder.CENTER, TitledBorder.TOP,
                    new Font("Jomhuria", Font.PLAIN, 70), GUIColors.ACCENT.toColor()));
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
        pauseAi.setText("Pause Kubot");
        pauseAi.setActionCommand("pauseAI");
        Player[] toUpdate = { null, k3.getP1(), k3.getP2() };
        for (Player p : toUpdate) {
            for (int i = 0; i < (p == null ? k3.getBaseSize() : p.getMountain().getBaseSize()); i++) {
                for (int j = 0; j < i + 1; j++) {
                    updateMoutain(p, i, j);
                }
            }
        }
        sugAIButton.setEnabled(!k3.getCurrentPlayer().isAI());
        updateHisto();
        updateAdditionnals(k3.getP1());
        updateAdditionnals(k3.getP2());
        updateActionnable();
        updateText();
        updateVisible();
        updatePanelGlow(false);
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);
    }

    public void updateMoutain(Player p, int i, int j) {
        updateMoutain(p, new Point(i, j));
    }

    public void updateMoutain(Player p, Point pos) {
        ModelColor c;
        JPanel panel;
        if (p == null) {
            c = k3.getK3().getCase(pos);
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

        p1Additionnals = new JPanel();
        p1Additionnals.setOpaque(false);
        p1Additionnals.setLayout(new GridBagLayout());
        p1Additionnals
                .setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Pieces additionnelles du Joueur 1 ", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Jomhuria", Font.PLAIN, 40), GUIColors.ACCENT.toColor()));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(p1Additionnals, gbc);

        p2Additionnals = new JPanel();
        p2Additionnals.setOpaque(false);
        p2Additionnals.setLayout(new GridBagLayout());
        p2Additionnals
                .setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Pieces additionnelles du Joueur 2 ", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Jomhuria", Font.PLAIN, 40), GUIColors.ACCENT.toColor()));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(p2Additionnals, gbc);

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

        base = initMountain(-1, k3.getK3().getBaseSize(), null);
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
            k3invisibles = new JPanel[9];
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
                k3invisibles[i - 1] = hexa;
                lineHexa.add(hexa);
                if (i == 8) {
                    rightwhiteDrop = new JPanel();
                    rightwhiteDrop.setOpaque(false);
                    HexIcon hex = new HexIcon(ModelColor.EMPTY, false, p);
                    rightwhiteDrop.add(hex);
                    rightwhiteDrop.setVisible(false);
                    lineHexa.add(rightwhiteDrop);
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
                } else if (k3.getPenality()) {
                    updateAdditionnals(k3.getCurrentPlayer(), true);
                } else {
                    if (hex.getColor() != ModelColor.WHITE) {
                        for (Point p : k3.getK3().compatible(hex.getColor())) {
                            HexIcon h = (HexIcon) k3Panels[p.x][p.y].getComponent(0);
                            h.setVisible(true);
                        }
                    } else {
                        if (k3.getCurrentPlayer() == k3.getP1()) {
                            leftWhiteDrop.setVisible(true);
                        } else {
                            rightwhiteDrop.setVisible(true);
                        }
                    }
                }
                break;
            case DND_STOP:
                updateVisible();
                updatePanelGlow(false);
                leftWhiteDrop.setVisible(false);
                rightwhiteDrop.setVisible(false);
                break;
            default:
                break;
        }

    }

    private void updatePanelGlow(boolean isDragging) {
        HashMap<JPanel, String> glowPan = new HashMap<>();
        if (k3.getPenality()) {
            if (isDragging) {
                if (k3.getCurrentPlayer() == k3.getP1()) {
                    glowPan.put(p1Additionnals, k3.getP1().getName());
                } else {
                    glowPan.put(p2Additionnals, k3.getP2().getName());
                }
            } else {
                if (k3.getCurrentPlayer() == k3.getP1()) {
                    if (k3.getP2().getAdditionals().size() > 0) {
                        glowPan.put(p2Additionnals, k3.getP2().getName());
                    }
                    glowPan.put(p2, "");
                } else {
                    if (k3.getP1().getAdditionals().size() > 0) {
                        glowPan.put(p1Additionnals, k3.getP1().getName());
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
                        glowPan.put(p1Additionnals, k3.getP1().getName());
                    }
                    glowPan.put(p1, "");
                } else {
                    if (k3.getP2().getAdditionals().size() > 0) {
                        glowPan.put(p2Additionnals, k3.getP2().getName());
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
        if (!glowPan.keySet().contains(p1Additionnals)) {
            LineBorder line = new LineBorder(GUIColors.GAME_BG_LIGHT.toColor(), 10);
            TitledBorder title = BorderFactory.createTitledBorder(line,
                    "Pieces additionnelles du " + k3.getP1().getName(), TitledBorder.CENTER, TitledBorder.TOP,
                    new Font("Jomhuria", Font.PLAIN, 40));
            p1Additionnals.setBorder(title);
            p1Additionnals.repaint();
        }

        if (!glowPan.keySet().contains(p2Additionnals)) {
            LineBorder line = new LineBorder(GUIColors.GAME_BG_LIGHT.toColor(), 10);
            TitledBorder title = BorderFactory.createTitledBorder(line,
                    "Pieces additionnelles du " + k3.getP2().getName(), TitledBorder.CENTER, TitledBorder.TOP,
                    new Font("Jomhuria", Font.PLAIN, 40));
            p2Additionnals.setBorder(title);
            p2Additionnals.repaint();
        }

        animationPanelGlow.setToRedraw(glowPan);
    }

    public void startMessage() {
        TransparentPanel transparentPanel = new TransparentPanel("");
        transparentPanel.setPreferredSize(gui.getMainFrame().getSize());
        transparentPanel.setVisible(false);
        gui.addToOverlay(transparentPanel);
        new Message(transparentPanel, "Le " + k3.getCurrentPlayer().getName() + " commence !", gui, animationHexGlow);
    }

    public void penalityMessage() {
        TransparentPanel transparentPanel = new TransparentPanel("", true);
        transparentPanel.setPreferredSize(gui.getMainFrame().getSize());
        transparentPanel.setVisible(false);
        gui.addToOverlay(transparentPanel);
        boolean aiAlreadyPaused = pauseAi.getText() == "Reprendre Kubot";
        new Message(transparentPanel, "Pénalité à l'avantage du " + k3.getCurrentPlayer().getName(), gui,
                animationHexGlow, false, aiAlreadyPaused);
    }

    public void winMessage(Action a) {
        Player winner = (Player) a.getData();
        winPanel panel = new winPanel();
        panel.setPreferredSize(gui.getMainFrame().getSize());
        panel.setVisible(false);
        gui.addToOverlay(panel);
        new winMsg(panel, gui, "Victoire du " + winner.getName(), controller);
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
        for (Component c : p1Additionnals.getComponents()) {
            h = (HexIcon) c;
            h.updateSize();
        }
        for (Component c : p2Additionnals.getComponents()) {
            h = (HexIcon) c;
            h.updateSize();
        }
        try {
            h = (HexIcon) leftWhiteDrop.getComponent(0);
            h.updateSize();
            leftWhiteDrop.removeAll();
            leftWhiteDrop.setBorder(null);

            leftWhiteDrop.add(h);

            h = (HexIcon) rightwhiteDrop.getComponent(0);
            h.updateSize();
            rightwhiteDrop.removeAll();
            rightwhiteDrop.setBorder(null);

            rightwhiteDrop.add(h);
        } catch (Exception e) {
            Config.debug("leftWhiteDrop or rightwhiteDrop doesn't exist");
        }
        // Update the old size to the new size
        oldSize = newSize;
        revalidate();
        repaint();
        gui.getOverlay().repaint();
    }

    private boolean isSignificantChange(Dimension oldSize, Dimension newSize) {
        int threshold = 100;
        return Math.abs(newSize.height - oldSize.height) > threshold;
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
