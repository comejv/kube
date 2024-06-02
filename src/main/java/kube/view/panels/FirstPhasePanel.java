package kube.view.panels;

import kube.configuration.Config;
import kube.controller.graphical.Phase1Controller;
import kube.model.Kube;
import kube.model.ModelColor;
import kube.model.action.Queue;
import kube.model.action.Remove;
import kube.model.action.Swap;
import kube.model.action.Action;
import kube.model.action.Build;
import kube.view.GUI;
import kube.view.GUIColors;
import kube.view.animations.HexGlow;
import kube.view.animations.Message;
import kube.view.components.Buttons;
import kube.view.components.HexIcon;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/*
 * This class extends JPanel and creates the GUI for the first phase of the game.
 */
public class FirstPhasePanel extends JPanel {
    private HexGlow animationGlow;
    private Kube k3;
    private Phase1Controller controller;
    private GUI gui;
    private JPanel constructPanel, piecesPanel, gamePanel, topPanel;
    private HashMap<ModelColor, JLabel> sidePanels;
    private JPanel[][] moutainPanels; // TODO : rename symbol to fix typo
    private HashMap<String, JButton> buttonsMap;
    private Dimension oldSize;
    private Queue<Action> eventsToModel;

    public FirstPhasePanel(GUI gui, Kube k3, Phase1Controller controller, Queue<Action> eventsToView,
            Queue<Action> eventsToModel) {
        this.eventsToModel = eventsToModel;
        this.gui = gui;
        this.k3 = k3;
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(GUIColors.GAME_BG.toColor());
        addComponentListener(controller);

        // Create the main panel that holds other components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBounds(0, 0, Config.INIT_WIDTH, Config.INIT_HEIGHT);
        mainPanel.setBackground(GUIColors.GAME_BG.toColor());
        // Create buttons panel and game panel
        JPanel buttonsPanel = initButtons();
        buttonsPanel.setBackground(GUIColors.GAME_BG.toColor());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(0, 10, 0, 10);
        mainPanel.add(buttonsPanel, gbc);

        JPanel gamePanel = createGamePanel();
        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(20, 20, 20, 20);
        mainPanel.add(gamePanel, gbc);

        // Add main panel to the layered pane
        add(mainPanel);

        animationGlow = new HexGlow();
        this.oldSize = getSize();
    }

    private JPanel createGamePanel() {
        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());

        // TOP BAR - GAME BASE
        topPanel = new JPanel();
        topPanel.setBackground(GUIColors.GAME_BG_DARK.toColor());
        JLabel baseLabel = new JLabel("Base Centrale: ");
        baseLabel.setFont(new Font("Jomhuria", Font.PLAIN, 40));
        baseLabel.setForeground(GUIColors.TEXT.toColor());
        topPanel.add(baseLabel);
        for (int i = 0; i < k3.getK3().getBaseSize(); i++) {
            topPanel.add(new HexIcon(k3.getK3().getCase(k3.getK3().getBaseSize() - 1, i), false, 1.5));
        }

