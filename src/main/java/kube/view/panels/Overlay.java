package kube.view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import kube.configuration.Config;
import kube.view.components.Buttons.RulesButton;

public class Overlay extends JPanel{

    private ActionListener buttonListener;

    public Overlay (ActionListener al){
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(Math.round(Config.getInitHeight()/1.5f), 
                                       Math.round(Config.getInitWidth()/1.5f)));
        setBackground(new Color(0, 0 , 0 , 150));
        setVisible(false);

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
        elemGBC.gridy = 1;
        elemGBC.anchor = GridBagConstraints.LAST_LINE_END;
        elemGBC.weighty = .5;
        elemGBC.weightx = .5;
        add(suivant, elemGBC);

        buttonListener = al;
    }
}
