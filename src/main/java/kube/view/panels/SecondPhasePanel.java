package kube.view.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

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
    private Phase2Controller controller;
    private Kube k3;
    private GUI gui;
    private JTextPane editorPane;

    public SecondPhasePanel(GUI gui, Kube k3, Phase2Controller controller) {
        this.gui = gui;
        this.k3 = k3;
        this.controller = controller;
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

    private JPanel createEastPanel(ActionListener a) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
       // gbc.insets = new Insets(10, 0, 0, 0);


        JButton quitButton = new Buttons.GameFirstPhaseButton("Quitter la partie");
        quitButton.setActionCommand("quit");
        quitButton.addActionListener(a);
        gbc.gridy=0;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        panel.add(quitButton,gbc);

        JButton optButton = new Buttons.GameFirstPhaseButton("Paramètres");
        optButton.setActionCommand("settings");
        optButton.addActionListener(a);
        gbc.gridy=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        panel.add(optButton,gbc);

        JButton sugIaButton = new Buttons.GameFirstPhaseButton("Suggestion IA");
        sugIaButton.addActionListener(a);
        gbc.gridy=2;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        panel.add(sugIaButton,gbc);

        JButton histoButton = new Buttons.GameFirstPhaseButton("Historique");
        histoButton.setActionCommand("updateHist");
        gbc.fill=GridBagConstraints.HORIZONTAL;
        histoButton.addActionListener(a);
        gbc.gridy=6;
        panel.add(histoButton,gbc);

        JButton annulerButton = new Buttons.GameFirstPhaseButton("Annuler");
        annulerButton.setActionCommand("undo");
        annulerButton.addActionListener(a);
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridy=7;
        panel.add(annulerButton,gbc);

        JButton refaireButton = new Buttons.GameFirstPhaseButton("Refaire");
        refaireButton.setActionCommand("redo");
        refaireButton.addActionListener(a);
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.gridy=8;
        panel.add(refaireButton,gbc);

        JLabel histoText = new JLabel("HISTO");
        histoText.setFont(new Font("Jomhuria", Font.PLAIN, 25));
        histoText.setForeground(GUIColors.TEXT.toColor());
        histoText.setPreferredSize(new Dimension(Config.getInitWidth()/5,(int)(Config.getInitHeight()/2)));
        //panel.add(histoText);
        JScrollPane histo = getHisto();
        //histo.setMinimumSize(new Dimension(Config.getInitWidth()/7,Config.getInitHeight()));
        gbc.gridy=3;
        gbc.gridheight=3;
        gbc.weighty=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        panel.add(histo,gbc);
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

        for(int i =0 ; i < k3.getHistory().getUndone().size(); i++){
            htmlContent.append("<font color = 'gray'>").append(k3.getHistory().getUndone().get(i).toHTML()).append("</font><br>");
        }

        for (int i = k3.getHistory().getDone().size(); i>0; i--){
            htmlContent.append(k3.getHistory().getDone().get(i-1).toHTML()).append("<br>");
        }
        editorPane.setText(htmlContent.toString());
    }

    private JPanel gamePanel() {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel p1 = pyra(0,6,false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(p1, gbc);
        JPanel p2 = pyra(0,6,false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gamePanel.add(p2, gbc);
        JPanel base = pyra(-1,9,true);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        gamePanel.add(base, gbc);
        return gamePanel;
    }

    private JPanel pyra(int rowMissing,int base,boolean empty) {
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
                if (empty) {
                    lineHexa.add(new HexIcon(ModelColor.EMPTY, false));
                } else {
                    lineHexa.add(new HexIcon(ModelColor.BLUE, true));
                }
            }
            gbc.gridx = 0;
            gbc.gridy = i;
            // gbc.anchor = GridBagConstraints.CENTER;
            constructPanel.add(lineHexa, gbc);
        }
        return constructPanel;
    }
}
