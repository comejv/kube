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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.controller.graphical.MenuController;
import kube.view.GUI;
import kube.view.GUIColors;
import kube.view.components.Icon;
import kube.view.components.Buttons.ButtonIcon;
import kube.view.components.Buttons.MenuButton;
import kube.view.components.Buttons.SelectPlayerButton;

/*
 * This class extends JPanel and creates the main menu of the app.
 */
public class MenuPanel extends JPanel {

    private JButton rules;
    private CardLayout buttonsLayout;
    private JPanel buttonsPanel;
    public JPanel player1, player2;
    private HashMap<String, JComponent> networkObjects;
    private MenuController buttonListener;

    public MenuPanel(GUI gui, MenuController buttonListener) {
        this.buttonListener = buttonListener;
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
        title.setForeground(GUIColors.ACCENT.toColor());

        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.gridx = 1;
        elemGBC.gridy = 0;
        elemGBC.fill = GridBagConstraints.BOTH;
        elemGBC.anchor = GridBagConstraints.CENTER;
        elemGBC.weighty = 1;
        elemGBC.weightx = .3;
        modal.add(title, elemGBC);

        // Settings
        ButtonIcon settings = new ButtonIcon("settings", ResourceLoader.getBufferedImage("gear"), buttonListener);
        settings.resizeIcon(100, 100);
        settings.recolor(GUIColors.ACCENT);
        elemGBC = new GridBagConstraints();
        elemGBC.anchor = GridBagConstraints.NORTHEAST;
        elemGBC.insets = new Insets(20, 0, 0, 20);
        elemGBC.gridx = 2;
        elemGBC.gridy = 0;
        elemGBC.weighty = 1;
        elemGBC.weightx = .3;
        modal.add(settings, elemGBC);

        // Volume
        Icon volumeOnImg = new Icon(ResourceLoader.getBufferedImage("volume"));
        volumeOnImg.resizeIcon(100, 100);
        volumeOnImg.recolor(GUIColors.ACCENT);
        Icon volumeOffImg = new Icon(ResourceLoader.getBufferedImage("mute"));
        volumeOffImg.resizeIcon(100, 100);
        volumeOffImg.recolor(GUIColors.ACCENT);

        BufferedImage volumeImg = Config.isSoundMute() && Config.isMusicMute() ? volumeOffImg.getImage()
                : volumeOnImg.getImage();
        ButtonIcon volume = new ButtonIcon("volume", volumeImg, buttonListener) {
            @Override
            public void paintComponent(Graphics g) {
                BufferedImage volumeImg = Config.isSoundMute() && Config.isMusicMute() ? volumeOffImg.getImage()
                        : volumeOnImg.getImage();
                setImage(volumeImg);
                super.paintComponent(g);
            }
        };
        volume.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BufferedImage volumeImg = Config.isSoundMute() && Config.isMusicMute() ? volumeOffImg.getImage()
                        : volumeOnImg.getImage();
                volume.setImage(volumeImg);
            }
        });
        elemGBC = new GridBagConstraints();
        elemGBC.anchor = GridBagConstraints.NORTHWEST;
        elemGBC.insets = new Insets(20, 20, 0, 0);
        elemGBC.gridx = 0;
        elemGBC.gridy = 0;
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
        play.setActionCommand("startLocal");

        JButton loadButton = new MenuButton("CHARGER");
        playersButtons.add(loadButton, buttonsGBC);
        loadButton.addActionListener(buttonListener);
        loadButton.setActionCommand("loadmenu");

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

        networkObjects = new HashMap<>();

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
        joinGame.addActionListener(e -> {
            // Switch to the main panel
            buttonsLayout.show(buttonsPanel, "join");
        });

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

        networkObjects.put("ipPane", ipPort);

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

        JButton returnHost = new MenuButton("RETOUR");
        returnHost.setActionCommand("returnHost");
        returnHost.addActionListener(e -> {
            buttonsLayout.show(buttonsPanel, "online");
        });
        returnHost.addActionListener(buttonListener);

        hostPanel.add(returnHost, buttonsGBC);

        buttonsPanel.add("host", hostPanel);

        // JOIN //
        JPanel joinPanel = new JPanel();
        joinPanel.setOpaque(false);
        joinPanel.setLayout(new GridBagLayout());
        buttonsGBC = new GridBagConstraints();
        insets = new Insets(10, 0, 10, 0);
        buttonsGBC.gridx = 0;
        buttonsGBC.gridy = GridBagConstraints.RELATIVE;
        buttonsGBC.fill = GridBagConstraints.BOTH;
        buttonsGBC.insets = insets;

        JPanel ipPortFieldPanel = new JPanel();
        ipPortFieldPanel.setOpaque(true);
        ipPortFieldPanel.setBackground(GUIColors.ACCENT.toColor());
        ipPortFieldPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(GUIColors.TEXT.toColor(), 1),
                "Renseignez l'addresse (IP:port)",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Jomhuria", Font.PLAIN, 40),
                GUIColors.TEXT.toColor()));

        JTextField ipPortField = new JTextField(20);
        ipPortField.setFont(new Font("Jomhuria", Font.PLAIN, 30));
        ipPortField.setForeground(GUIColors.TEXT.toColor());
        ipPortField.setOpaque(true);
        ipPortField.setBackground(GUIColors.ACCENT.toColor());
        ipPortFieldPanel.add(ipPortField);

        ipPortField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleTextChange(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleTextChange(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleTextChange(e);
            }

            private void handleTextChange(DocumentEvent e) {
                String input = ipPortField.getText();
                if (input.contains(":")) {
                    String[] parts = input.split(":");
                    String ip = parts[0];
                    int port = Integer.valueOf(parts[1]);
                    Config.debug("Connecting to IP: " + ip + " on port: " + port);
                    Config.setHostIP(ip);
                    Config.setHostPort(port);
                } 
            }
        });

        joinPanel.add(ipPortFieldPanel, buttonsGBC);

        JButton connect = new MenuButton("CONNECTER");
        connect.setActionCommand("startOnline");
        connect.addActionListener(buttonListener);

        JButton returnJoin = new MenuButton("RETOUR");
        returnJoin.addActionListener(e -> {
            buttonsLayout.show(buttonsPanel, "online");
        });

        joinPanel.add(connect, buttonsGBC);
        joinPanel.add(returnJoin, buttonsGBC);

        buttonsPanel.add("join", joinPanel);
    }

    public void showHostMenu() {
        // Set text of the ip and port
        JTextPane ipPort = (JTextPane) networkObjects.get("ipPane");
        ipPort.setText(Config.getHostIP() + ":" + Config.getHostPort());
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
        buttonsLayout.show(buttonsPanel, "host");
    }

    public void enableHostStartButton(boolean b) {
        JButton start = (JButton) networkObjects.get("startButton");
        start.setEnabled(b);
    }

    public JButton getRulesButton() {
        return rules;
    }

    public HashMap<String, JComponent> getNetworkObjects() {
        return networkObjects;
    }

    public MenuController getButtonListener() {
        return buttonListener;
    }

    public CardLayout getButtonsLayout() {
        return buttonsLayout;
    }

    public JPanel getButtonsPanel() {
        return buttonsPanel;
    }
}
