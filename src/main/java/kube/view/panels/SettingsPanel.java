package kube.view.panels;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.controller.graphical.MenuController;
import kube.view.GUI;
import kube.view.GUIColors;
import kube.view.components.Icon;
import kube.view.components.Buttons.ButtonIcon;

public class SettingsPanel extends JPanel {

    private GUI gui;
    private int width;
    private int height;
    private MenuController buttonListener;
    private JTabbedPane tabbedPanel;
    private int tabNb;
    private String[] res = { "1140 x 900", "1366 x 900", "1600 x 900", "1920 x 1080", "2560 x 1440" };

    public SettingsPanel(GUI gui, MenuController buttonListener) {
        this.gui = gui;
        this.buttonListener = buttonListener;
        tabNb = 0;
        width = 800;
        height = 600;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);

        tabbedPanel = new JTabbedPane();
        tabbedPanel.setPreferredSize(new Dimension(width, height));
        tabbedPanel.setBackground(GUIColors.ACCENT.toColor());
        add(tabbedPanel, BorderLayout.CENTER);

        addGraphicsPanel();
        addAudioTab();
        addCreditsTab();

        setVisible(true);
    }

    private void addGraphicsPanel() {
        JPanel graphicsPanel = createTab("Graphismes");
        graphicsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Resolution Manager
        JLabel resolutionLabel = new JLabel("Résolution de la fenêtre ");
        JComboBox<String> resolutionManager = new JComboBox<>(res);
        resolutionManager.setSelectedIndex(0);
        resolutionManager.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch ((String) resolutionManager.getSelectedItem()) {
                    case "1140 x 900":
                        gui.getMainFrame().setSize(1140, 900);
                        break;
                    case "1366 x 900":
                        gui.getMainFrame().setSize(1366, 900);
                        break;
                    case "1600 x 900":
                        gui.getMainFrame().setSize(1600, 900);
                        break;
                    case "1920 x 1080":
                        gui.getMainFrame().setSize(1920, 1080);
                        break;
                    case "2560 x 1440":
                        gui.getMainFrame().setSize(2560, 1440);
                        break;
                    default:
                        break;
                }
                OverlayPanel overlay = (OverlayPanel) gui.getOverlay().getComponent(0);
                overlay.repaint();
            }
        });
        resolutionManager.setPreferredSize(new Dimension(200, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        graphicsPanel.add(resolutionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        graphicsPanel.add(resolutionManager, gbc);

        // Accessibility and Textured Mode Toggle
        JLabel accessibilityModeLabel = new JLabel("Activer le mode accessibilité:");
        JToggleButton accessibilityToggleButton = new JToggleButton("Désactivé");
        accessibilityToggleButton.setPreferredSize(new Dimension(150, 30));
        if (Config.getMode() == Config.SYMBOL_MODE) {
            accessibilityToggleButton.setText("Activé");
            accessibilityToggleButton.setSelected(true);
        } else {
            accessibilityToggleButton.setText("Désactivé");
            accessibilityToggleButton.setSelected(false);
        }
        accessibilityToggleButton.addItemListener(e -> {
            if (accessibilityToggleButton.isSelected()) {
                gui.changeMode(Config.SYMBOL_MODE);
                accessibilityToggleButton.setText("Activé");
            } else {
                gui.changeMode(Config.TEXTURED_MODE);
                accessibilityToggleButton.setText("Désactivé");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        graphicsPanel.add(accessibilityModeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        graphicsPanel.add(accessibilityToggleButton, gbc);

        // Quit Button
        JButton saveChanges = new JButton("Quitter");
        saveChanges.setPreferredSize(new Dimension(150, 30));
        saveChanges.addActionListener(buttonListener);
        saveChanges.setActionCommand("confirmed_settings");

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        graphicsPanel.add(saveChanges, gbc);
    }

    private void addAudioTab() {
        JPanel audioPanel = createTab("Audio");
        audioPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        Icon volumeOnImg = new Icon(ResourceLoader.getBufferedImage("volume"));
        volumeOnImg.resizeIcon(100, 100);
        volumeOnImg.recolor(GUIColors.ACCENT);
        Icon volumeOffImg = new Icon(ResourceLoader.getBufferedImage("mute"));
        volumeOffImg.resizeIcon(100, 100);
        volumeOffImg.recolor(GUIColors.ACCENT);

        // Music Control
        JLabel musicLabel = new JLabel("Musique :");
        BufferedImage musicVolumeImg = Config.isMusicMute() ? volumeOffImg.getImage() : volumeOnImg.getImage();
        ButtonIcon musicButton = new ButtonIcon("musicVolume", musicVolumeImg, buttonListener);
        musicButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BufferedImage musicVolumeImg = Config.isMusicMute() ? volumeOffImg.getImage() : volumeOnImg.getImage();
                musicButton.setImage(musicVolumeImg);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        audioPanel.add(musicLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        audioPanel.add(musicButton, gbc);

        // Sound Effects Control
        JLabel soundEffectsLabel = new JLabel("Effets sonores :");
        BufferedImage soundVolumeImg = Config.isSoundMute() ? volumeOffImg.getImage() : volumeOnImg.getImage();
        ButtonIcon soundButton = new ButtonIcon("soundVolume", soundVolumeImg, buttonListener);
        soundButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BufferedImage soundVolumeImg = Config.isSoundMute() ? volumeOffImg.getImage() : volumeOnImg.getImage();
                soundButton.setImage(soundVolumeImg);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        audioPanel.add(soundEffectsLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        audioPanel.add(soundButton, gbc);

        // Quit Button
        JButton saveChanges = new JButton("Quitter");
        saveChanges.setPreferredSize(new Dimension(150, 30));
        saveChanges.addActionListener(buttonListener);
        saveChanges.setActionCommand("confirmed_settings");

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        audioPanel.add(saveChanges, gbc);
    }

    private void addCreditsTab() {
        JPanel creditsPanel = createTab("Credits");
        creditsPanel.setLayout(new BorderLayout());

        JEditorPane creditsPane = new JEditorPane();
        creditsPane.setContentType("text/html");
        creditsPane.setEditable(false);

        String creditsContent = ResourceLoader.getText("credits.html");
        creditsPane.setText(creditsContent);

        creditsPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (UnsupportedOperationException err) {
                        gui.showError("Impossible d'ouvrir le lien",
                                "Nous n'avons pas réussi à ouvrir le lien dans un navigateur. Vous pouvez trouver les liens sur notre page github : github.com/comejv/kube dans le README ou le fichier credits.txt");
                    } catch (URISyntaxException | IOException err) {
                        gui.showError("Impossible d'ouvrir le lien",
                                "Le lien est mal formatté ou inaccessible, impossible de l'ouvrir.");
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(creditsPane);
        creditsPanel.add(scrollPane, BorderLayout.CENTER);

        JButton saveChanges = new JButton("Quitter");
        saveChanges.setPreferredSize(new Dimension(150, 30));
        saveChanges.addActionListener(buttonListener);
        saveChanges.setActionCommand("confirmed_settings");

        creditsPanel.add(saveChanges, BorderLayout.SOUTH);
    }

    private JPanel createTab(String name) {
        JPanel newPanel = new JPanel();
        JLabel newLabel = new JLabel(name, SwingConstants.CENTER);
        newLabel.setFont(new Font("Jomhuria", Font.PLAIN, (int) (Config.INIT_HEIGHT / 12)));
        newLabel.setForeground(GUIColors.ACCENT.toColor());
        newLabel.setPreferredSize(new Dimension(300, 50));
        tabbedPanel.addTab(name, newPanel);
        tabbedPanel.setTabComponentAt(getTabNb(), newLabel);
        setTabNb(getTabNb() + 1);
        return newPanel;
    }

    public void loadPanel() {
        tabbedPanel.setSelectedIndex(2);
    }

    public JTabbedPane getTabbedPanel() {
        return tabbedPanel;
    }

    private int getTabNb() {
        return tabNb;
    }

    private void setTabNb(int newTabNb) {
        tabNb = newTabNb;
    }
}
