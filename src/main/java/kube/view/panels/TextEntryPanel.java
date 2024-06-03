package kube.view.panels;

import kube.configuration.Config;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.view.GUI;
import kube.view.GUIColors;
import kube.view.animations.HexGlow;
import kube.view.animations.PanelGlow;
import kube.view.components.Buttons.MenuButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextEntryPanel extends JPanel {
    private JTextField textField;
    private MenuButton validateButton;
    private MenuButton cancelButton;
    private Font panelFont;
    private JLabel label;
    private String currentDateAndTime;
    private HexGlow hexGlow;
    private PanelGlow panGlow;

    public TextEntryPanel(GUI gui, HexGlow hexGlow, PanelGlow panGlow) {
        this.hexGlow = hexGlow;
        this.panGlow = panGlow;
        if (hexGlow != null) {
            hexGlow.getTimer().stop();
        }
        if (panGlow != null) {
            panGlow.getTimer().stop();
        }
        setLayout(new GridBagLayout());
        setOpaque(false);

        // Set panel size
        int width = Math.round(Config.INIT_WIDTH / 2f);
        int height = Math.round(Config.INIT_HEIGHT / 1.33f);
        setPreferredSize(new Dimension(width, height));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create the label
        label = new JLabel("Nom du fichier:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panelFont = new Font("Jomhuria", Font.PLAIN, (int) (height / 15.0f));
        label.setFont(panelFont);
        label.setForeground(GUIColors.ACCENT.toColor());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(label, gbc);

        // Initialize components
        textField = new JTextField(20);
        validateButton = new MenuButton("Valider");
        cancelButton = new MenuButton("Annuler");

        // Set custom font
        textField.setFont(panelFont);

        // Set default text in text field to the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        currentDateAndTime = sdf.format(new Date());
        textField.setText(currentDateAndTime);

        // Add components to panel
        gbc.gridy = 1;
        add(textField, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(validateButton, gbc);

        gbc.gridx = 1;
        add(cancelButton, gbc);

        // Add action listeners for buttons
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                gui.eventsToModel.add(new Action(ActionType.SAVE, text));
                gui.removeAllFromOverlay();
                gui.setGlassPanelVisible(true);
                if (hexGlow != null) {
                    hexGlow.getTimer().restart();
                }
                if (panGlow != null) {
                    panGlow.getTimer().restart();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.removeAllFromOverlay();
                gui.setGlassPanelVisible(true);
                if (hexGlow != null) {
                    hexGlow.getTimer().restart();
                }
                if (panGlow != null) {
                    panGlow.getTimer().restart();
                }
            }
        });

        // Add resizing listener to adjust font size and button size dynamically
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                adjustSizes();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        Color semiTransparentGray = new Color(128, 128, 128, 128); // 50% opacity
        g2d.setColor(semiTransparentGray);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }

    private void adjustSizes() {
        int panelHeight = getHeight();
        float scaleFactor = (float) panelHeight / Config.INIT_HEIGHT;
        int fontSize = Math.round(scaleFactor * (Config.INIT_HEIGHT / 15.0f));
        panelFont = panelFont.deriveFont((float) fontSize);
        label.setFont(panelFont);
        textField.setFont(panelFont);
        validateButton.setFont(panelFont);
        cancelButton.setFont(panelFont);

        Dimension buttonSize = new Dimension(Math.round(Config.INIT_WIDTH / 3 * scaleFactor),
                Math.round(Config.INIT_WIDTH / 15 * scaleFactor));
        validateButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        revalidate();
    }
}
