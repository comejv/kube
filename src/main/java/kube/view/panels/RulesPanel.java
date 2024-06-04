package kube.view.panels;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import kube.view.GUI;
import kube.view.GUIColors;
import kube.view.animations.AnimatedRule;
import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.controller.graphical.MenuController;
import kube.model.ModelColor;
import kube.view.components.HexIcon;
import kube.view.components.Buttons.RulesButton;

public class RulesPanel extends JPanel {
    // TODO : refactor this class to make it more readables

    private GUI gui;
    private int width;
    private int height;
    private MenuController buttonListener;
    private JPanel cardPanel;
    private ArrayList<Timer> animatedRuleTimer;
    private JTextArea[] textAreas;
    private RulePanel[] rulePanels;
    private int currentRuleNb;
    private HashSet<Integer> rulesWithAnimNb = new HashSet<>();
    private final int TOTAL_RULE_NB = 8;
    private final Color BACKGROUND = GUIColors.ACCENT.toColor();
    private final Color FOREGROUND = GUIColors.TEXT.toColor();

    public RulesPanel(GUI gui, MenuController buttonListener) {

        this.buttonListener = buttonListener;
        this.gui = gui;

        width = Math.round(gui.getMainFrame().getWidth() / 1.5f);
        height = Math.round(gui.getMainFrame().getHeight());

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        setBackground(GUIColors.ACCENT.toColor());

        int[] rulesWithAnimation = {1, 2, 3};
        animatedRuleTimer = new ArrayList<>();
        setRulesWithAnimation(rulesWithAnimation);
        setCurrentRuleNb(0);
        
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
            rulePanel.addAnimation(i);
            rulePanel.addImage(i);
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

    public void setRulesWithAnimation(int[] rulesWithAnimation){
        for (int i : rulesWithAnimation) {
            rulesWithAnimNb.add(i);
        }
    }

    private class RulePanel extends JPanel {
        
        private RulePanel() {
            setLayout(new GridBagLayout());
            setBackground(BACKGROUND);
        }

        private void addTextArea(int ruleNb) {
            GridBagConstraints elemGBC = new GridBagConstraints();
            elemGBC.gridx = 0;
            elemGBC.gridy = 2;
            elemGBC.anchor = GridBagConstraints.CENTER;
            elemGBC.fill = GridBagConstraints.BOTH;
            elemGBC.insets = new Insets(0, 30, 0, 30);
            add(textAreas[ruleNb], elemGBC);
        }

        private void addAnimation(int ruleNb){
            if (rulesWithAnimNb.contains(ruleNb)) {
                GridBagConstraints elemGBC = new GridBagConstraints();
                elemGBC.gridx = 0;
                elemGBC.gridy = 1;
                elemGBC.anchor = GridBagConstraints.CENTER;
                elemGBC.fill = GridBagConstraints.BOTH;
                AnimationPanel animationPanel = new AnimationPanel(ruleNb, gui);
                animatedRuleTimer.add(animationPanel.getAnimation().getTimer());
                add(animationPanel);
            }
        }

        private void addImage(int ruleNb){
            if (ruleNb > 4) {
                int imgWidth = gui.getMainFrame().getWidth() / 10;
                int imgHeigth = gui.getMainFrame().getWidth() / 10;
                HexIcon nat = new HexIcon(ModelColor.NATURAL);
                nat.resizeIcon(imgWidth, imgHeigth);
                HexIcon white = new HexIcon(ModelColor.WHITE);
                white.resizeIcon(imgWidth, imgHeigth);
                GridBagConstraints elemGBC = new GridBagConstraints();
                elemGBC.gridx = 0;
                elemGBC.gridy = 1;
                elemGBC.anchor = GridBagConstraints.CENTER;
                elemGBC.fill = GridBagConstraints.BOTH;
                switch (ruleNb) {
                    case 5:
                        JPanel imagePanel = new JPanel(new GridBagLayout());
                        imagePanel.setBackground(new Color(0,0,0,0));
                        add(imagePanel, elemGBC);
                        elemGBC = new GridBagConstraints();
                        elemGBC.gridx = 0;
                        elemGBC.anchor = GridBagConstraints.CENTER;
                        imagePanel.add(white, elemGBC);
                        elemGBC = new GridBagConstraints();
                        elemGBC.gridx = 1;
                        elemGBC.anchor = GridBagConstraints.CENTER;
                        imagePanel.add(nat, elemGBC);
                        break;
                    case 6:
                        add(white, elemGBC);
                        break;
                    case 7:
                        add(nat, elemGBC);
                        break;
                    default:
                        break;
                }
            }
        }
        
        private void addNextButton(int ruleNb) {
            JButton next = new RulesButton("Suivant");
            GridBagConstraints elemGBC = new GridBagConstraints();
            elemGBC.gridx = 0;
            elemGBC.gridy = 3;
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
                elemGBC.gridy = 3;
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

    public ArrayList<Timer> getAnimatedRuleTimer() {
        return animatedRuleTimer;
    }

    public class AnimationPanel extends JPanel{

        private JLabel[] frames;
        private int updatedWidth;
        private int updatedHeight;
        private AnimatedRule animation;

        private AnimationPanel(int ruleNb, GUI gui){
            frames = new JLabel[4];
            updatedWidth = Math.round(gui.getMainFrame().getWidth() / 2.5f);
            updatedHeight = Math.round(gui.getMainFrame().getHeight() / 2.5f);
            for (int i = 0; i < 4; i++) {
                Image image = ResourceLoader.getBufferedImage("animations/animation" + ruleNb + i);
                Image resized = image.getScaledInstance(updatedWidth, updatedHeight, Image.SCALE_SMOOTH);
                JLabel frame = new JLabel(new ImageIcon(resized));
                frame.setPreferredSize(new Dimension(updatedWidth, updatedHeight));
                if(i > 0){
                    frame.setVisible(false);
                }
                frames[i] = frame;
                add(frame);
            }
            animation = new AnimatedRule(ruleNb, this);
        }

        public JLabel[] getFrames(){
            return frames;
        }

        public AnimatedRule getAnimation() {
            return animation;
        }
    } 
}
