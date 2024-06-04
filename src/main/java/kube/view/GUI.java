package kube.view;

// Import Java classes
import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.controller.graphical.Phase1DnD;
import kube.controller.graphical.Phase2DnD;
import kube.controller.graphical.GUIControllers;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.animations.HexGlow;
import kube.view.animations.PanelGlow;
import kube.view.panels.*;

// Import Java classes
import java.io.IOException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.*;
import javax.swing.UnsupportedLookAndFeelException;

public class GUI implements Runnable {

    /**********
     * CONSTANTS
     **********/

    public final static String MENU = "MENU";
    public final static String PHASE1 = "PHASE1";
    public final static String PHASE2 = "PHASE2";

    /**********
     * ATTRIBUTES
     **********/

    private volatile SecondPhasePanel secondPhasePanel;
    private volatile FirstPhasePanel firstPhasePanel;

    private MainFrame mF;
    private GUIControllers controllers;
    private Kube k3;
    private Thread loaderThread;
    public Queue<Action> eventsToView, eventsToModel;
    public MenuPanel mP;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor for the GUI class
     * 
     * @param k3            the Kube object
     * @param controllers   the GUIControllers object
     * @param eventsToView  the Queue of Action to send to the view
     * @param eventsToModel the Queue of Action to send to the model
     */
    public GUI(Kube k3, GUIControllers controllers, Queue<Action> eventsToView, Queue<Action> eventsToModel) {

        boolean nimbusFound;
        GraphicsEnvironment ge;
        Font font;

        this.eventsToView = eventsToView;
        this.eventsToModel = eventsToModel;
        this.controllers = controllers;
        this.k3 = k3;

        try {
            nimbusFound = false;

            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    Config.debug("Set Look and Feel to Nimbus.");
                    nimbusFound = true;
                    break;
                }
            }

