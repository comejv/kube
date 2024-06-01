package kube.view.panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.controller.graphical.MenuController;
import kube.view.GUI;
import kube.view.GUIColors;
import kube.view.components.Buttons.ButtonIcon;
import kube.view.components.Buttons.MenuButton;
import kube.view.components.Buttons.SelectPlayerButton;

/*
 * This class extends JPanel and creates the main menu of the app.
 */
public class MenuPanel extends JPanel {

    // TODO : refactor this class to make it more readable
    private GUI gui;
    private JButton rules;

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
                super.paintComponent(g);
                Image scaledBackground = backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                g.drawImage(scaledBackground, 0, 0, this);
            }
        };
        modal.setLayout(new GridBagLayout());

        add(modal, BorderLayout.CENTER);

        // Game title
        JLabel title = new JLabel("KUBE", SwingConstants.CENTER);
        title.setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.INIT_HEIGHT / 6)));

        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.gridx = 0;
        elemGBC.gridy = 0;
        elemGBC.fill = GridBagConstraints.BOTH;
        elemGBC.gridwidth = GridBagConstraints.REMAINDER;
        elemGBC.anchor = GridBagConstraints.CENTER;
        elemGBC.weighty = 1;
        elemGBC.weightx = .3;
        modal.add(title, elemGBC);

        // Settings
        ButtonIcon settings = new ButtonIcon("settings", ResourceLoader.getBufferedImage("gear"), buttonListener);
        settings.resizeIcon(100, 100);
        settings.recolor(GUIColors.ACCENT);
        elemGBC = new GridBagConstraints();
        elemGBC.gridx = 2;
        elemGBC.gridy = 2;
        elemGBC.weighty = 1;
        elemGBC.weightx = .3;
        modal.add(settings, elemGBC);

        // Volume
        ButtonIcon volume = new ButtonIcon("volume", ResourceLoader.getBufferedImage("volume"), buttonListener);
        volume.resizeIcon(100, 100);
        volume.recolor(GUIColors.ACCENT);
        elemGBC = new GridBagConstraints();
        elemGBC.gridx = 0;
        elemGBC.gridy = 2;
        elemGBC.weighty = 1;
        elemGBC.weightx = .3;
        modal.add(volume, elemGBC);

        JPanel buttonsPanel = new JPanel(new CardLayout());
        buttonsPanel.setOpaque(false);
        elemGBC = new GridBagConstraints();
        elemGBC.gridx = 1;
        elemGBC.gridy = 1;
        elemGBC.anchor = GridBagConstraints.CENTER;
        elemGBC.fill = GridBagConstraints.HORIZONTAL;
        elemGBC.weighty = .3;
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
        JButton online = new MenuButton("EN LIGNE");
        online.addActionListener(buttonListener);
        online.setActionCommand("online");
        // TODO : add online panel

        // Rules button
        rules = new MenuButton("REGLES");
        rules.addActionListener(buttonListener);
        rules.setActionCommand("rules");

        // Quit button
        JButton quit = new MenuButton("QUITTER");
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

        
        JPanel player1 = new SelectPlayerButton("JOUEUR 1");
        JPanel player2 = new SelectPlayerButton("JOUEUR 2");

        playersButtons.add(player1, buttonsGBC);
        playersButtons.add(player2, buttonsGBC);

        JButton play = new MenuButton("JOUER");
        playersButtons.add(play, buttonsGBC);
        play.addActionListener(buttonListener);
        play.setActionCommand("play");

        JButton returnButton = new MenuButton("RETOUR");
        playersButtons.add(returnButton, buttonsGBC);
        returnButton.addActionListener(e -> {
            // Switch to the main panel
            CardLayout cl = (CardLayout) (buttonsPanel.getLayout());
            cl.show(buttonsPanel, "start");
        });


        buttonsPanel.add("players", playersButtons);

        // ***************************************************************************************//
        // RULES //
        // ***************************************************************************************//

    }

    public JButton getRulesButton() {
        return rules;
    }
}
