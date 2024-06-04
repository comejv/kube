package kube.view.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

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

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);

        tabbedPanel = new JTabbedPane();
        tabbedPanel.setPreferredSize(new Dimension(width, height));
        tabbedPanel.setBackground(GUIColors.ACCENT.toColor());
        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.anchor = GridBagConstraints.CENTER;
        add(tabbedPanel, elemGBC);

        addGraphismePanel();
        addAudioTab();

        setVisible(true);
    }

    private void addGraphismePanel() {
        JPanel graphismePanel = createTab("Graphisme");
        JComboBox<String> resolutionManager = new JComboBox<>(res);
        resolutionManager.setSelectedIndex(0);
        resolutionManager.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch ((String) resolutionManager.getSelectedItem()) {
                    // TODO : refactor this switch case : use events and a controller ?
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

        JButton enlargeButton = new JButton("+");
        enlargeButton.setPreferredSize(new Dimension(50, 50));
        enlargeButton.addActionListener(e -> {
            gui.incrementUIScale(1.1);
        });

        JButton shrinkButton = new JButton("-");
        shrinkButton.setPreferredSize(new Dimension(50, 50));
        shrinkButton.addActionListener(e -> {
            gui.incrementUIScale(0.9);
        });

        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(100, 50));
        resetButton.addActionListener(e -> {
            gui.resetUIScale();
        });
        JButton changeModeSymbolButton = new JButton("Mode Accessibilité");
        changeModeSymbolButton.addActionListener(e -> {
            gui.changeMode("Symbol");
        });
        JButton changeModeTexturedButton = new JButton("Mode Texturé");
        changeModeTexturedButton.addActionListener(e -> {
            gui.changeMode("Textured");
        });

        wrapInJPanel(enlargeButton, null, graphismePanel);
        wrapInJPanel(shrinkButton, null, graphismePanel);
        wrapInJPanel(resetButton, null, graphismePanel);
        resolutionManager.setPreferredSize(new Dimension(350, 50));
        wrapInJPanel(resolutionManager, null, graphismePanel);
        wrapInJPanel(changeModeSymbolButton, null, graphismePanel);
        wrapInJPanel(changeModeTexturedButton, null, graphismePanel);

        addFillerPanel(graphismePanel);

        JButton saveChanges = new JButton("Quitter");
        saveChanges.setPreferredSize(new Dimension(150, 50));
        saveChanges.addActionListener(buttonListener);
        saveChanges.setActionCommand("confirmed_settings");
        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.anchor = GridBagConstraints.SOUTHEAST;
        wrapInJPanel(saveChanges, elemGBC, graphismePanel);
    }

    private void addAudioTab() {
        JPanel audioPanel = createTab("Audio");
        audioPanel.setLayout(new GridBagLayout());

        // Volume
        Icon volumeOnImg = new Icon(ResourceLoader.getBufferedImage("volume"));
        volumeOnImg.resizeIcon(100, 100);
        volumeOnImg.recolor(GUIColors.ACCENT);
        Icon volumeOffImg = new Icon(ResourceLoader.getBufferedImage("mute"));
        volumeOffImg.resizeIcon(100, 100);
        volumeOffImg.recolor(GUIColors.ACCENT);

        BufferedImage volumeImg = Config.isMute() ? volumeOnImg.getImage() : volumeOffImg.getImage();
        ButtonIcon volume = new ButtonIcon("volume", volumeImg, buttonListener);
        volume.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BufferedImage volumeImg = Config.isMute() ? volumeOnImg.getImage() : volumeOffImg.getImage();
                volume.setImage(volumeImg);
            }
        });

        audioPanel.add(volume);
    }

    private JPanel createTab(String name) {
        JPanel newPanel = new JPanel(new GridLayout(4, 2));

        JLabel newLabel = new JLabel(name, SwingConstants.CENTER);
        newLabel.setFont(new Font("Jomhuria", Font.PLAIN, (int) (Config.INIT_HEIGHT / 12)));
        newLabel.setForeground(GUIColors.ACCENT.toColor());
        newLabel.setPreferredSize(new Dimension(200, 50));
        tabbedPanel.addTab(name, newPanel);
        tabbedPanel.setTabComponentAt(getTabNb(), newLabel);
        setTabNb(getTabNb() + 1);

        return newPanel;
    }

    private void addFillerPanel(JPanel container) {
        // set to 7 because there's 4 x 2 cells minus one reserved for the saveChanges
        // Button
        int nbToFill = 7 - container.getComponentCount();
        for (int i = 0; i < nbToFill; i++) {
            JPanel filler = new JPanel();
            container.add(filler);
        }
    }

    private void wrapInJPanel(Component c, GridBagConstraints elemGBC, JPanel container) {
        JPanel wraper = new JPanel(new GridBagLayout());
        container.add(wraper);
        wraper.add(c, elemGBC);
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
