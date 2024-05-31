package kube.view.panels;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import kube.configuration.Configuration;
import kube.configuration.ResourceLoader;
import kube.controller.graphical.MenuController;
import kube.view.components.Buttons.*;
import kube.view.GUI;
import kube.view.GUIColors;

/*
 * This class extends JPanel and creates the main menu of the app.
 */
public class MenuPanel extends JPanel {

    // TODO : refactor this class to make it more readable
    private GUI gui;

    public MenuPanel(GUI gui, MenuController buttonListener) {
        this.gui = gui;

        setLayout(new BorderLayout());

        // ****************************************************************************************//
        // MENU //
        // ****************************************************************************************//
        BufferedImage backgroundImage = ResourceLoader.getBufferedImage("background");
        JPanel modal = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Config.debug("painting menu");
                super.paintComponent(g);
                Image scaledBackground = backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                g.drawImage(scaledBackground, 0, 0, this);
            }
        };
        modal.setLayout(new GridBagLayout());

        add(modal, BorderLayout.CENTER);

        // Game title
        JLabel title = new JLabel("KUBE", SwingConstants.CENTER);
        title.setFont(new Font("Jomhuria", Font.BOLD, (int) (Configuration.INIT_HEIGHT / 6)));

        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.gridx = 0;
        elemGBC.gridy = 0;
        elemGBC.fill = GridBagConstraints.BOTH;
        elemGBC.gridwidth = GridBagConstraints.REMAINDER;
        elemGBC.anchor = GridBagConstraints.CENTER;
        elemGBC.weighty = .5;
        elemGBC.weightx = .5;
        modal.add(title, elemGBC);

        // Settings
        ButtonIcon settings = new ButtonIcon("settings", ResourceLoader.getBufferedImage("gear"), buttonListener);
        settings.resizeIcon(100, 100);
        settings.recolor(GUIColors.ACCENT);
        elemGBC = new GridBagConstraints();
        elemGBC.gridx = 2;
        elemGBC.gridy = 2;
        modal.add(settings, elemGBC);

        // Volume
        ButtonIcon volume = new ButtonIcon("volume", ResourceLoader.getBufferedImage("volume"), buttonListener);
        volume.resizeIcon(100, 100);
        volume.recolor(GUIColors.ACCENT);
        elemGBC = new GridBagConstraints();
        elemGBC.gridx = 0;
        elemGBC.gridy = 2;
        modal.add(volume, elemGBC);

        JPanel buttonsPanel = new JPanel(new CardLayout());
        buttonsPanel.setOpaque(false);
        elemGBC = new GridBagConstraints();
        elemGBC.gridx = 1;
        elemGBC.gridy = 1;
        elemGBC.anchor = GridBagConstraints.CENTER;
        elemGBC.fill = GridBagConstraints.HORIZONTAL;
        elemGBC.weighty = .5;
        elemGBC.weightx = 1;
        modal.add(buttonsPanel, elemGBC);

        // ***************************************************************************************//
        // START BUTTONS //
        // ***************************************************************************************//
        JPanel startButtons = new JPanel();
        startButtons.setOpaque(false);
        startButtons.setLayout(new GridBagLayout());
        GridBagConstraints buttonsGBC = new GridBagConstraints();
        Insets insets = new Insets(10, 0, 10, 0);
        buttonsGBC.gridx = 0;
        buttonsGBC.gridy = GridBagConstraints.RELATIVE;
        buttonsGBC.fill = GridBagConstraints.BOTH;
        buttonsGBC.insets = insets;

        // Local button
        JButton local = new MenuButton("LOCAL");
        local.addActionListener(buttonListener);
        local.setActionCommand("local");
        local.addActionListener(e -> {
            // Switch to the players panel
            CardLayout cl = (CardLayout) (buttonsPanel.getLayout());
            cl.show(buttonsPanel, "players");
        });

        // Online button
        JButton online = new MenuButton("ONLINE");
        online.addActionListener(buttonListener);
        online.setActionCommand("online");
        // TODO : add online panel

        // Rules button
        JButton rules = new MenuButton("RULES");
        rules.addActionListener(buttonListener);
        rules.setActionCommand("rules");

        // Quit button
        JButton quit = new MenuButton("QUIT");
        quit.addActionListener(buttonListener);
        quit.setActionCommand("quit");

        startButtons.add(local, buttonsGBC);
        startButtons.add(online, buttonsGBC);
        startButtons.add(rules, buttonsGBC);
        startButtons.add(quit, buttonsGBC);

        buttonsPanel.add("start", startButtons);

        // ***************************************************************************************//
        // LOCAL //
        // ***************************************************************************************//

        // Players buttons - fill entire row
        JPanel playersButtons = new JPanel();
        playersButtons.setOpaque(false);
        playersButtons.setLayout(new GridBagLayout());
        buttonsGBC = new GridBagConstraints();
        insets = new Insets(10, 0, 10, 0);
        buttonsGBC.gridx = 0;
        buttonsGBC.gridy = GridBagConstraints.RELATIVE;
        buttonsGBC.fill = GridBagConstraints.BOTH;
        buttonsGBC.insets = insets;

        JButton returnButton = new MenuButton("Retour");
        playersButtons.add(returnButton, buttonsGBC);
        returnButton.addActionListener(e -> {
            // Switch to the main panel
            CardLayout cl = (CardLayout) (buttonsPanel.getLayout());
            cl.show(buttonsPanel, "start");
        });

        JPanel player1 = new SelectPlayerButton("PLAYER 1");
        JPanel player2 = new SelectPlayerButton("PLAYER 2");

        playersButtons.add(player1, buttonsGBC);
        playersButtons.add(player2, buttonsGBC);

        JButton play = new MenuButton("PLAY");
        playersButtons.add(play, buttonsGBC);
        play.addActionListener(buttonListener);
        play.setActionCommand("play");

        buttonsPanel.add("players", playersButtons);

        // ***************************************************************************************//
        // RULES //
        // ***************************************************************************************//

    }
}
