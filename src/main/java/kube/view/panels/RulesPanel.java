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

    private GUI gui;
    private int width;
    private int height;
    private MenuController buttonListener;
    private JPanel cardPanel;
    private RulePanel rulePanel;
    private static int totalRuleNb = 8;

    public RulesPanel(GUI gui, MenuController buttonListener) {

        this.gui = gui;
        this.buttonListener = buttonListener;
        width = Math.round(Config.getWidth() / 1.75f);
        height = Math.round(Config.getHeight() / 1.5f);

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        setBackground(GUIColors.ACCENT.toColor());

        JLabel ruleTitle = new JLabel("RULES", SwingConstants.CENTER);
        ruleTitle.setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getHeight() / 6)));
        ruleTitle.setForeground(GUIColors.TEXT.toColor());
        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.gridx = 0;
        elemGBC.gridy = 0;
        elemGBC.anchor = GridBagConstraints.CENTER;
        elemGBC.weightx = 0.5;
        elemGBC.weighty = 0.1;
        add(ruleTitle, elemGBC);

        cardPanel = new JPanel(new CardLayout());
        cardPanel.setPreferredSize(new Dimension(width, height));
        elemGBC = new GridBagConstraints();
        elemGBC.gridx = 0;
        elemGBC.gridy = 1;
        elemGBC.anchor = GridBagConstraints.CENTER;
        elemGBC.weighty = 0.9;
        elemGBC.fill = GridBagConstraints.BOTH;
        add(cardPanel, elemGBC);

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
            textArea.setFont(new Font("Jomhuria", Font.PLAIN, (int) (Config.getHeight() / 14)));
            textArea.setBackground(background);
            textArea.setForeground(foreground);
            textArea.setOpaque(false);
            textArea.setBorder(null);

            GridBagConstraints elemGBC = new GridBagConstraints();
            elemGBC.gridx = 0;
            elemGBC.gridy = 1;
            elemGBC.anchor = GridBagConstraints.CENTER;
            elemGBC.fill = GridBagConstraints.BOTH;
            elemGBC.insets = new Insets(0, 30, 0, 30);
            add(textArea, elemGBC);
        }

        private void addNextButton() {
            JButton suivant = new RulesButton("Suivant");
            GridBagConstraints elemGBC = new GridBagConstraints();
            elemGBC.gridx = 0;
            elemGBC.gridy = 2;
            elemGBC.anchor = GridBagConstraints.LAST_LINE_END;
            elemGBC.weightx = .5;
            elemGBC.weighty = .5;
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
