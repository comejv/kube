package kube.view.panels;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;

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
        width = 600;
        height = 600;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);

        tabbedPanel = new JTabbedPane();
        tabbedPanel.setPreferredSize(new Dimension(width, height));
        tabbedPanel.setBackground(GUIColors.ACCENT.toColor());
        add(tabbedPanel, BorderLayout.CENTER);

        addGraphismePanel();
        addAudioTab();

        setVisible(true);
    }

    private void addGraphismePanel() {
        JPanel graphismePanel = createTab("Graphisme");
        graphismePanel.setLayout(new GridBagLayout());
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
        graphismePanel.add(resolutionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        graphismePanel.add(resolutionManager, gbc);

        // UI Scale
        JLabel uiScaleLabel = new JLabel("Changer la taille de l'interface:");
        JButton enlargeButton = new JButton("+");
        enlargeButton.setPreferredSize(new Dimension(50, 30));
        enlargeButton.addActionListener(e -> gui.incrementUIScale(1.1));

        JButton shrinkButton = new JButton("-");
        shrinkButton.setPreferredSize(new Dimension(50, 30));
        shrinkButton.addActionListener(e -> gui.incrementUIScale(0.9));

        gbc.gridx = 0;
        gbc.gridy = 1;
        graphismePanel.add(uiScaleLabel, gbc);

        JPanel uiScalePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        uiScalePanel.add(enlargeButton);
        uiScalePanel.add(shrinkButton);

        gbc.gridx = 1;
        gbc.gridy = 1;
        graphismePanel.add(uiScalePanel, gbc);

        // Reset Button
        JLabel resetLabel = new JLabel("Réinitialiser l'interface");
        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(100, 30));
        resetButton.addActionListener(e -> gui.resetUIScale());

        gbc.gridx = 0;
        gbc.gridy = 2;
        graphismePanel.add(resetLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        graphismePanel.add(resetButton, gbc);

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
        gbc.gridy = 3;
        graphismePanel.add(accessibilityModeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        graphismePanel.add(accessibilityToggleButton, gbc);

        // Quit Button
        JButton saveChanges = new JButton("Quitter");
        saveChanges.setPreferredSize(new Dimension(150, 30));
        saveChanges.addActionListener(buttonListener);
        saveChanges.setActionCommand("confirmed_settings");

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        graphismePanel.add(saveChanges, gbc);
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
        BufferedImage musicVolumeImg = Config.isMusicMute() ? volumeOnImg.getImage() : volumeOffImg.getImage();
        ButtonIcon musicButton = new ButtonIcon("musicVolume", musicVolumeImg, buttonListener);
        musicButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BufferedImage volumeImg = Config.isMusicMute() ? volumeOffImg.getImage() : volumeOnImg.getImage();
                musicButton.setImage(volumeImg);
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
        BufferedImage soundVolumeImg = Config.isMusicMute() ? volumeOnImg.getImage() : volumeOffImg.getImage();
        ButtonIcon soundButton = new ButtonIcon("soundVolume", soundVolumeImg, buttonListener);
        soundButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BufferedImage volumeImg = Config.isSoundMute() ? volumeOffImg.getImage() : volumeOnImg.getImage();
                soundButton.setImage(volumeImg);
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

    private JPanel createTab(String name) {
        JPanel newPanel = new JPanel();
        JLabel newLabel = new JLabel(name, SwingConstants.CENTER);
        newLabel.setFont(new Font("Jomhuria", Font.PLAIN, (int) (Config.INIT_HEIGHT / 12)));
        newLabel.setForeground(GUIColors.ACCENT.toColor());
        newLabel.setPreferredSize(new Dimension(200, 50));
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
