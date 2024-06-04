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
    public final static String FONT_PATH = "fonts/Jomhuria-Regular.ttf";

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
                    ResourceLoader.getResourceAsStream(FONT_PATH));
            ge.registerFont(font);
            ge.getAvailableFontFamilyNames();
        } catch (IOException | FontFormatException e) {
            Config.error("Could not load buttons font, using default.");
        }

        new Thread(new GUIEventsHandler(this, eventsToView, eventsToModel)).start();

        SwingUtilities.invokeLater(this);
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
     * Get the overlay panel
     * 
     * @return JPanel the overlay panel
     */
    public JPanel getOverlay() {
        return mF.getOverlay();
    }

    public void addToOverlay(Component p) {
        mF.addToOverlay(p);
    }

    public void removeAllFromOverlay() {
        mF.removeAllFromOverlay();
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

    public void createGlassPane() {
        mF.createGlassPane();
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

    public void setGlassPanelVisible(boolean b) {
        mF.getGlassPane().setVisible(b);
    }

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

    public void showWarning(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.WARNING_MESSAGE);
    }

    public void showError(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateFirstPanel(Action a) {
        firstPhasePanel.update(a);
    }

    public void updateSecondPanel(Action a) {
        secondPhasePanel.update(a);
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

    public MainFrame getMainFrame() {
        return mF;
    }

    public Kube getKube() {
        return k3;
    }

    public void updateDnd(Action a) {
        if (k3.getPhase() == Kube.PREPARATION_PHASE) {
            firstPhasePanel.updateDnd(a);
        } else {
            secondPhasePanel.updateDnd(a);
        }
    }

    public void incrementUIScale(double factor) {
        mF.incrementUIScale(factor);
    }

    public void resetUIScale() {
        mF.resetUIScale();
    }

    public void winMessage(Action a) {
        secondPhasePanel.winMessage(a);
    }

    public void updateHexSize() {
        switch (k3.getPhase()) {
            case Kube.PREPARATION_PHASE:
                firstPhasePanel.updateHexSize();
                break;
            case Kube.GAME_PHASE:
                secondPhasePanel.updateHexSize();
                break;
            default:
                Config.error("Unimplemented game phase for resize");
                break;
        }
    }

    public void save(Action a) {
        setGlassPanelVisible(false);
        HexGlow hexGlow = null;
        PanelGlow panGlow = null;
        if ((Integer) a.getData() == 1) {
            hexGlow = firstPhasePanel.animationGlow;
        } else if ((Integer) a.getData() == 2) {
            hexGlow = secondPhasePanel.animationHexGlow;
            panGlow = secondPhasePanel.animationPanelGlow;
        }
        TextEntryPanel savePanel = new TextEntryPanel(this, hexGlow, panGlow);
        savePanel.setPreferredSize(getMainFrame().getSize());
        addToOverlay(savePanel);
    }
}
