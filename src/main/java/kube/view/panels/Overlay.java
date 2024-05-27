package kube.view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;

import javax.swing.JPanel;

import kube.configuration.Config;

public class Overlay extends JPanel{

    private ActionListener buttonListener;

    public Overlay (ActionListener al){
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(Math.round(Config.getInitHeight()/1.5f), 
                                       Math.round(Config.getInitWidth()/1.5f)));
        setBackground(new Color(0, 0 , 0 , 150));
        setVisible(false);

        add(new RulesPanel());

        buttonListener = al;
    }
}
