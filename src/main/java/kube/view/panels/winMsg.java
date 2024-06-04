package kube.view.panels;

import java.awt.*;

import javax.swing.*;

import kube.configuration.Config;
import kube.controller.graphical.Phase2Controller;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.view.GUI;
import kube.view.components.Buttons;

public class winMsg {
    private winPanel panel;
    private GUI gui;
    private Phase2Controller controller;

    public winMsg(winPanel panel, GUI gui,String text, Phase2Controller controller) {
        gui.getEventsToModel().add(new Action(ActionType.AI_PAUSE, true));
        this.panel = panel;
        this.gui = gui;
        this.controller=controller;

        panel.setVisible(true);
        panel.setOpacity(1);
        panel.setLayout(new BorderLayout());

        JPanel panelButton = new JPanel();
        panelButton.setOpaque(false);
        panelButton.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label1 = new JLabel(text);
        label1.setForeground(Color.BLACK);
        label1.setFont( new Font("Jomhuria", Font.BOLD, (int) (Config.INIT_HEIGHT * Config.getUIScale() / 10)));
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelButton.add(label1, gbc);

        JButton jeuButton = new Buttons.GamePhaseButton("Retourner au Jeu");
        jeuButton.setActionCommand("return");
        jeuButton.addMouseListener(controller);
        gbc.gridy = 1;
        panelButton.add(jeuButton, gbc);

        JButton quitButton = new Buttons.GamePhaseButton("Retourner au Menu");
        quitButton.setActionCommand("quit");
        quitButton.addMouseListener(controller);
        gbc.gridy = 2;
        panelButton.add(quitButton, gbc);


        panel.add(panelButton,BorderLayout.CENTER);
    }


}
