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

import kube.view.GUIColors;
import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.view.components.Buttons.RulesButton;

public class RulesPanel extends JPanel {

    public RulesPanel() {
        setLayout(new CardLayout());
        setPreferredSize(new Dimension(700, 500));

        JPanel ruleFirst = new RulePanel();
        add("ruleFirst", ruleFirst);

        JTextArea textArea = new JTextArea(ResourceLoader.getText("rule1"));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setBackground(new Color(0, 0, 0, 0));
        textArea.setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 12)));
        textArea.setForeground(GUIColors.TEXT.toColor());
        textArea.setBorder(null);
        textArea.setPreferredSize(new Dimension(550, 200));
        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.gridx = 0;
        elemGBC.gridy = 1;
        elemGBC.anchor = GridBagConstraints.CENTER;
        elemGBC.weighty = .5;
        elemGBC.weightx = .5;
        ruleFirst.add(textArea, elemGBC);
    }

    private class RulePanel extends JPanel {
        private RulePanel() {
            setLayout(new GridBagLayout());
            setPreferredSize(new Dimension(700, 500));
            setBackground(GUIColors.ACCENT.toColor());

            JLabel ruleTitle = new JLabel("RULES", SwingConstants.CENTER);
            ruleTitle.setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 6)));

            GridBagConstraints elemGBC = new GridBagConstraints();
            elemGBC.gridx = 0;
            elemGBC.gridy = 0;
            elemGBC.fill = GridBagConstraints.BOTH;
            elemGBC.gridwidth = GridBagConstraints.REMAINDER;
            elemGBC.anchor = GridBagConstraints.CENTER;
            elemGBC.weighty = .5;
            elemGBC.weightx = .5;
            add(ruleTitle, elemGBC);

            JButton suivant = new RulesButton("suivant");
            elemGBC = new GridBagConstraints();
            elemGBC.gridx = 0;
            elemGBC.gridy = 2;
            elemGBC.anchor = GridBagConstraints.LAST_LINE_END;
            elemGBC.weighty = .5;
            elemGBC.weightx = .5;
            add(suivant, elemGBC);
        }
    }
}