        gamePanel.add(topPanel, BorderLayout.NORTH);
        // CENTER - CONSTRUCTION OF PLAYER MOUNTAIN
        initGrid();
        gamePanel.add(constructPanel, BorderLayout.CENTER);
        // SIDE BAR - PIECES AVAILABLE
        initSide();
        gamePanel.add(piecesPanel, BorderLayout.EAST);
        return gamePanel;
    }

    private JPanel initButtons() {
        buttonsMap = new HashMap<>();
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridBagLayout());
        buttons.setPreferredSize(new Dimension(Config.INIT_WIDTH / 5, Config.INIT_HEIGHT / 2));
        buttons.setBackground(GUIColors.GAME_BG.toColor());

        JButton quitButton = new Buttons.GamePhaseButton("Quitter la partie");
        quitButton.setActionCommand("quit");
        quitButton.addMouseListener(controller);
        buttonsMap.put("Quit", quitButton);
        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.gridy = 0;
        elemGBC.fill = GridBagConstraints.HORIZONTAL;
        buttons.add(quitButton, elemGBC);

        JButton optButton = new Buttons.GamePhaseButton("Paramètres");
        optButton.setActionCommand("settings");
        optButton.addMouseListener(controller);
        buttonsMap.put("Option", optButton);
        elemGBC = new GridBagConstraints();
        elemGBC.gridy = 1;
        elemGBC.fill = GridBagConstraints.HORIZONTAL;
        buttons.add(optButton, elemGBC);

        JButton sugIaButton = new Buttons.GamePhaseButton("Construction auto");
        sugIaButton.setActionCommand("AI");
        sugIaButton.addMouseListener(controller);
        buttonsMap.put("AI", sugIaButton);
        elemGBC = new GridBagConstraints();
        elemGBC.gridy = 2;
        elemGBC.fill = GridBagConstraints.HORIZONTAL;
        buttons.add(sugIaButton, elemGBC);

        JButton validerButton = new Buttons.GamePhaseButton("Valider");
        validerButton.setEnabled(false);
        validerButton.setActionCommand("validate");
        validerButton.addMouseListener(controller);
        buttonsMap.put("Validate", validerButton);
        elemGBC = new GridBagConstraints();
        elemGBC.gridy = 3;
        elemGBC.fill = GridBagConstraints.HORIZONTAL;
        buttons.add(validerButton, elemGBC);

        return buttons;
    }

    public void initGrid() {
        moutainPanels = new JPanel[6][6];
        constructPanel = new JPanel();

        constructPanel.setOpaque(false);
        constructPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        for (int i = 1; i <= 6; i++) {
            JPanel lineHexa = new JPanel();
            lineHexa.setLayout(new GridLayout(1, i));
            lineHexa.setOpaque(false);
            for (int j = 0; j < i; j++) {
                JPanel hexPanel = new JPanel();
                HexIcon hex = new HexIcon(k3.getPlayerCase(k3.getCurrentPlayer(), i - 1, j), false, 2);
                hex.setPosition(new Point(i - 1, j));
                hexPanel.add(hex);
                lineHexa.add(hexPanel);
                moutainPanels[i - 1][j] = hexPanel;
            }
            gbc.gridx = 0;
            gbc.gridy = i;
            // gbc.anchor = GridBagConstraints.CENTER;
            constructPanel.add(lineHexa, gbc);
        }
        constructPanel.revalidate();
        constructPanel.repaint();
    }

    public void initSide() {
        sidePanels = new HashMap<>();
        piecesPanel = new JPanel();
        piecesPanel.setBackground(GUIColors.TEXT_HOVER.toColor());
        piecesPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        int x = 0, y = 0, numberOfPieces;
        boolean actionable;
        JPanel mini;
        JLabel numOfPieces;
        for (ModelColor c : ModelColor.getAllColored()) {
            mini = new JPanel();
            mini.setOpaque(false);
            numberOfPieces = k3.getCurrentPlayer().getAvailableToBuild().get(c);
            numOfPieces = new JLabel("x" + numberOfPieces);
            numOfPieces.setFont(new Font("Jomhuria", Font.PLAIN, 40));
            actionable = numberOfPieces > 0;
            mini.add(new HexIcon(c, actionable, 1.5));
            mini.add(numOfPieces);
            gbc.gridx = x;
            gbc.gridy = y;
            x++;
            if (x > 1) {
                x = 0;
                y++;
            }
            piecesPanel.add(mini, gbc);
            sidePanels.put(c, numOfPieces); // add to hashmap for later update
        }
        JPanel jokers = new JPanel();
        jokers.setOpaque(false);
        jokers.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Jokers", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Jomhuria", Font.PLAIN, 50), GUIColors.ACCENT.toColor()));

        // White
        mini = new JPanel();
        mini.setOpaque(false);
        numberOfPieces = k3.getCurrentPlayer().getAvailableToBuild().get(ModelColor.WHITE);
        numOfPieces = new JLabel("x" + numberOfPieces);
        numOfPieces.setFont(new Font("Jomhuria", Font.PLAIN, 40));
        actionable = numberOfPieces > 0;
        mini.add(new HexIcon(ModelColor.WHITE, actionable, 1.5));
        mini.add(numOfPieces);
        jokers.add(mini);
        sidePanels.put(ModelColor.WHITE, numOfPieces); // add to hashmap for later update

        // White
        mini = new JPanel();
        mini.setOpaque(false);
        numberOfPieces = k3.getCurrentPlayer().getAvailableToBuild().get(ModelColor.NATURAL);
        numOfPieces = new JLabel("x" + numberOfPieces);
        numOfPieces.setFont(new Font("Jomhuria", Font.PLAIN, 40));
        actionable = numberOfPieces > 0;
        mini.add(new HexIcon(ModelColor.NATURAL, actionable, 1.5));
        mini.add(numOfPieces);
        jokers.add(mini);
        sidePanels.put(ModelColor.NATURAL, numOfPieces); // add to hashmap for later update

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        piecesPanel.add(jokers, gbc);

        piecesPanel.revalidate();
        piecesPanel.repaint();
    }

    public void updateButton() {
        JButton validateButton = buttonsMap.get("Validate");
        if (k3.getCurrentPlayer().isMountainFull()) {
            validateButton.setEnabled(true);
        } else {
            validateButton.setEnabled(false);
        }
    }

    public void updateGrid(int i, int j) {
        updateGrid(new Point(i, j));
    }

    public void updateGrid(Point pos) {
        ModelColor c = k3.getPlayerCase(k3.getCurrentPlayer(), pos.x, pos.y);
        boolean actionable = c != ModelColor.EMPTY;
        HexIcon hex = new HexIcon(c, actionable, 2);
        hex.setPosition(pos);
        JPanel panel = moutainPanels[pos.x][pos.y];
        panel.removeAll();
        panel.add(hex);
        panel.revalidate();
        panel.repaint();
    }

    public void updateSide(ModelColor c) {
        JLabel lab = sidePanels.get(c);
        int numberOfPieces = k3.getCurrentPlayer().getAvailableToBuild().get(c);
        lab.setText("x" + numberOfPieces);
        lab.repaint();
    }

    public void updateAll(Boolean firstUpdate) {
        if (firstUpdate) {
            if (k3.getCurrentPlayer() == k3.getP1()) {
                topPanel.removeAll();
                JLabel baseLabel = new JLabel("Base Centrale: ");
                baseLabel.setFont(new Font("Jomhuria", Font.PLAIN, 30));
                baseLabel.setForeground(GUIColors.TEXT.toColor());
                topPanel.add(baseLabel);
                for (int i = 0; i < k3.getK3().getBaseSize(); i++) {
                    topPanel.add(new HexIcon(k3.getK3().getCase(k3.getK3().getBaseSize() - 1, i), false, 1.5));
                }
            }
        }

        constructPanel.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Au tour de " + k3.getCurrentPlayer().getName() + " de construire sa montagne",
                        TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Jomhuria", Font.PLAIN, 60), GUIColors.ACCENT.toColor()));
        buttonsMap.get("AI").setEnabled(false);
        buttonsMap.get("Validate").setEnabled(false);
        for (ModelColor c : ModelColor.getAllColoredAndJokers()) {
            updateSide(c);
        }
        for (int i = 0; i < k3.getCurrentPlayer().getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                updateGrid(i, j);
            }
        }
        for (ModelColor c : ModelColor.getAllColoredAndJokers()) {
            updateSide(c);
        }
        buttonsMap.get("AI").setEnabled(true);
        updateButton();
        updateActionnable();
    }

    public void update(Action a) {
        switch (a.getType()) {
            case BUILD:
                Build b = (Build) a.getData();
                updateGrid(b.getPosition());
                updateSide(b.getModelColor());
                if (b.getOldColor() != null && b.getOldColor() != ModelColor.EMPTY) {
                    updateSide(b.getOldColor());
                }
                break;
            case REMOVE:
                Remove r = (Remove) a.getData();
                updateGrid(r.getPosition());
                updateSide(r.getModelColor());
                break;
            case SWAP:
                Swap s = (Swap) a.getData();
                updateGrid(s.getFrom());
                updateGrid(s.getTo());
                break;
            case AI_MOVE:
                updateActionnable();
                for (ModelColor c : ModelColor.getAllColoredAndJokers()) {
                    updateSide(c);
                }
                for (int i = 0; i < k3.getCurrentPlayer().getMountain().getBaseSize(); i++) {
                    for (int j = 0; j < i + 1; j++) {
                        updateGrid(i, j);
                    }
                }
                break;
            default:
                break;
        }
        updateActionnable();
        updateButton();
    }

    public void setWaitingButton() {
        for (JButton b : buttonsMap.values()) {
            b.setEnabled(false);
        }
        buttonsMap.get("Validate").setText("Validation en cours");
    }

    public void resetButtonValue() {
        buttonsMap.get("Quit").setText("Quitter la partie");
        buttonsMap.get("Quit").setEnabled(true);
        buttonsMap.get("Option").setText("Paramètres");
        buttonsMap.get("Option").setEnabled(true);
        buttonsMap.get("AI").setText("Suggestion IA");
        buttonsMap.get("AI").setEnabled(true);
        buttonsMap.get("Validate").setText("Valider");
        buttonsMap.get("Validate").setEnabled(false);
    }

    // TODO : remove ?
    public void updateDnd(Action a) {

    }

    public void updateActionnable() {
        ArrayList<HexIcon> toGlow = new ArrayList<>();
        for (JLabel pan : sidePanels.values()) {
            HexIcon hex = (HexIcon) pan.getParent().getComponent(0);
            int numberOfPieces = k3.getCurrentPlayer().getAvailableToBuild().get(hex.getColor());
            if (numberOfPieces > 0) {
                hex.setActionable(true);
                toGlow.add(hex);
            } else {
                hex.setActionable(false);
            }
        }
        animationGlow.setToRedraw(toGlow);
    }

    public void updateHexSize() {
        Dimension newSize = this.getSize();
        if (isSignificantChange(oldSize, newSize)) {
            // Update the static size of HexIcon based on new size
            int newHexSize = calculateNewHexSize(newSize);
            HexIcon.setStaticSize(newHexSize);

            JPanel panel;
            HexIcon h;
            // Loop through panels and update hex size
            for (int i = 0; i < k3.getCurrentPlayer().getMountain().getBaseSize(); i++) {
                for (int j = 0; j < i + 1; j++) {
                    panel = moutainPanels[i][j];
                    h = (HexIcon) panel.getComponents()[0];
                    h.updateSize();
                    // panel.setPreferredSize(h.getSize());
                }
            }

            // Update the old size to the new size
            oldSize = newSize;
            constructPanel.revalidate();
            constructPanel.repaint();
        }
    }

    private boolean isSignificantChange(Dimension oldSize, Dimension newSize) {
        int threshold = 100;
        return Math.abs(newSize.height - oldSize.height) > threshold;
    }

    private int calculateNewHexSize(Dimension newSize) {
        double scaleFactor = newSize.getHeight() / (double) Config.INIT_HEIGHT;
        int newHexSize = (int) (40 * scaleFactor);
        return newHexSize;
    }

    public void buildMessage() {
        TransparentPanel transparentPanel = new TransparentPanel("");
        transparentPanel.setPreferredSize(gui.getMainFrame().getSize());
        transparentPanel.setVisible(false);
        gui.addToOverlay(transparentPanel);
        new Message(transparentPanel,
                k3.getCurrentPlayer().getName() + " preparez votre montagne !",
                gui,
                animationGlow,
                k3.getCurrentPlayer() == k3.getP1());
    }

    public void winMessage(Action a) {
        TransparentPanel transparentPanel = new TransparentPanel("");
        transparentPanel.setPreferredSize(gui.getMainFrame().getSize());
        transparentPanel.setVisible(false);
        gui.addToOverlay(transparentPanel);
        new Message(transparentPanel,
                k3.getCurrentPlayer().getName() + " preparez votre montagne !",
                gui,
                animationGlow);
    }
}
