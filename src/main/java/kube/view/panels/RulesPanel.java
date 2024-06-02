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

    private int width;
    private int height;
    private MenuController buttonListener;
    private JPanel cardPanel;
    private JTextArea[] textAreas;
    private RulePanel[] rulePanels;
    private int currentRuleNb;
    private final int TOTAL_RULE_NB = 8;
    private final Color BACKGROUND = GUIColors.ACCENT.toColor();
    private final Color FOREGROUND = GUIColors.TEXT.toColor();

    public RulesPanel(GUI gui, MenuController buttonListener) {

        this.buttonListener = buttonListener;
        width = Config.INIT_WIDTH / 2;
        height = Config.INIT_HEIGHT / 2;

        width = Math.round(Config.INIT_WIDTH / 1.5f);
        height = Math.round(Config.INIT_HEIGHT / 1.25f);

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        setBackground(GUIColors.ACCENT.toColor());

        setCurrentRuleNb(1);
        
        JLabel ruleTitle = new JLabel("REGLES", SwingConstants.CENTER);
        ruleTitle.setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.INIT_HEIGHT / 6)));
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
        
        textAreas = new JTextArea[TOTAL_RULE_NB];
        rulePanels = new RulePanel[TOTAL_RULE_NB];
        loadAllPanels();

        setVisible(true);
    }
    
    private void loadAllTexts(){
        for (int i = 0; i < TOTAL_RULE_NB; i++) {
            Config.debug("load rule nb " + i);
            //rules are numeroted from 1 to 8, not 0 to 7 thus the +1
            JTextArea textArea = new JTextArea(ResourceLoader.getText("rule" + (i + 1)));
            textArea.setEditable(false);
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.INIT_HEIGHT / 14)));
            textArea.setBackground(BACKGROUND);
            textArea.setForeground(FOREGROUND);
            textArea.setOpaque(false);
            textArea.setBorder(null);
            textAreas[i] = textArea;
        }
    }

    private void loadAllPanels(){
        loadAllTexts();
        RulePanel rulePanel = new RulePanel();
        rulePanel.addTextArea(0);
        rulePanel.addNextButton(0);
        rulePanel.addPreviousButton(0);
        rulePanels[0] = rulePanel;
        cardPanel.add(rulePanels[0]);
        for (int i = 1; i < TOTAL_RULE_NB; i++) {
             rulePanel = new RulePanel();
            rulePanel.addTextArea(i);
            rulePanel.addNextButton(i);
            rulePanel.addPreviousButton(i);
            rulePanel.setVisible(false);
            rulePanels[i] = rulePanel;
            cardPanel.add(rulePanels[i]);
            rulePanel.setVisible(true);
        }
    }
    
    public void nextRule() {
        cardPanel.removeAll();
        setCurrentRuleNb(getCurrentRuleNb() + 1);
        cardPanel.add(rulePanels[getCurrentRuleNb()]);
        cardPanel.revalidate();
        cardPanel.repaint();
    }
    
    public void previousRule() {
        cardPanel.removeAll();
        setCurrentRuleNb(getCurrentRuleNb() - 1);
        cardPanel.add(rulePanels[getCurrentRuleNb()]);
        cardPanel.revalidate();
        cardPanel.repaint();
    }
    
    public int getCurrentRuleNb() {
        return currentRuleNb;
    }

    public void setCurrentRuleNb(int i) {
        currentRuleNb = i % TOTAL_RULE_NB;
    }

    private class RulePanel extends JPanel {
        
        private RulePanel() {
            setLayout(new GridBagLayout());
            setBackground(BACKGROUND);
        }

        private void addTextArea(int ruleNb) {
            GridBagConstraints elemGBC = new GridBagConstraints();
            elemGBC.gridx = 0;
            elemGBC.gridy = 1;
            elemGBC.anchor = GridBagConstraints.CENTER;
            elemGBC.fill = GridBagConstraints.BOTH;
            elemGBC.insets = new Insets(0, 30, 0, 30);
            //textAreas goes from 0 to 7, not 1 to 8
            add(textAreas[ruleNb], elemGBC);
        }

        private void addNextButton(int ruleNb) {
            JButton next = new RulesButton("Suivant");
            GridBagConstraints elemGBC = new GridBagConstraints();
            elemGBC.gridx = 0;
            elemGBC.gridy = 2;
            elemGBC.anchor = GridBagConstraints.LAST_LINE_END;
            elemGBC.weightx = .5;
            elemGBC.weighty = .5;
            elemGBC.insets = new Insets(0, 0, 20, 20);
            next.addActionListener(buttonListener);
            //ruleNb ranges from 0 to 7
            if (ruleNb == TOTAL_RULE_NB - 1) {
                next.setText("Terminer");
                next.setActionCommand("endRule");
            } else {
                next.setActionCommand("nextRule");
            }
            add(next, elemGBC);
        }

        private void addPreviousButton(int ruleNb) {
            if (ruleNb != 0) {
                JButton previous = new RulesButton("Précédent");
                GridBagConstraints elemGBC = new GridBagConstraints();
                elemGBC.gridx = 0;
                elemGBC.gridy = 2;
                elemGBC.anchor = GridBagConstraints.LAST_LINE_START;
                elemGBC.weightx = .5;
                elemGBC.weighty = .5;
                elemGBC.insets = new Insets(0, 20, 20, 0);
                previous.addActionListener(buttonListener);
                previous.setActionCommand("previousRule");
                add(previous, elemGBC);
            }
        }
    }
}
