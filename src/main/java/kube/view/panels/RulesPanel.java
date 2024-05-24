package kube.view.panels;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import kube.view.GUI;
import kube.view.GUIColors;
import kube.configuration.Config;
import kube.view.components.Buttons.RulesButton;

public class RulesPanel extends JPanel{

    private GUI gui;
    private int width;
    private int height;
    JPanel cardPanel;
    JPanel gridPanel;
    
    public RulesPanel(GUI gui){

        this.gui = gui;
        width = Config.getInitWidth()/2;
        height = Config.getInitHeight()/2;

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
        
        JPanel ruleFirst = new RulePanel();
        cardPanel.add(ruleFirst, "ruleFirst");
        
        // JTextArea textArea = new JTextArea(ResourceLoader.getText("rule1"));
        // textArea.setEditable(false);
        // textArea.setLineWrap(true);
        // textArea.setBackground(new Color(0, 0, 0, 0));
        // textArea.setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 12)));
        // textArea.setForeground(GUIColors.TEXT.toColor());
        // textArea.setBorder(null);
        // textArea.setPreferredSize(new Dimension(550, 200));
        
        // GridBagConstraints elemGBC = new GridBagConstraints();
        // elemGBC.gridx = 0;
        // elemGBC.gridy = 1;
        // elemGBC.anchor = GridBagConstraints.CENTER;
        // elemGBC.weighty = .5;
        // elemGBC.weightx = .5;
        // ruleFirst.add(textArea, elemGBC);
        setVisible(true);
    }
    
    private class RulePanel extends JPanel {
        private RulePanel(){
            setLayout(new GridBagLayout());
            setBackground(GUIColors.ACCENT.toColor());

            JButton suivant = new RulesButton("suivant");
            GridBagConstraints elemGBC = new GridBagConstraints();
            elemGBC.gridx = 0;
            elemGBC.gridy = 2;
            elemGBC.anchor = GridBagConstraints.LAST_LINE_END;
            elemGBC.weighty = .5;
            elemGBC.weightx = .5;
            add(suivant, elemGBC);
        }
    }
}
