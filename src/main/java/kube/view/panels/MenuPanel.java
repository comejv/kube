package kube.view.panels;

import java.awt.*;

import javax.swing.*;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.controller.graphical.MenuController;
import kube.view.components.Buttons.*;
import kube.view.GUIColors;

/*
 * This class extends JPanel and creates the main menu of the app.
 */
public class MenuPanel extends JPanel {

    public MenuPanel(MenuController buttonListener) {
        setLayout(new CardLayout());

        // ****************************************************************************************//
        // MENU //
        // ****************************************************************************************//

        JPanel modal = new JPanel();
        modal.setLayout(new GridBagLayout());
        add("modal", modal);

        // Game title
        JLabel title = new JLabel("KUBE", SwingConstants.CENTER);
        title.setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 6)));

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
        elemGBC = new GridBagConstraints();
        elemGBC.gridx = 1;
        elemGBC.gridy = 1;
        elemGBC.anchor = GridBagConstraints.CENTER;
        elemGBC.fill = GridBagConstraints.HORIZONTAL;
        elemGBC.weighty = .5;
        elemGBC.weightx = 1;
        modal.add(buttonsPanel, elemGBC);

        // ****************************************************************************************//
        // START BUTTONS //
        // ****************************************************************************************//
        JPanel startButtons = new JPanel();
        startButtons.setLayout(new GridBagLayout());
        GridBagConstraints buttonsGBC = new GridBagConstraints();
        Insets insets = new Insets(10, 0, 10, 0);
        buttonsGBC.gridx = 0;
        buttonsGBC.gridy = GridBagConstraints.RELATIVE;
        buttonsGBC.fill = GridBagConstraints.BOTH;
        buttonsGBC.insets = insets;

        // Local button
        JButton local = new MenuButton("LOCAL");
        local.addActionListener(e -> {
            // Switch to the players panel
            CardLayout cl = (CardLayout) (buttonsPanel.getLayout());
            cl.show(buttonsPanel, "players");
        });
        startButtons.add(local, buttonsGBC);

        // Online button
        JButton online = new MenuButton("ONLINE");
        startButtons.add(online, buttonsGBC);

        // Rules button
        JButton rules = new MenuButton("RULES");
        startButtons.add(rules, buttonsGBC);

        // Quit button
        JButton quit = new MenuButton("QUIT");
        startButtons.add(quit, buttonsGBC);

        buttonsPanel.add("start", startButtons);

        // ****************************************************************************************//
        // LOCAL //
        // ****************************************************************************************//

        // Players buttons - fill entire row
        JPanel playersButtons = new JPanel();
        playersButtons.setLayout(new GridBagLayout());
        buttonsGBC = new GridBagConstraints();
        insets = new Insets(10, 0, 10, 0);
        buttonsGBC.gridx = 0;
        buttonsGBC.gridy = GridBagConstraints.RELATIVE;
        buttonsGBC.fill = GridBagConstraints.BOTH;
        buttonsGBC.insets = insets;

        JPanel player1 = new SelectPlayerButton("PLAYER 1");
        JPanel player2 = new SelectPlayerButton("PLAYER 2");


        playersButtons.add(player1, buttonsGBC);
        playersButtons.add(player2, buttonsGBC);


        /*JButton playerTwo = new MenuButton("JOUEUR 2");
        playersButtons.add(playerTwo, buttonsGBC);*/

        JButton play = new MenuButton("PLAY");
        playersButtons.add(play, buttonsGBC);
        play.addActionListener(buttonListener);
        play.setActionCommand("play");

        buttonsPanel.add("players", playersButtons);
    }
}
