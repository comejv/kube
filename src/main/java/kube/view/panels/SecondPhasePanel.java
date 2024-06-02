package kube.view.panels;

import java.awt.Color;
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
import javax.swing.JLabel;
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
import kube.view.animations.panelGlow;
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
    public JPanel gamePanel, p1Additionnals, p2Additionnals, p1, p2, base;
    private JButton undoButton, redoButton;
    private Dimension oldSize;

    private HexGlow animationHexGlow;
    private panelGlow animationPanelGlow;
    // TODO : set hex in middle of pyra not actionable

    public SecondPhasePanel(GUI gui, Kube k3, Phase2Controller controller) {
        this.gui = gui;
        this.k3 = k3;
        this.controller = controller;
        this.animationHexGlow = new HexGlow();
        this.animationPanelGlow = new panelGlow();
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

        JButton sugIaButton = new Buttons.GamePhaseButton("Suggestion IA");
        sugIaButton.addMouseListener(a);
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(sugIaButton, gbc);

        undoButton = new Buttons.GamePhaseButton("Annuler le coup");
        undoButton.setActionCommand("undo");
        undoButton.addMouseListener(a);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 6;
        panel.add(undoButton, gbc);

        redoButton = new Buttons.GamePhaseButton("Rejouer le coup");
        redoButton.setActionCommand("redo");
        redoButton.addMouseListener(a);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 7;
        panel.add(redoButton, gbc);

        JScrollPane histo = getHisto();
        histo.setMinimumSize(new Dimension(Config.INIT_WIDTH / 7, Config.INIT_HEIGHT));
        gbc.gridy = 3;
        gbc.gridheight = 3;
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
        for (Point p : player.getMountain().removable()) {
            HexIcon hex = (HexIcon) moutainPan[p.x][p.y].getComponent(0);
            hex.setActionable(true);
            hexToGlow.add(hex);
        }
        for (Component c : additionnals.getComponents()) {
            HexIcon hex = (HexIcon) c;
            if (hex.getColor() != ModelColor.EMPTY) {
                hex.setActionable(true);
                hexToGlow.add(hex);
            }
        }
        animationHexGlow.setToRedraw(hexToGlow);
    }

    public void updateVisible() {
        JPanel[][][] toUpdate = { k3Panels, p1Panels, p2Panels };
        for (JPanel[][] pan : toUpdate) {
            for (int i = 0; i < pan.length; i++) {
                for (int j = 0; j < i + 1; j++) {
                    HexIcon hex = (HexIcon) pan[i][j].getComponent(0);
                    if (hex.getColor() == ModelColor.EMPTY) {
                        hex.setVisible(false);
                    } else if (i > 0) {
                        //pan[i - 1][0].setPreferredSize(k3Panels[8][0].getSize());
                    }
                }
            }
        }
        for (Component c : p1Additionnals.getComponents()){
            HexIcon hex = (HexIcon) c;
            if (hex.getColor() == ModelColor.EMPTY){
                hex.setVisible(false);
            }
        }
        for (Component c : p2Additionnals.getComponents()){
            HexIcon hex = (HexIcon) c;
            if (hex.getColor() == ModelColor.EMPTY){
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

        int n = 0;
        for (ModelColor c : p.getAdditionals()) {
            if (n > 5) {
                gbc.gridy = 1;
            } else {
                gbc.gridy = 0;
            }
            additionnalsPanel.add(new HexIcon(c, false, p), gbc);
            n++;
        }
        if (addWire) {
            additionnalsPanel.add(new HexIcon(ModelColor.EMPTY, false, p), gbc);
        }
        additionnalsPanel.revalidate();
        additionnalsPanel.repaint();
    }

    public void update(Action a) {
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);
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
        updateActionnable();
        updateHisto();
        updateText();
        updateVisible();
        updatePanelGlow(false);
        if (k3.getHistory().canUndo() && !k3.getCurrentPlayer().isAI()) {
            undoButton.setEnabled(true);
        }
        if (k3.getHistory().canRedo() && !k3.getCurrentPlayer().isAI()) {
            redoButton.setEnabled(true);
        }
        if (a.getType() != ActionType.UNDO && k3.getPenality()) {
            penalityMessage();
        }
    }

    private void updateText() {
        if (k3.getPenality()) {
            gamePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                    k3.getCurrentPlayer().getName() + " volez une pièce à votre adversaire",
                    TitledBorder.CENTER, TitledBorder.TOP,
                    new Font("Jomhuria", Font.PLAIN, 60), GUIColors.ACCENT.toColor()));
        } else {
            gamePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                    k3.getCurrentPlayer().getName() + " jouez sur la montagne commune",
                    TitledBorder.CENTER, TitledBorder.TOP,
                    new Font("Jomhuria", Font.PLAIN, 60), GUIColors.ACCENT.toColor()));
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
        Player[] toUpdate = { null, k3.getP1(), k3.getP2() };
        for (Player p : toUpdate) {
            for (int i = 0; i < (p == null ? k3.getBaseSize() : p.getMountain().getBaseSize()); i++) {
                for (int j = 0; j < i + 1; j++) {
                    updateMoutain(p, i, j);
                }
            }
        }
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
                        HexIcon h = (HexIcon) k3Panels[0][0].getComponent(0);
                        h.setVisible(true);
                    }
                }
                updatePanelGlow(true);
                break;
            case DND_STOP:
                updateVisible();
                updatePanelGlow(false);
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
                        glowPan.put(p1Additionnals,  k3.getP1().getName());
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
                        glowPan.put(p1Additionnals,  k3.getP1().getName());
                    }
                    glowPan.put(p1, "");
                } else {
                    if (k3.getP2().getAdditionals().size() > 0) {
                        glowPan.put(p2Additionnals,  k3.getP2().getName());
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
        new Message(transparentPanel, "Pénalité à l'avantage du " + k3.getCurrentPlayer().getName(), gui,
                animationHexGlow);
    }

    public void winMessage(Action a) {
        Player winner = (Player) a.getData();
        TransparentPanel transparentPanel = new TransparentPanel("");
        transparentPanel.setPreferredSize(gui.getMainFrame().getSize());
        transparentPanel.setVisible(false);
        gui.addToOverlay(transparentPanel);
        new Message(transparentPanel, "Victoire du " + winner.getName(), gui, animationHexGlow);
    }

    public void updateHexSize() {
        Dimension newSize = this.getSize();
        if (isSignificantChange(oldSize, newSize)) {
            // Update the static size of HexIcon based on new size
            int newHexSize = calculateNewHexSize(newSize);
            Config.debug("Set hex size to ", newHexSize);
            HexIcon.setStaticSize(newHexSize);
            JPanel panel;
            HexIcon h;
            // Loop through panels and update hex size
            for (int i = 0; i < k3.getCurrentPlayer().getMountain().getBaseSize(); i++) {
                for (int j = 0; j < i + 1; j++) {
                    panel = p1Panels[i][j];
                    h = (HexIcon) panel.getComponents()[0];
                    h.updateSize();
                    panel.setPreferredSize(h.getSize());
                    panel = p2Panels[i][j];
                    h = (HexIcon) panel.getComponents()[0];
                    h.updateSize();
                    panel.setPreferredSize(h.getSize());
                }
            }
            for (int i = 0; i < k3Panels.length; i++) {
                for (int j = 0; j < i + 1; j++) {
                    panel = k3Panels[i][j];
                    h = (HexIcon) panel.getComponents()[0];
                    h.updateSize();
                    panel.setPreferredSize(h.getSize());
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

            // Update the old size to the new size
            oldSize = newSize;
            revalidate();
        }
    }

    private boolean isSignificantChange(Dimension oldSize, Dimension newSize) {
        int threshold = 100;
        return Math.abs(newSize.height - oldSize.height) > threshold;
    }

    private int calculateNewHexSize(Dimension newSize) {
        double scaleFactor = newSize.getHeight() / (double) Config.INIT_HEIGHT;
        int newHexSize = (int) (40 * scaleFactor);
        return newHexSize;
    }
}
