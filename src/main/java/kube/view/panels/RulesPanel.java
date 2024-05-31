package kube.view.panels;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import kube.view.GUI;
import kube.view.GUIColors;
import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.controller.graphical.MenuController;
import kube.view.components.Buttons.RulesButton;

public class RulesPanel extends JPanel {
    // TODO : refactor this class to make it more readables

    private GUI gui;
    private int width;
    private int height;
    private MenuController buttonListener;
    private JPanel cardPanel;
    private JPanel gridPanel;
    private RulePanel rulePanel;
    private static int totalRuleNb = 8;

    public RulesPanel(GUI gui, MenuController buttonListener) {

        this.gui = gui;
        this.buttonListener = buttonListener;
        width = Config.getInitWidth() / 2;
        height = Config.getInitHeight() / 2;

        gui.setGlassPanelVisible(true);

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(Config.getInitWidth(), Config.getInitHeight()));
        setBackground(new Color(0, 0, 0, 150));

        gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setSize(new Dimension(width, height));
        gridPanel.setBackground(GUIColors.ACCENT.toColor());
        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.anchor = GridBagConstraints.CENTER;
        add(gridPanel, elemGBC);

        JLabel ruleTitle = new JLabel("RULES", SwingConstants.CENTER);
        ruleTitle.setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 6)));
        ruleTitle.setForeground(GUIColors.TEXT.toColor());
        elemGBC = new GridBagConstraints();
        elemGBC.gridx = 0;
        elemGBC.gridy = 0;
        elemGBC.anchor = GridBagConstraints.CENTER;
        elemGBC.weighty = 0;
        elemGBC.weightx = .5;
        gridPanel.add(ruleTitle, elemGBC);

        cardPanel = new JPanel(new CardLayout());
        cardPanel.setPreferredSize(new Dimension(width, height));
        elemGBC = new GridBagConstraints();
        elemGBC.gridx = 0;
        elemGBC.gridy = 1;
        elemGBC.anchor = GridBagConstraints.CENTER;
        elemGBC.weighty = .67;
        elemGBC.weightx = .5;
        gridPanel.add(cardPanel, elemGBC);

        rulePanel = new RulePanel();
        cardPanel.add(rulePanel, "rule");

        setVisible(true);
    }

    public void nextRule() {
        rulePanel.setRuleNb(rulePanel.getRuleNb() % totalRuleNb + 1);
        rulePanel.ruleToShow();
        rulePanel.revalidate();
        rulePanel.repaint();
    }

    private class RulePanel extends JPanel {
        private int ruleNb = 1;
        private Color background;
        private Color foreground;
        private JTextArea textArea;

        private RulePanel() {
            setLayout(new GridBagLayout());
            background = GUIColors.ACCENT.toColor();
            foreground = GUIColors.TEXT.toColor();
            setBackground(background);
            ruleToShow();
        }

        private void addTextArea(String name) {
            textArea = new JTextArea(ResourceLoader.getText(name));
            textArea.setEditable(false);
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 14)));
            textArea.setBackground(background);
            textArea.setForeground(foreground);
            textArea.setOpaque(false);
            textArea.setBorder(null);

            GridBagConstraints elemGBC = new GridBagConstraints();
            elemGBC.gridx = 0;
            elemGBC.gridy = 1;
            elemGBC.anchor = GridBagConstraints.CENTER;
            elemGBC.fill = GridBagConstraints.BOTH;
            elemGBC.weighty = .5;
            elemGBC.weightx = .5;
            elemGBC.insets = new Insets(0, 30, 0, 30);
            add(textArea, elemGBC);
        }

        private void addNextButton() {
            JButton suivant = new RulesButton("Suivant");
            GridBagConstraints elemGBC = new GridBagConstraints();
            elemGBC.gridx = 0;
            elemGBC.gridy = 2;
            elemGBC.anchor = GridBagConstraints.LAST_LINE_END;
            elemGBC.weighty = .5;
            elemGBC.weightx = .5;
            elemGBC.insets = new Insets(0, 0, 20, 20);
            suivant.addActionListener(buttonListener);
            if (getRuleNb() == totalRuleNb) {
                suivant.setText("Terminer");
                suivant.setActionCommand("endRule");
            } else {
                suivant.setActionCommand("nextRule");
            }
            add(suivant, elemGBC);
        }

        private void ruleToShow() {
            removeAll();
            addTextArea("rule" + getRuleNb());
            addNextButton();
        }

        public int getRuleNb() {
            return ruleNb;
        }

        public void setRuleNb(int i) {
            if (i > 0 && i < totalRuleNb + 1) {
                ruleNb = i;
            } else {
                System.err.println("Can't assign invalid rule number. Valid range of rule number is 1 to " + totalRuleNb
                        + " included");
            }
        }
    }
}
