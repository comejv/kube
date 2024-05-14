package kube.view.panels;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.view.components.Buttons.*;

/*
 * This class extends JPanel and creates the main menu of the app.
 */
public class MenuPanel extends JPanel {
    private GraphicsEnvironment ge;

    public MenuPanel(ActionListener buttonListener) {
        setLayout(new GridBagLayout());

        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font buttonsFont = Font.createFont(Font.TRUETYPE_FONT,
                    ResourceLoader.getResourceAsStream("fonts/Jomhuria-Regular.ttf"));
            setFont(buttonsFont);
            ge.registerFont(buttonsFont);
            ge.getAvailableFontFamilyNames();
        } catch (IOException | FontFormatException e) {
            Config.debug("Error : ");
            System.err.println("Could not load buttons font, using default.");
        }

        // Game title
        JLabel title = new JLabel("KUBE");
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        title.setBorder(BorderFactory.createLineBorder(Color.red));
        add(title, c);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new CardLayout());
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        add(buttonsPanel, c);

        // Local and Online buttons - fill entire row
        JPanel localOnlineButtons = new JPanel();
        localOnlineButtons.setLayout(new BoxLayout(localOnlineButtons, BoxLayout.Y_AXIS));
        buttonsPanel.add(localOnlineButtons, "localonline");

        // Local button
        JButton local = new MenuButton("LOCAL");
        local.addActionListener(e -> {
            // Switch to the players panel
            CardLayout cl = (CardLayout) (buttonsPanel.getLayout());
            cl.show(buttonsPanel, "players");
        });
        localOnlineButtons.add(local);

        // Online button
        JButton online = new MenuButton("ONLINE");
        localOnlineButtons.add(online);

        buttonsPanel.add("main", localOnlineButtons);

        // Players buttons - fill entire row
        JPanel playersButtons = new JPanel();
        playersButtons.setLayout(new BoxLayout(playersButtons, BoxLayout.Y_AXIS));
        buttonsPanel.add(playersButtons, "players");

        // Player buttons
        JButton playerOne = new MenuButton("JOUEUR 1");
        playersButtons.add(playerOne);

        JButton playerTwo = new MenuButton("JOUEUR 2");
        playersButtons.add(playerTwo);

        JButton play = new MenuButton("PLAY");
        playersButtons.add(play);
    }
}
