package kube.view;

// Import kube classes
import kube.configuration.Config;
import kube.configuration.ResourceLoader;
import kube.controller.graphical.Phase1DnD;
import kube.controller.graphical.Phase2DnD;
import kube.controller.graphical.GUIControllerManager;
import kube.model.Kube;
import kube.model.action.Action;
import kube.model.action.Queue;
import kube.view.animations.HexGlow;
import kube.view.animations.PanelGlow;
import kube.view.panels.*;

// Import java classes
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

    private MenuPanel menuPanel;
    private volatile FirstPhasePanel firstPhasePanel;
    private volatile SecondPhasePanel secondPhasePanel;

    private MainFrame mainFrame;
    private GUIControllerManager controllerManager;
    private Kube k3;
    private Thread loaderThread;
    private Queue<Action> eventsToView, eventsToModel;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor for the GUI class
     * 
     * @param k3                the Kube object
     * @param controllerManager the GUIControllers object
     * @param eventsToView      the Queue of Action to send to the view
     * @param eventsToModel     the Queue of Action to send to the model
     */
    public GUI(Kube k3, GUIControllerManager controllerManager, Queue<Action> eventsToView,
            Queue<Action> eventsToModel) {

        boolean nimbusFound;
        GraphicsEnvironment ge;
        Font font;

        this.eventsToView = eventsToView;
        this.eventsToModel = eventsToModel;
        this.controllerManager = controllerManager;
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
        getMainFrame().getGlassPane().setVisible(b);
    }

    public final void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public final void setMenuPanel(MenuPanel menuPanel) {
        this.menuPanel = menuPanel;
    }

    protected void setPanel(String panelName, JPanel panel) {

        switch (panelName) {
            case PHASE1:
                firstPhasePanel = (FirstPhasePanel) panel;
                break;
            case PHASE2:
                secondPhasePanel = (SecondPhasePanel) panel;
                break;
            default:
                break;
        }

        getMainFrame().addPanel(panel, panelName);
    }

    /**********
     * GETTERS
     **********/

    public SecondPhasePanel getSecondPhasePanel() {
        return secondPhasePanel;
    }

    public FirstPhasePanel getFirstPhasePanel() {
        return firstPhasePanel;
    }

    public Queue<Action> getEventsToView() {
        return eventsToView;
    }

    public Queue<Action> getEventsToModel() {
        return eventsToModel;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public Kube getK3() {
        return k3;
    }

    public Thread getLoaderThread() {
        return loaderThread;
    }

    public Kube getKube() {
        return k3;
    }

    public JPanel getContentPane() {
        return getMainFrame().getFramePanel();
    }

    public Component getOverlayComponent() {
        return getMainFrame().getOverlayComponent();
    }

    public MouseAdapter getCurrentListener() {
        return getMainFrame().getCurrentListener();
    }

    public MouseAdapter getDefaultGlassPaneController() {
        return getMainFrame().getDefaultGlassPaneController();
    }

    public GUIControllerManager getControllerManager() {
        return controllerManager;
    }

    public JPanel getPanel(String panelName) {

        switch (panelName) {
            case MENU:
                return menuPanel;
            case PHASE1:
                return firstPhasePanel;
            case PHASE2:
                return secondPhasePanel;
            default:
                return null;
        }
    }

    public void setGlassPaneController(MouseAdapter ma) {
        getMainFrame().setGlassPaneController(ma);
    }

    public Phase1DnD getGlassPaneController() {
        if (getMainFrame().getGlassPane().getMouseListeners() == null) {
            return null;
        } else {
            return new Phase1DnD(eventsToView, eventsToModel);
        }
    }

    public JPanel getOverlay() {
        return getMainFrame().getOverlay();
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
        getFirstPhasePanel().update(action);
    }

    /**
     * Update the second panel
     * 
     * @param action the action to update the panel with
     */
    public void updateSecondPanel(Action action) {
        getSecondPhasePanel().update(action);
    }

    /**
     * Update the drag and drop
     * 
     * @param action the action to update the drag and drop with
     */
    public void updateDnd(Action action) {
        if (getK3().getPhase() == Kube.PREPARATION_PHASE) {
            getFirstPhasePanel().updateDnd(action);
        } else {
            getSecondPhasePanel().updateDnd(action);
        }
    }

    /**
     * Update the hexagones size
     * 
     * @return void
     */
    public void updateHexSize() {
        switch (getK3().getPhase()) {
            case Kube.PREPARATION_PHASE:
                getFirstPhasePanel().updateHexSize();
                break;
            case Kube.GAME_PHASE:
                getSecondPhasePanel().updateHexSize();
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
        getMainFrame().showPanel(panelName);
    }

    /**
     * Show all borders
     * 
     * @param container the container to show the borders of
     * @return void
     */
    private void showAllBorders(Container container) {

        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                showAllBorders((Container) component);
            }
            if (component instanceof JPanel) {
                ((JPanel) component).setBorder(BorderFactory.createLineBorder(Color.red));
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

        setMainFrame(new MainFrame());
        setMenuPanel(new MenuPanel(this, getControllerManager().getMenuController()));
        getMainFrame().addPanel(menuPanel, MENU);

        if (Config.SHOW_BORDERS) {
            showAllBorders(mainFrame);
        }

        getMainFrame().repaint();
        getMainFrame().setFrameVisible(true);
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

        switch (getK3().getPhase()) {

            case Kube.PREPARATION_PHASE:
                setGlassPaneController(new Phase1DnD(eventsToView, eventsToModel));
                getFirstPhasePanel().buildMessage();
                getFirstPhasePanel().updateAll(true);
                getMainFrame().showPanel(PHASE1);
                setGlassPanelVisible(true);
                loadPanel(PHASE2);
                break;
            case Kube.GAME_PHASE:
                loadPanel(PHASE2);
                getFirstPhasePanel().setWaitingButton();
                setGlassPaneController(new Phase2DnD(eventsToView, eventsToModel));
                waitPanel(PHASE2);
                getSecondPhasePanel().startMessage();
                getSecondPhasePanel().updateAll();
                getMainFrame().showPanel(PHASE2);
                setGlassPanelVisible(true);
                getFirstPhasePanel().resetButtonValue();
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
            case PHASE1:
            case PHASE2:
                if (getPanel(panelName) != null) {
                    Config.debug("Panel ", panelName, " already loaded");
                    return;
                }
                break;
            default:
                break;
        }

        loader = new PanelLoader(this, panelName, k3, controllerManager, eventsToView, eventsToModel);
        loaderThread = new Thread(loader);
        getLoaderThread().start();
    }

    /**
     * Wait for a panel to load
     * 
     * @param panelName the name of the panel to wait for
     * @return void
     */
    public synchronized void waitPanel(String panelName) {

        switch (panelName) {
            case PHASE1:
            case PHASE2:
                try {
                    while (getPanel(panelName) == null) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    Config.error("Interrupted loading");
                }
                break;
            case MENU:
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
        getMainFrame().addToOverlay(p);
    }

    /**
     * Remove all components from the overlay
     * 
     * @return void
     */
    public void removeAllFromOverlay() {
        getMainFrame().removeAllFromOverlay();
    }

    /**
     * Create a glass pane
     * 
     * @return void
     */
    public void createGlassPane() {
        getMainFrame().createGlassPane();
    }

    /**
     * Increment the UI scale
     * 
     * @param factor the factor to increment the scale with
     */
    public void incrementUIScale(double factor) {
        getMainFrame().incrementUIScale(factor);
    }

    /**
     * Reset the UI scale
     * 
     * @return void
     */
    public void resetUIScale() {
        getMainFrame().resetUIScale();
    }

    /**
     * Display the win message
     * 
     * @param action the action to display the message with
     * @return void
     */
    public void winMessage(Action action) {
        getSecondPhasePanel().winMessage(action);
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
        Object[] options={"Oui","Non"};
        int option;

        // Show custom dialog
        option = JOptionPane.showOptionDialog(null,
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

        if (k3.getPhase() == Kube.PREPARATION_PHASE) {
            hexGlow = getFirstPhasePanel().getAnimationGlow();
        } else if (k3.getPhase() == Kube.GAME_PHASE) {
            hexGlow = getSecondPhasePanel().animationHexGlow;
            panGlow = getSecondPhasePanel().animationPanelGlow;
        }

        savePanel = new TextEntryPanel(this, hexGlow, panGlow);
        savePanel.setPreferredSize(getMainFrame().getSize());
        addToOverlay(savePanel);
    }

    /**
     * Enable the host start button
     * 
     * @param b whether or not to enable the button
     */
    public void enableHostStartButton(boolean b) {
        MenuPanel menuPanel;
        menuPanel = (MenuPanel) getPanel(MENU);
        menuPanel.enableHostStartButton(b);
    }

    public void changeMode(String mode) {
        Config.setMode(mode);
        firstPhasePanel.removeAll();
        firstPhasePanel.resetPanel();
        if(secondPhasePanel!=null){
            secondPhasePanel.removeAll();
            secondPhasePanel.resetPanel();
        }
    }
}
