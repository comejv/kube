package kube.view.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import kube.configuration.Config;
import kube.controller.graphical.Phase2Controller;
import kube.model.Kube;
import kube.model.ModelColor;
import kube.model.Mountain;
import kube.model.Player;
import kube.model.action.Action;
import kube.model.action.move.Move;
import kube.view.GUI;
import kube.view.GUIColors;
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
    private HashMap<String, JButton> buttonsMap;
    // TODO : set hex in middle of pyra not actionable

    public SecondPhasePanel(GUI gui, Kube k3, Phase2Controller controller) {
        this.gui = gui;
        this.k3 = k3;
        this.controller = controller;
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
        gbc.insets = new Insets(20, 15, 20, 15);
        add(gamePanel, gbc);
    }

    private JPanel createEastPanel(Phase2Controller a) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // gbc.insets = new Insets(10, 0, 0, 0);

        JButton quitButton = new Buttons.GameFirstPhaseButton("Quitter la partie");
        quitButton.setActionCommand("quit");
        quitButton.addMouseListener(a);
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(quitButton, gbc);

        JButton optButton = new Buttons.GameFirstPhaseButton("Paramètres");
        optButton.setActionCommand("settings");
        optButton.addMouseListener(a);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(optButton, gbc);

        JButton sugIaButton = new Buttons.GameFirstPhaseButton("Suggestion IA");
        sugIaButton.addMouseListener(a);
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(sugIaButton, gbc);

        JButton histoButton = new Buttons.GameFirstPhaseButton("Historique");
        histoButton.setActionCommand("updateHist");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        histoButton.addMouseListener(a);
        gbc.gridy = 6;
        panel.add(histoButton, gbc);

        JButton annulerButton = new Buttons.GameFirstPhaseButton("Annuler");
        annulerButton.setActionCommand("undo");
        annulerButton.addMouseListener(a);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 7;
        panel.add(annulerButton, gbc);

        JButton refaireButton = new Buttons.GameFirstPhaseButton("Refaire");
        refaireButton.setActionCommand("redo");
        refaireButton.addMouseListener(a);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 8;
        panel.add(refaireButton, gbc);

        JLabel histoText = new JLabel("HISTO");
        histoText.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        histoText.setForeground(GUIColors.TEXT.toColor());
        histoText.setPreferredSize(new Dimension(Config.getInitWidth() / 5, (int) (Config.getInitHeight() / 2)));
        // panel.add(histoText);
        JScrollPane histo = getHisto();
        // histo.setMinimumSize(new
        // Dimension(Config.getInitWidth()/7,Config.getInitHeight()));
        gbc.gridy = 3;
        gbc.gridheight = 3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(histo, gbc);
        return panel;
    }

    public void update(Action a) {
        // TODO
        updateHisto();
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

        JPanel additionals1 = new JPanel();
        additionals1.setOpaque(false);
        additionals1.setLayout(new GridBagLayout());
        additionals1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                "Additionnel Pieces de Joueur 1 ", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Jomhuria", Font.PLAIN, 40), GUIColors.ACCENT.toColor()));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(additionals1, gbc);

        JPanel additionals2 = new JPanel();
        additionals2.setOpaque(false);
        additionals2.setLayout(new GridBagLayout());
        additionals2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                "Additionnel Pieces de Joueur 2 ", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Jomhuria", Font.PLAIN, 40), GUIColors.ACCENT.toColor()));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(additionals2, gbc);

        JPanel p1 = initMountain(0, k3.getP1().getMountain().getBaseSize(), k3.getP1());
        p1.setBorder(BorderFactory.createLineBorder(GUIColors.GAME_BG_LIGHT.toColor(), 5));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(p1, gbc);

        JPanel p2 = initMountain(0, k3.getP2().getMountain().getBaseSize(), k3.getP2());
        p2.setBorder(BorderFactory.createLineBorder(GUIColors.GAME_BG_LIGHT.toColor(), 5));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(p2, gbc);

        JPanel base = initMountain(-1, k3.getK3().getBaseSize(), null);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gamePanel.add(base, gbc);

        return gamePanel;
    }

    private void addAdditionals(JPanel panel, int n, ModelColor c) {
        for (int i = 0; i < n; i++) {
            panel.add(new HexIcon(c, true));
        }
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
}
