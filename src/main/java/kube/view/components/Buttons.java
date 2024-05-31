package kube.view.components;
import kube.configuration.Config;
import kube.view.GUIColors;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

/*
 * This class will have subclasses for all the buttons used.
 */
public class Buttons {

    // TODO : refactor this class to make it more readable
    public static class MenuButton extends JButton {
        public MenuButton(String name) {
            super(name);

            setPreferredSize(new Dimension(Config.INIT_WIDTH / 3,
                    Config.INIT_WIDTH / 15));
            setBackground(GUIColors.ACCENT.toColor());
            setForeground(GUIColors.TEXT.toColor());

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.INIT_HEIGHT / 10)));
        }
    }

    // TODO : maybe not in buttons ?
    public static class SelectPlayerButton extends JPanel {
        public SelectPlayerButton(String name) {
            setLayout(new GridBagLayout());
            setPreferredSize(new Dimension(Config.INIT_WIDTH / 2,
                    Config.INIT_WIDTH / 12));
            setBackground(GUIColors.ACCENT.toColor());
            GridBagConstraints gbc = new GridBagConstraints();
            int size = Config.INIT_HEIGHT / 10;
            int sizeDisplay = Config.INIT_HEIGHT / 20;

            JLabel nameLabel = new JLabel();
            nameLabel.setText(name);
            nameLabel.setForeground(GUIColors.TEXT.toColor());
            nameLabel.setFont(new Font("Jomhuria", Font.BOLD,size));
            gbc.insets= new Insets(5,2,5,25);
            add( nameLabel,gbc);

            String[] players = {"Humain", "IA Facile", "IA Moyenne","IA Difficile"};
            final int[] currentIndex = {0};
            JPanel selectComp = new JPanel();
            selectComp.setLayout(new BorderLayout());

            // Create buttons and label
            JButton leftButton = new JButton("<");
            leftButton.setFont(new Font("Jomhuria", Font.PLAIN, sizeDisplay));
            leftButton.setForeground(GUIColors.ACCENT.toColor());

            leftButton.setFocusPainted(false);  // Remove focus border
            leftButton.setBorder(BorderFactory.createEmptyBorder());  // Remove button border
            leftButton.setContentAreaFilled(false);
            leftButton.setFocusPainted(false); // Remove focus border
            JButton rightButton = new JButton(">");
            rightButton.setFont(new Font("Jomhuria", Font.PLAIN,sizeDisplay));
            rightButton.setForeground(GUIColors.ACCENT.toColor());

            rightButton.setFocusPainted(false);  // Remove focus border
            rightButton.setBorder(BorderFactory.createEmptyBorder());  // Remove button border
            rightButton.setContentAreaFilled(false);
            rightButton.setFocusPainted(false); // Remove focus border
            JLabel displayLabel = new JLabel(players[currentIndex[0]], JLabel.CENTER);
            displayLabel.setFont(new Font("Jomhuria", Font.PLAIN, sizeDisplay));
            displayLabel.setForeground(GUIColors.ACCENT.toColor());
            displayLabel.setPreferredSize(new Dimension(150, 50)); // Set fixed width for display label

            // Add buttons and label to the panel
            selectComp.add(leftButton, BorderLayout.WEST);
            selectComp.add(displayLabel, BorderLayout.CENTER);
            selectComp.add(rightButton, BorderLayout.EAST);

            // Set button actions
            leftButton.addActionListener(e -> {
                currentIndex[0] = (currentIndex[0] - 1 + players.length) % players.length;
                displayLabel.setText(players[currentIndex[0]]);
            });

            rightButton.addActionListener(e -> {
                currentIndex[0] = (currentIndex[0] + 1) % players.length;
                displayLabel.setText(players[currentIndex[0]]);
            });

            selectComp.setMaximumSize(new Dimension(150, 40));
            selectComp.setMinimumSize(new Dimension(150, 40));

            gbc.insets= new Insets(5,100,5,2);
            add(selectComp,gbc);

        }

    }

    public static class GameFirstPhaseButton extends JButton {
        public GameFirstPhaseButton(String name) {
            super(name);
            setPreferredSize(new Dimension(200, 50));
            setBackground(GUIColors.ACCENT.toColor());
            setForeground(GUIColors.TEXT.toColor());
            setFont(new Font("Jomhuria", Font.PLAIN, (int) (Config.INIT_HEIGHT / 20)));
        }
    }

    public static class GameSecondPhaseButton extends JButton {
        public GameSecondPhaseButton(String name) {
            super(name);
            setPreferredSize(new Dimension(300, 100));
            setBackground(GUIColors.ACCENT.toColor());
            setForeground(GUIColors.TEXT.toColor());
            setFont(new Font("Jomhuria", Font.PLAIN, (int) (Config.INIT_HEIGHT / 20)));
        }
    }

    public static class ParameterButton extends JButton {
        public ParameterButton(Image image) {
            super();
            Image scaledImg = image.getScaledInstance(
                    (int) (0.15 * Config.INIT_HEIGHT),
                    (int) (0.15 * Config.INIT_HEIGHT),
                    Image.SCALE_SMOOTH);

            ImageIcon icon = new ImageIcon(scaledImg);
            setIcon(icon);

            setPreferredSize(new Dimension((int) (0.20 * Config.INIT_HEIGHT),
                    (int) (0.20 * Config.INIT_HEIGHT)));

            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }
    }

    public static class RulesButton extends JButton {
        public RulesButton(String name) {
            super(name);
            setPreferredSize(new Dimension(200, 50));
            setBackground(GUIColors.ACCENT.toColor());

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.INIT_HEIGHT / 15)));
        }
    }

    public static class InGameMenuButton extends JButton {
        public InGameMenuButton(String name) {
            super(name);
            setPreferredSize(new Dimension(Config.INIT_WIDTH / 4, Config.INIT_HEIGHT / 8));
            setBackground(GUIColors.ACCENT.toColor());

            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.INIT_HEIGHT / 15)));
        }
    }

    public static class ButtonIcon extends Icon {
        private final String name;
        private boolean isHovered = false;
        private boolean isPressed = false;

        public ButtonIcon(String name, BufferedImage image, ActionListener mouseListener) {
            super(image);
            this.name = name;

            // Apply the provided MouseListener
            addMouseListener((MouseListener) mouseListener);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Darken the image on hover
            float factor = isHovered ? (isPressed ? 0.75f : 1.25f) : 1.0f;

            if (isHovered) { // Draw darker image
                float[] scales = { factor };
                float[] offsets = new float[4];
                RescaleOp rop = new RescaleOp(scales, offsets, null);
                g2d.drawImage(getImage(), rop, 0, 0);
            } else { // Draw the original image
                g2d.drawImage(getImage(), 0, 0, null);
            }

        }

        public void setHovered(boolean b) {
            isHovered = b;
            repaint();
        }

        public void setPressed(boolean b) {
            isPressed = b;
            repaint();
        }

        public void setDefault() {
            isHovered = false;
            isPressed = false;
            repaint();
        }

        public String getName() {
            return name;
        }

    }
}
