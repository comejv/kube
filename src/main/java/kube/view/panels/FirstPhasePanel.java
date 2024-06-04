package kube.view.panels;

// Import kube classes
import kube.configuration.Config;
import kube.controller.graphical.Phase1Controller;
import kube.model.Game;
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

// Import java classes
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class FirstPhasePanel extends JPanel {

    /**********
     * ATTRIBUTES
     **********/

    private HexGlow animationGlow;
    private Kube kube;
    private Phase1Controller controller;
    private GUI gui;
    private JPanel constructPanel, piecesPanel, gamePanel, topPanel, opponentPanel;
    private HashMap<ModelColor, JLabel> sidePanels, opponentPiecesPanel;
    private JPanel[][] mountainPanels;
    private HashMap<String, JButton> buttonsMap;
    private HashMap<ModelColor, Integer> p1Pieces;
    private JButton loadButton, validateButton;
    private int gameType;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the FirstPhasePanel
     * 
     * @param gui          the GUI object
     * @param kube         the Kube object
     * @param controller   the controller
     * @param eventsToView the queue of actions to view
     */
    public FirstPhasePanel(GUI gui, Kube kube, Phase1Controller controller, Queue<Action> eventsToView) {

        JPanel mainPanel, sidePanel, buttonsPanel;
        GridBagConstraints gbc;

        this.gui = gui;
        this.kube = kube;
        this.controller = controller;

        setLayout(new BorderLayout());
        setBackground(GUIColors.GAME_BG.toColor());
        addComponentListener(getController());

        // Create the main panel that holds other components
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBounds(0, 0, Config.INIT_WIDTH, Config.INIT_HEIGHT);
        mainPanel.setBackground(GUIColors.GAME_BG.toColor());

        sidePanel = new JPanel();
        sidePanel.setLayout(new GridBagLayout());
        sidePanel.setOpaque(false);

        // Create buttons panel and game panel
        buttonsPanel = initButtons();
        buttonsPanel.setBackground(GUIColors.GAME_BG.toColor());
        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        sidePanel.add(buttonsPanel, gbc);

        opponentsPieces();
        getOpponentPanel()
                .setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        " Pièces de l'adversaire", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Jomhuria", Font.PLAIN, 35), GUIColors.ACCENT.toColor()));

        gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 0, 20, 0);

        sidePanel.add(getOpponentPanel(), gbc);

        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 0, 10);
        mainPanel.add(sidePanel, gbc);

        setGamePanel(createGamePanel());
        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(20, 20, 20, 20);
        mainPanel.add(getGamePanel(), gbc);

        // Add main panel to the layered pane
        add(mainPanel);

        animationGlow = new HexGlow();
    }

    /**********
     * SETTERS
     **********/

    private final void setAnimationGlow(HexGlow animationGlow) {
        this.animationGlow = animationGlow;
    }

    private final void setConstructPanel(JPanel constructPanel) {
        this.constructPanel = constructPanel;
    }

    private final void setPiecesPanel(JPanel piecesPanel) {
        this.piecesPanel = piecesPanel;
    }

    private final void setGamePanel(JPanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    private final void setTopPanel(JPanel topPanel) {
        this.topPanel = topPanel;
    }

    private final void setOppenentPanel(JPanel opponentPanel) {
        this.opponentPanel = opponentPanel;
    }

    private final void setSidePanels(HashMap<ModelColor, JLabel> sidePanels) {
        this.sidePanels = sidePanels;
    }

    private final void setOppenentPiecesPanel(HashMap<ModelColor, JLabel> opponentPiecesPanel) {
        this.opponentPiecesPanel = opponentPiecesPanel;
    }

    private final void setMountainPanels(JPanel[][] mountainPanels) {
        this.mountainPanels = mountainPanels;
    }

    private final void setButtonMap(HashMap<String, JButton> buttonsMap) {
        this.buttonsMap = buttonsMap;
    }

    private final void setP1Pieces(HashMap<ModelColor, Integer> p1Pieces) {
        this.p1Pieces = p1Pieces;
    }

    /**********
     * GETTERS
     **********/

    public HexGlow getAnimationGlow() {
        return animationGlow;
    }

    public Kube getKube() {
        return kube;
    }

    public Phase1Controller getController() {
        return controller;
    }

    public GUI getGui() {
        return gui;
    }

    public JPanel getConstructPanel() {
        return constructPanel;
    }

    public JPanel getPiecesPanel() {
        return piecesPanel;
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public JPanel getOpponentPanel() {
        return opponentPanel;
    }

    public HashMap<ModelColor, JLabel> getSidePanels() {
        return sidePanels;
    }

    public HashMap<ModelColor, JLabel> getOpponentPiecesPanel() {
        return opponentPiecesPanel;
    }

    public JPanel[][] getMountainPanels() {
        return mountainPanels;
    }

    public HashMap<String, JButton> getButtonsMap() {
        return buttonsMap;
    }

    public HashMap<ModelColor, Integer> getP1Pieces() {
        return p1Pieces;
    }

    /**********
     * METHODS
     **********/

    /**
     * Reset the panel to its initial state
     * 
     * @return void
     */
    public void resetPanel() {

        JPanel mainPanel;

        // Create the main panel that holds other components
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBounds(0, 0, Config.INIT_WIDTH, Config.INIT_HEIGHT);
        mainPanel.setBackground(GUIColors.GAME_BG.toColor());

        // TODO : continue the refactoring

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridBagLayout());
        sidePanel.setOpaque(false);
        // Create buttons panel and game panel
        JPanel buttonsPanel = initButtons();
        buttonsPanel.setBackground(GUIColors.GAME_BG.toColor());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        sidePanel.add(buttonsPanel, gbc);

        opponentsPieces();
        getOpponentPanel()
                .setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        " Pièces de l'adversaire", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Jomhuria", Font.PLAIN, 35), GUIColors.ACCENT.toColor()));
        gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 0, 20, 0);

        sidePanel.add(getOpponentPanel(), gbc);

        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 0, 10);
        mainPanel.add(sidePanel, gbc);

        setGamePanel(createGamePanel());
        gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(20, 20, 20, 20);
        mainPanel.add(getGamePanel(), gbc);

        // Add main panel to the layered pane
        add(mainPanel);

        setAnimationGlow(new HexGlow());
        updateAll(true);
    }

    private JPanel createGamePanel() {
        setGamePanel(new JPanel());
        getGamePanel().setLayout(new BorderLayout());

        // TOP BAR - GAME BASE
        setTopPanel(new JPanel());
        getTopPanel().setBackground(GUIColors.GAME_BG_DARK.toColor());
        JLabel baseLabel = new JLabel("Base Centrale: ");
        baseLabel.setFont(new Font("Jomhuria", Font.PLAIN, 40));
        baseLabel.setForeground(GUIColors.TEXT.toColor());
        getTopPanel().add(baseLabel);
        for (int i = 0; i < getKube().getMountain().getBaseSize(); i++) {
            getTopPanel().add(new HexIcon(getKube().getMountain().getCase(getKube().getMountain().getBaseSize() - 1, i),
                    false, 1.5));
        }

        getGamePanel().add(getTopPanel(), BorderLayout.NORTH);
        // CENTER - CONSTRUCTION OF PLAYER MOUNTAIN
        initGrid();
        getGamePanel().add(getConstructPanel(), BorderLayout.CENTER);
        // SIDE BAR - PIECES AVAILABLE

        initSide();
        getGamePanel().add(getPiecesPanel(), BorderLayout.EAST);
        return getGamePanel();
    }

    private JPanel initButtons() {

        setButtonMap(new HashMap<>());
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridBagLayout());
        buttons.setPreferredSize(new Dimension(Config.INIT_WIDTH / 5, Config.INIT_HEIGHT / 2));
        buttons.setBackground(GUIColors.GAME_BG.toColor());

        JButton quitButton = new Buttons.GamePhaseButton("Quitter la partie");
        quitButton.setActionCommand("quit");
        quitButton.addMouseListener(getController());
        getButtonsMap().put("Quit", quitButton);
        GridBagConstraints elemGBC = new GridBagConstraints();
        elemGBC.gridy = 0;
        elemGBC.fill = GridBagConstraints.HORIZONTAL;
        buttons.add(quitButton, elemGBC);

        JButton optButton = new Buttons.GamePhaseButton("Paramètres");
        optButton.setActionCommand("settings");
        optButton.addMouseListener(getController());
        getButtonsMap().put("Option", optButton);
        elemGBC = new GridBagConstraints();
        elemGBC.gridy = 1;
        elemGBC.fill = GridBagConstraints.HORIZONTAL;
        buttons.add(optButton, elemGBC);

        JButton sugIaButton = new Buttons.GamePhaseButton("Construction auto");
        sugIaButton.setActionCommand("AI");
        sugIaButton.addMouseListener(getController());
        getButtonsMap().put("AI", sugIaButton);
        elemGBC = new GridBagConstraints();
        elemGBC.gridy = 2;
        elemGBC.fill = GridBagConstraints.HORIZONTAL;
        buttons.add(sugIaButton, elemGBC);

        validateButton = new Buttons.GamePhaseButton("Valider");
        validateButton.setEnabled(false);
        validateButton.setActionCommand("validate");
        validateButton.addMouseListener(getController());
        getButtonsMap().put("Validate", validateButton);
        elemGBC = new GridBagConstraints();
        elemGBC.gridy = 3;
        elemGBC.fill = GridBagConstraints.HORIZONTAL;
        buttons.add(validateButton, elemGBC);

        JButton saveButton = new Buttons.GamePhaseButton("Sauvegarder");
        saveButton.setEnabled(true);
        saveButton.setActionCommand("save");
        saveButton.addMouseListener(getController());
        getButtonsMap().put("Save", saveButton);
        elemGBC = new GridBagConstraints();
        elemGBC.gridy = 4;
        elemGBC.fill = GridBagConstraints.HORIZONTAL;
        buttons.add(saveButton, elemGBC);

        loadButton = new Buttons.GamePhaseButton("Charger");
        loadButton.setEnabled(false);
        loadButton.setActionCommand("load");
        loadButton.addMouseListener(getController());
        getButtonsMap().put("Load", loadButton);
        elemGBC = new GridBagConstraints();
        elemGBC.gridy = 5;
        elemGBC.fill = GridBagConstraints.HORIZONTAL;
        buttons.add(loadButton, elemGBC);

        return buttons;
    }

    public void initGrid() {
        setMountainPanels(new JPanel[6][6]);
        setConstructPanel(new JPanel());

        getConstructPanel().setOpaque(false);
        getConstructPanel().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        for (int i = 1; i <= 6; i++) {
            JPanel lineHexa = new JPanel();
            lineHexa.setLayout(new GridLayout(1, i));
            lineHexa.setOpaque(false);
            for (int j = 0; j < i; j++) {
                JPanel hexPanel = new JPanel();
                HexIcon hex = new HexIcon(getKube().getPlayerCase(getKube().getCurrentPlayer(), i - 1, j), false, 2);
                hex.setPosition(new Point(i - 1, j));
                hexPanel.add(hex);
                lineHexa.add(hexPanel);
                getMountainPanels()[i - 1][j] = hexPanel;
            }
            gbc.gridx = 0;
            gbc.gridy = i;
            // gbc.anchor = GridBagConstraints.CENTER;
            getConstructPanel().add(lineHexa, gbc);
        }
        getConstructPanel().revalidate();
        getConstructPanel().repaint();
    }

    public void opponentsPieces() {
        setOppenentPanel(new JPanel());
        setOppenentPiecesPanel(new HashMap<>());
        getOpponentPanel().setBackground(GUIColors.TEXT_HOVER.toColor());
        getOpponentPanel().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        int x = 0, y = 0;
        JPanel mini;
        JLabel numOfPieces;
        for (ModelColor c : ModelColor.getAllColored()) {
            mini = new JPanel();
            mini.setOpaque(false);
            numOfPieces = new JLabel("x0");
            numOfPieces.setFont(new Font("Jomhuria", Font.PLAIN, 40));
            mini.add(new HexIcon(c, false, 1.5));
            mini.add(numOfPieces);
            gbc.gridx = x;
            gbc.gridy = y;
            x++;
            if (x > 1) {
                x = 0;
                y++;
            }
            getOpponentPanel().add(mini, gbc);
            getOpponentPiecesPanel().put(c, numOfPieces); // add to hashmap for later update
        }

        // Jokers
        JPanel jokers = new JPanel();
        jokers.setLayout(new GridBagLayout());
        jokers.setOpaque(false);
        jokers.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Jokers", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Jomhuria", Font.PLAIN, 50), GUIColors.ACCENT.toColor()));

        gbc = new GridBagConstraints();

        // White
        mini = new JPanel();
        mini.setOpaque(false);
        numOfPieces = new JLabel("x0");
        numOfPieces.setFont(new Font("Jomhuria", Font.PLAIN, 40));
        mini.add(new HexIcon(ModelColor.WHITE, false, 1.5));
        mini.add(numOfPieces);
        gbc.gridx = 0;
        gbc.gridy = 0;
        jokers.add(mini, gbc);
        getOpponentPiecesPanel().put(ModelColor.WHITE, numOfPieces); // add to hashmap for later update

        // Natural
        mini = new JPanel();
        mini.setOpaque(false);
        numOfPieces = new JLabel("x0");
        numOfPieces.setFont(new Font("Jomhuria", Font.PLAIN, 40));
        mini.add(new HexIcon(ModelColor.NATURAL, false, 1.5));
        mini.add(numOfPieces);
        gbc.gridx = 1;
        gbc.gridy = 0;
        jokers.add(mini, gbc);
        getOpponentPiecesPanel().put(ModelColor.NATURAL, numOfPieces); // add to hashmap for later update

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        getOpponentPanel().add(jokers, gbc);

        getOpponentPanel().revalidate();
        getOpponentPanel().repaint();
    }

    public void initSide() {

        setPiecesPanel(new JPanel());
        setSidePanels(new HashMap<>());
        getPiecesPanel().setBackground(GUIColors.TEXT_HOVER.toColor());
        getPiecesPanel().setLayout(new GridBagLayout());
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
            numberOfPieces = getKube().getCurrentPlayer().getAvailableToBuild().get(c);
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
            getPiecesPanel().add(mini, gbc);
            getSidePanels().put(c, numOfPieces); // add to hashmap for later update
        }

        // Jokers
        JPanel jokers = new JPanel();
        jokers.setLayout(new GridBagLayout());
        jokers.setOpaque(false);
        jokers.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Jokers", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Jomhuria", Font.PLAIN, 50), GUIColors.ACCENT.toColor()));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between elements

        // White
        JPanel whitePanel = createHexPanel(ModelColor.WHITE,
                "<html>Permet de passer son tour en le jouant<br>sur le côté de la montagne commune.</html>");
        gbc.gridx = 0;
        gbc.gridy = 0;
        jokers.add(whitePanel, gbc);
        getSidePanels().put(ModelColor.WHITE, (JLabel) whitePanel.getComponent(1));

        // Natural
        JPanel naturalPanel = createHexPanel(ModelColor.NATURAL,
                "<html>Peut être posé n'importe où et sert<br> de base à n'importe quelle pièce.</html>");
        gbc.gridx = 1;
        gbc.gridy = 0;
        jokers.add(naturalPanel, gbc);
        getSidePanels().put(ModelColor.NATURAL, (JLabel) naturalPanel.getComponent(1));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        getPiecesPanel().add(jokers, gbc);

        getPiecesPanel().revalidate();
        getPiecesPanel().repaint();
    }

    private JPanel createHexPanel(ModelColor color, String tooltipText) {
        JPanel mini = new JPanel();
        mini.setOpaque(false);
        mini.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Ensure components are laid out properly

        JLabel numOfPieces = new JLabel("x0");
        numOfPieces.setFont(new Font("Jomhuria", Font.PLAIN, 40));

        HexIcon hexIcon = new HexIcon(color, false, 1.5);
        hexIcon.setToolTipText(tooltipText); // Set the tooltip text

        mini.add(hexIcon);
        mini.add(numOfPieces);

        // Optionally add a mouse listener for additional context menu features
        mini.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Show tooltip or context menu on hover
                mini.setToolTipText(tooltipText);
            }
        });

        return mini;
    }

    public void updateButton() {
        getButtonsMap().get("AI").setEnabled(true);
        validateButton.setEnabled(getKube().getCurrentPlayer().isMountainFull());
        loadButton.setEnabled(gameType == Game.LOCAL);
    }

    public void updateGrid(int i, int j) {
        updateGrid(new Point(i, j));
    }

    public void updateGrid(Point pos) {
        ModelColor c = getKube().getPlayerCase(getKube().getCurrentPlayer(), pos.x, pos.y);
        boolean actionable = c != ModelColor.EMPTY;
        HexIcon hex = new HexIcon(c, actionable, 2);
        hex.setPosition(pos);
        JPanel panel = getMountainPanels()[pos.x][pos.y];
        panel.removeAll();
        panel.add(hex);
        panel.revalidate();
        panel.repaint();
    }

    public void updateSide(ModelColor c) {
        JLabel lab = getSidePanels().get(c);
        int numberOfPieces = getKube().getCurrentPlayer().getAvailableToBuild().get(c);
        lab.setText("x" + numberOfPieces);
        lab.repaint();
    }

    public void updateOpponent() {
        if (getKube().getCurrentPlayer() == getKube().getP1()) {
            setP1Pieces(new HashMap<>(getKube().getP1().getAvailableToBuild()));
            for (int i = 0; i < getKube().getP1().getMountain().getBaseSize(); i++) {
                for (int j = 0; j < i + 1; j++) {
                    ModelColor c = getKube().getP1().getMountain().getCase(i, j);
                    if (c != ModelColor.EMPTY) {
                        getP1Pieces().put(c, getP1Pieces().get(c) + 1);
                    }
                }
            }
        }
        if (getP1Pieces() == null) {
            setP1Pieces(new HashMap<>(getKube().getP1().getAvailableToBuild()));
            getTopPanel().removeAll();
            JLabel baseLabel = new JLabel("Base Centrale: ");
            baseLabel.setFont(new Font("Jomhuria", Font.PLAIN, 30));
            baseLabel.setForeground(GUIColors.TEXT.toColor());
            getTopPanel().add(baseLabel);
            for (int i = 0; i < getKube().getMountain().getBaseSize(); i++) {
                getTopPanel().add(new HexIcon(
                        getKube().getMountain().getCase(getKube().getMountain().getBaseSize() - 1, i), false, 1.5));
            }
        }
        for (ModelColor c : ModelColor.getAllColoredAndJokers()) {
            JLabel label = getOpponentPiecesPanel().get(c);
            int numberOfPieces = 0;
            if (getKube().getCurrentPlayer() == getKube().getP1()) {
                numberOfPieces = getKube().getP2().getAvailableToBuild().get(c);
            } else {
                numberOfPieces = getP1Pieces().get(c);
            }
            label.setText("x" + numberOfPieces);
            label.repaint();
        }

    }

    public void updateAll(Boolean firstUpdate) {
        gameType = kube.getGameType();
        getButtonsMap().get("AI").setEnabled(false);
        getButtonsMap().get("Validate").setEnabled(false);
        updateOpponent();
        if (firstUpdate) {
            if (getKube().getCurrentPlayer() == getKube().getP1()) {
                getTopPanel().removeAll();
                JLabel baseLabel = new JLabel("Base Centrale: ");
                baseLabel.setFont(new Font("Jomhuria", Font.PLAIN, 30));
                baseLabel.setForeground(GUIColors.TEXT.toColor());
                getTopPanel().add(baseLabel);
                for (int i = 0; i < getKube().getMountain().getBaseSize(); i++) {
                    getTopPanel().add(new HexIcon(
                            getKube().getMountain().getCase(getKube().getMountain().getBaseSize() - 1, i), false, 1.5));
                }
            }
        }
        getConstructPanel().setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "Au tour de " + getKube().getCurrentPlayer().getName() + " de construire sa montagne",
                        TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Jomhuria", Font.PLAIN, 60), GUIColors.ACCENT.toColor()));

        for (ModelColor c : ModelColor.getAllColoredAndJokers()) {
            updateSide(c);
        }
        for (int i = 0; i < getKube().getCurrentPlayer().getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                updateGrid(i, j);
            }
        }
        updateButton();
        updateActionnable();
    }

    public void update(Action a) {
        gameType = kube.getGameType();
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
                for (int i = 0; i < getKube().getCurrentPlayer().getMountain().getBaseSize(); i++) {
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
        for (JButton b : getButtonsMap().values()) {
            b.setEnabled(false);
        }
        getButtonsMap().get("Validate").setText("Validation en cours");
    }

    public void resetButtonValue() {
        getButtonsMap().get("Quit").setText("Quitter la partie");
        getButtonsMap().get("Quit").setEnabled(true);
        getButtonsMap().get("Option").setText("Paramètres");
        getButtonsMap().get("Option").setEnabled(true);
        getButtonsMap().get("AI").setText("Construction auto");
        getButtonsMap().get("AI").setEnabled(true);
        getButtonsMap().get("Validate").setText("Valider");
        getButtonsMap().get("Validate").setEnabled(false);
        getButtonsMap().get("Save").setText("Sauvegarder");
        getButtonsMap().get("Save").setEnabled(true);
    }

    // TODO : remove ?
    public void updateDnd(Action a) {

    }

    public void updateActionnable() {
        ArrayList<HexIcon> toGlow = new ArrayList<>();
        for (JLabel pan : getSidePanels().values()) {
            HexIcon hex = (HexIcon) pan.getParent().getComponent(0);
            int numberOfPieces = getKube().getCurrentPlayer().getAvailableToBuild().get(hex.getColor());
            if (numberOfPieces > 0) {
                hex.setActionable(true);
                toGlow.add(hex);
            } else {
                hex.setActionable(false);
            }
        }
        getAnimationGlow().setToRedraw(toGlow);
    }

    public void updateHexSize() {
        Dimension newSize = this.getSize();
        // if (isSignificantChange(oldSize, newSize)) {
        // Update the static size of HexIcon based on new size
        int newHexSize = calculateNewHexSize(newSize);
        HexIcon.setStaticSize(newHexSize);

        JPanel panel;
        HexIcon h;
        // Loop through panels and update hex size
        for (int i = 0; i < getKube().getCurrentPlayer().getMountain().getBaseSize(); i++) {
            for (int j = 0; j < i + 1; j++) {
                panel = getMountainPanels()[i][j];
                h = (HexIcon) panel.getComponents()[0];
                h.updateSize();
                panel.removeAll();
                panel.add(h);
            }
        }

        for (JLabel pan : getSidePanels().values()) {
            JPanel p = (JPanel) pan.getParent();
            h = (HexIcon) p.getComponents()[0];
            JLabel lab = (JLabel) p.getComponents()[1];
            p.removeAll();
            h.updateSize();
            p.add(h);
            p.add(lab);
        }

        for (JLabel pan : getOpponentPiecesPanel().values()) {
            JPanel p = (JPanel) pan.getParent();
            h = (HexIcon) p.getComponents()[0];
            JLabel lab = (JLabel) p.getComponents()[1];
            p.removeAll();
            h.updateSize();
            p.add(h);
            p.add(lab);
        }

        // Update the old size to the new size

        revalidate();
        repaint();
        getGui().getOverlay().repaint();
    }

    // private boolean isSignificantChange(Dimension oldSize, Dimension newSize) {
    // int threshold = 1;
    // return Math.abs(newSize.height - oldSize.height) > threshold;
    // }

    private int calculateNewHexSize(Dimension newSize) {
        double scaleFactor = newSize.getHeight() / (double) Config.INIT_HEIGHT;
        int newHexSize = (int) (40 * scaleFactor);
        if (newHexSize == 0) {
            Config.error("Tried resizing hexa to 0 width");
            return 20;
        }
        return Math.max(newHexSize, 20);
    }

    public void buildMessage() {
        TransparentPanel transparentPanel = new TransparentPanel("");
        transparentPanel.setPreferredSize(getGui().getMainFrame().getSize());
        transparentPanel.setVisible(false);
        getGui().addToOverlay(transparentPanel);
        if (k3.getGameType() == Game.LOCAL) {
            new Message(transparentPanel,
                    getKube().getCurrentPlayer().getName() + " preparez votre montagne !",
                    getGui(),
                    getAnimationGlow(),
                    getKube().getCurrentPlayer() == getKube().getP1(), false);
        } else {
            new Message(transparentPanel,
                    "Construisez votre montagne !",
                    getGui(),
                    getAnimationGlow(),
                    getKube().getCurrentPlayer() == getKube().getP1(), false);
        }
    }

}
