package kube.view.panels;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
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
    private CardLayout buttonsLayout;
    private JPanel buttonsPanel;
    public JPanel player1, player2;

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

        buttonsLayout = new CardLayout();
        buttonsPanel = new JPanel(buttonsLayout);
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
            buttonsLayout.show(buttonsPanel, "players");
        });

        // Online button
        JButton online = new MenuButton("EN LIGNE");
        online.addActionListener(buttonListener);
        online.setActionCommand("online");
        online.addActionListener(e -> {
            // Switch to the online panel
            buttonsLayout.show(buttonsPanel, "online");
        });

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

        player1 = new SelectPlayerButton("JOUEUR 1");
        player2 = new SelectPlayerButton("JOUEUR 2");

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
            buttonsLayout.show(buttonsPanel, "start");
        });

        buttonsPanel.add("players", playersButtons);

        // ***************************************************************************************//
        // ONLINE //
        // ***************************************************************************************//

        JPanel onlinePanel = new JPanel();
        onlinePanel.setOpaque(false);
        onlinePanel.setLayout(new GridBagLayout());
        buttonsGBC = new GridBagConstraints();
        insets = new Insets(10, 0, 10, 0);
        buttonsGBC.gridx = 0;
        buttonsGBC.gridy = GridBagConstraints.RELATIVE;
        buttonsGBC.fill = GridBagConstraints.BOTH;
        buttonsGBC.insets = insets;

        JButton createGame = new MenuButton("HÉBERGER");
        createGame.addActionListener(buttonListener);
        createGame.setActionCommand("host");

        JButton joinGame = new MenuButton("REJOINDRE");
        joinGame.addActionListener(buttonListener);
        joinGame.setActionCommand("join");

        JButton returnOnline = new MenuButton("RETOUR");
        returnOnline.addActionListener(e -> {
            // Switch to the main panel
            buttonsLayout.show(buttonsPanel, "start");
        });

        onlinePanel.add(createGame, buttonsGBC);
        onlinePanel.add(joinGame, buttonsGBC);
        onlinePanel.add(returnOnline, buttonsGBC);

        buttonsPanel.add("online", onlinePanel);

        // HOST //
        JPanel hostPanel = new JPanel();
        hostPanel.setOpaque(false);
        hostPanel.setLayout(new GridBagLayout());
        buttonsGBC = new GridBagConstraints();
        insets = new Insets(10, 0, 10, 0);
        buttonsGBC.gridx = 0;
        buttonsGBC.gridy = GridBagConstraints.RELATIVE;
        buttonsGBC.fill = GridBagConstraints.BOTH;
        buttonsGBC.insets = insets;

        // Show the ip and port number
        JTextPane ipPort = new JTextPane();
        ipPort.setFont(new Font("Jomhuria", Font.PLAIN, 60));
        ipPort.setForeground(GUIColors.TEXT.toColor());
        ipPort.setOpaque(false);
        ipPort.setBackground(new Color(0, 0, 0, 0));
        ipPort.setEditable(false);
        JPanel ipPortPanel = new JPanel();
        ipPortPanel.setOpaque(true);
        ipPortPanel.setBackground(GUIColors.ACCENT.toColor());
        ipPortPanel.add(ipPort);
        ipPort.setAlignmentX(CENTER_ALIGNMENT);
        JButton copy = new MenuButton("COPIER VOTRE IP");
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                StringSelection stringSelection = new StringSelection(Config.getHostIP() + ":" + Config.getHostPort());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });

        hostPanel.add(ipPortPanel, buttonsGBC);
        hostPanel.add(copy, buttonsGBC);

        JButton start = new MenuButton("DÉMARRER");
        start.addActionListener(buttonListener);
        start.setActionCommand("startOnline");
        start.setEnabled(false);

        hostPanel.add(start, buttonsGBC);

        JButton returnHost = new MenuButton("RETOUR");
        returnHost.setActionCommand("returnHost");
        returnHost.addActionListener(e -> {
            buttonsLayout.show(buttonsPanel, "online");
        });
        returnHost.addActionListener(buttonListener);

        hostPanel.add(returnHost, buttonsGBC);

        buttonsPanel.add("host", hostPanel);
    }

    public void showHostMenu() {
        // Set text of the ip and port
        JPanel hostPanel = (JPanel) buttonsPanel.getComponent(3);
        JTextPane ipPort = (JTextPane) ((JPanel) hostPanel.getComponent(0)).getComponent(0);
        ipPort.setText(Config.getHostIP() + ":" + Config.getHostPort());
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
        buttonsLayout.show(buttonsPanel, "host");
    }

    public JButton getRulesButton() {
        return rules;
    }
}