            if (!nimbusFound) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Config.debug("Set Look and Feel to system.");
            }
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException
                | InstantiationException | IllegalAccessException e) {
            Config.error("Can't set look and feel : " + e);
        }

        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            font = Font.createFont(Font.TRUETYPE_FONT,
                    ResourceLoader.getResourceAsStream("fonts/Jomhuria-Regular.ttf"));
            ge.registerFont(font);
            ge.getAvailableFontFamilyNames();
        } catch (IOException | FontFormatException e) {
            Config.error("Could not load buttons font, using default.");
        }

        new Thread(new GUIEventsHandler(this, eventsToView, eventsToModel)).start();

        SwingUtilities.invokeLater(this);
    }

    /**********
     * SETTERS
     **********/

    public void setGlassPanelVisible(boolean b) {
        mF.getGlassPane().setVisible(b);
    }

    protected void setPanel(String panelName, JPanel panel) {

        switch (panelName) {
            case GUI.PHASE1:
                firstPhasePanel = (FirstPhasePanel) panel;
                break;
            case GUI.PHASE2:
                secondPhasePanel = (SecondPhasePanel) panel;
                break;
            default:
                break;
        }

        mF.addPanel(panel, panelName);
    }

    /**********
     * GETTERS
     **********/

    public MainFrame getMainFrame() {
        return mF;
    }

    public Kube getKube() {
        return k3;
    }

    public JPanel getContentPane() {
        return mF.getFramePanel();
    }

    public Component getOverlayComponent() {
        return mF.getOverlayComponent();
    }

    public MouseAdapter getCurrentListener() {
        return getMainFrame().getCurrentListener();
    }

    public MouseAdapter getDefaultGlassPaneController() {
        return getMainFrame().getDefaultGlassPaneController();
    }

    public GUIControllers getControllers() {
        return controllers;
    }

    public JPanel getPanel(String panelName) {

        switch (panelName) {
            case GUI.PHASE1:
                return firstPhasePanel;
            case GUI.PHASE2:
                return secondPhasePanel;
            default:
                return null;
        }
    }

    public void setGlassPaneController(MouseAdapter ma) {
        mF.setGlassPaneController(ma);
    }

    public Phase1DnD getGlassPaneController() {
        if (mF.getGlassPane().getMouseListeners() == null) {
            return null;
        } else {
            return new Phase1DnD(eventsToView, eventsToModel);
        }
    }

    public JPanel getOverlay() {
        return mF.getOverlay();
    }

    /**********
     * UPDATERS
     **********/

    /**
     * Update the first panel
     * 
     * @param action the action to update the panel with
     */
    public void updateFirstPanel(Action action) {
        firstPhasePanel.update(action);
    }

    /**
     * Update the second panel
     * 
     * @param action the action to update the panel with
     */
    public void updateSecondPanel(Action action) {
        secondPhasePanel.update(action);
    }

    /**
     * Update the drag and drop
     * 
     * @param action the action to update the drag and drop with
     */
    public void updateDnd(Action action) {
        if (k3.getPhase() == Kube.PREPARATION_PHASE) {
            firstPhasePanel.updateDnd(action);
        } else {
            secondPhasePanel.updateDnd(action);
        }
    }

    /**
     * Update the hexagones size
     * 
     * @return void
     */
    public void updateHexSize() {
        switch (k3.getPhase()) {
            case Kube.PREPARATION_PHASE:
                firstPhasePanel.updateHexSize();
                break;
            case Kube.GAME_PHASE:
                secondPhasePanel.updateHexSize();
                break;
            default:
                Config.error("Unimplemented kube's phase for resize");
                break;
        }
    }

    /**********
     * HELPERS
     **********/

    /**
     * Show a panel
     * 
     * @param panelName the name of the panel to show
     * @return void
     */
    public void showPanel(String panelName) {
        waitPanel(panelName);
        mF.showPanel(panelName);
    }

    /**
     * Show all borders
     * 
     * @param container the container to show the borders of
     * @return void
     */
    private void showAllBorders(Container container) {

        for (Component comp : container.getComponents()) {
            if (comp instanceof Container) {
                showAllBorders((Container) comp);
            }
            if (comp instanceof JPanel) {
                ((JPanel) comp).setBorder(BorderFactory.createLineBorder(Color.red));
            }
        }
    }

    /**
     * Show a warning message
     * 
     * @param title   the title of the message
     * @param message the message to show
     * @return void
     */
    public void showWarning(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Show an error message
     * 
     * @param title   the title of the message
     * @param message the message to show
     * @return void
     */
    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show an information message
     * 
     * @param title   the title of the message
     * @param message the message to show
     * @return void
     */
    public void showInfo(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**********
     * METHODS
     **********/

    @Override
    public void run() {

        // Disable optimized drawing
        System.setProperty("sun.java2d.opengl", "true");

        mF = new MainFrame();
        mP = new MenuPanel(this, controllers.getMenuController());
        mF.addPanel(mP, MENU);

        if (Config.SHOW_BORDERS) {
            showAllBorders(mF);
        }

        mF.repaint();
        mF.setFrameVisible(true);
        loadPanel(PHASE1);

        // After repaint start loading next panel
        createGlassPane();
    }

    /**
     * Update the panel according to the model
     * 
     * @return void
     */
    public void updatePanel() {

        switch (k3.getPhase()) {

            case Kube.PREPARATION_PHASE:
                setGlassPaneController(new Phase1DnD(eventsToView, eventsToModel));
                firstPhasePanel.buildMessage();
                firstPhasePanel.updateAll(true);
                mF.showPanel(PHASE1);
                setGlassPanelVisible(true);
                loadPanel(PHASE2);
                break;
            case Kube.GAME_PHASE:
                loadPanel(PHASE2);
                firstPhasePanel.setWaitingButton();
                setGlassPaneController(new Phase2DnD(eventsToView, eventsToModel));
                waitPanel(PHASE2);
                secondPhasePanel.startMessage();
                secondPhasePanel.updateAll();
                mF.showPanel(PHASE2);
                setGlassPanelVisible(true);
                firstPhasePanel.resetButtonValue();
                break;
        }
    }

    /**
     * Load a panel according to its name
     * 
     * @param panelName the name of the panel to load
     * @return void
     */
    public void loadPanel(String panelName) {

        PanelLoader loader;

        switch (panelName) {
            case GUI.PHASE1:
            case GUI.PHASE2:
                if (getPanel(panelName) != null) {
                    Config.debug("Panel ", panelName, " already loaded");
                    return;
                }
                break;
            default:
                break;
        }

        loader = new PanelLoader(this, panelName, k3, controllers, eventsToView, eventsToModel);
        loaderThread = new Thread(loader);
        loaderThread.start();
    }

    /**
     * Wait for a panel to load
     * 
     * @param panelName the name of the panel to wait for
     * @return void
     */
    public synchronized void waitPanel(String panelName) {

        switch (panelName) {
            case GUI.PHASE1:
            case GUI.PHASE2:
                try {
                    while (getPanel(panelName) == null) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    Config.error("Interrupted loading");
                }
                break;
            case GUI.MENU:
                // Do nothing, menu always loaded
                break;
            default:
                Config.error("Waiting for non existent panel " + panelName);
                break;
        }

        Config.debug("Panel ", panelName, " finished loading");
    }

    /**
     * Add a component to the overlay
     * 
     * @param p the component to add
     * @return void
     */
    public void addToOverlay(Component p) {
        mF.addToOverlay(p);
    }

    /**
     * Remove all components from the overlay
     * 
     * @param p the component to remove
     * @return void
     */
    public void removeAllFromOverlay() {
        mF.removeAllFromOverlay();
    }

    /**
     * Create a glass pane
     * 
     * @return void
     */
    public void createGlassPane() {
        mF.createGlassPane();
    }

    /**
     * Increment the UI scale
     * 
     * @param factor the factor to increment the scale with
     */
    public void incrementUIScale(double factor) {
        mF.incrementUIScale(factor);
    }

    /**
     * Reset the UI scale
     * 
     * @return void
     */
    public void resetUIScale() {
        mF.resetUIScale();
    }

    /**
     * Display the win message
     * 
     * @param action the action to display the message with
     * @return void
     */
    public void winMessage(Action action) {
        secondPhasePanel.winMessage(action);
    }

    /**
     * Ask for confirmation for an action
     *
     * @param title   title of the Window
     * @param message message to display
     * @return boolean whether or not the user confirmed the action
     */
    public boolean askForConfirmation(String title, String message) {
        // Custom options for buttons
        Object[] options = { "Oui", "Non" };

        // Show custom dialog
        int option = JOptionPane.showOptionDialog(null,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        return option == JOptionPane.YES_OPTION;
    }

    /**
     * Display the save message
     * 
     * @param action the action to save
     * @return void
     */
    public void save(Action action) {

        HexGlow hexGlow;
        PanelGlow panGlow;
        TextEntryPanel savePanel;

        setGlassPanelVisible(false);
        hexGlow = null;
        panGlow = null;

        if ((Integer) action.getData() == 1) {
            hexGlow = firstPhasePanel.animationGlow;
        } else if ((Integer) action.getData() == 2) {
            hexGlow = secondPhasePanel.animationHexGlow;
            panGlow = secondPhasePanel.animationPanelGlow;
        }

        savePanel = new TextEntryPanel(this, hexGlow, panGlow);
        savePanel.setPreferredSize(getMainFrame().getSize());
        addToOverlay(savePanel);
    }
}
