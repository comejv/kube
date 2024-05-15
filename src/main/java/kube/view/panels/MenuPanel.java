package kube.view.panels;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Insets;
import java.awt.Image;

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
        setLayout(new CardLayout());

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
        
        //****************************************************************************************//
        //                                       MENU                                             //
        //****************************************************************************************//
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new CardLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        add("buttonsPanel", buttonsPanel);
        
        // Rules panel
        JPanel rulesPanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        rulesPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        rulesPanel.setBackground(new Color(0, 0 , 0 , 125));
        
        // Main buttons - fill entire row
        JPanel menuMainButtons = new JPanel();
        menuMainButtons.setLayout(new GridBagLayout());
        GridBagConstraints gbc1 = new GridBagConstraints();
        Insets insets = new Insets(10, 0, 10, 0);
        gbc1.gridx = 0;
        gbc1.gridy = GridBagConstraints.RELATIVE;
        gbc1.weightx = 1;
        gbc1.weighty = 1;
        gbc1.insets = insets;
        buttonsPanel.add(menuMainButtons, "menuMainButtons");
        
        // Game title
        JLabel title = new JLabel("KUBE");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        title.setBorder(BorderFactory.createLineBorder(Color.red));
        menuMainButtons.add(title, c);

        // Local button
        JButton local = new MenuButton("LOCAL");
        local.addActionListener(e -> {
            // Switch to the players panel
            CardLayout cl = (CardLayout) (buttonsPanel.getLayout());
            cl.show(buttonsPanel, "players");
        });
        menuMainButtons.add(local, gbc1);
        
        // Online button
        JButton online = new MenuButton("ONLINE");
        online.addActionListener(e -> {
            // Switch to the players panel
            CardLayout cl = (CardLayout) (buttonsPanel.getLayout());
            cl.show(buttonsPanel, "players");
        });
        menuMainButtons.add(online, gbc1);
        
        
        // Rules button
        JButton rules = new MenuButton("RULES");
        rules.addActionListener(e -> {
            // Switch to the rules pop up
            CardLayout cl = (CardLayout) (this.getLayout());
            cl.show(this, "rules");
        });
        menuMainButtons.add(rules, gbc1);
        
        // Quit button
        JButton quit = new MenuButton("QUIT");
        quit.addActionListener(e -> {
            System.exit(0);
        });
        menuMainButtons.add(quit, gbc1);
        
        // Parameter button
        Image img = ResourceLoader.lireImage("parameters");
        JButton parameter = new ParameterButton(img);
        gbc1.anchor = GridBagConstraints.LAST_LINE_END;
        menuMainButtons.add(parameter, gbc1);
        
        //****************************************************************************************//
        //                                      LOCAL                                             //
        //****************************************************************************************//
        
        // Players buttons - fill entire row
        JPanel playersButtons = new JPanel();
        playersButtons.setLayout(new BoxLayout(playersButtons, BoxLayout.Y_AXIS));
        buttonsPanel.add("players", playersButtons);
        
        // Player buttons
        JButton playerOne = new MenuButton("JOUEUR 1");
        playersButtons.add(playerOne);
        
        JButton playerTwo = new MenuButton("JOUEUR 2");
        playersButtons.add(playerTwo);
        
        JButton play = new MenuButton("PLAY");
        playersButtons.add(play);
        
        //****************************************************************************************//
        //                                      ONLINE                                            //
        //****************************************************************************************//
        
        //****************************************************************************************//
        //                                      RULES                                             //
        //****************************************************************************************//
        
        // Parameter button
        gbc1.anchor = GridBagConstraints.LAST_LINE_END;
        rulesPanel.add(parameter, gbc1);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        
        add("rules", rulesPanel);
    }
}
