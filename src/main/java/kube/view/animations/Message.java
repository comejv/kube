package kube.view.animations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.view.GUI;
import kube.view.panels.TransparentPanel;

public class Message implements ActionListener {
    private Timer timer;
    private float opacity;
    private int state, increasingState, stableState, decreasingState;
    private TransparentPanel panel;
    private HexGlow hexGlow;
    private boolean firstTimeStable;
    private GUI gui;
    private boolean aiAlreadyPaused;
    public Message(TransparentPanel panel, String text, GUI gui, HexGlow hexGlow) {
        this(panel, text, gui, hexGlow, false, false);
    }

    public Message(TransparentPanel panel, String text, GUI gui, HexGlow hexGlow, boolean onlyDecreasing, boolean aiAlreadyPaused) {
        if (!aiAlreadyPaused){
            gui.getEventsToModel().add(new Action(ActionType.AI_PAUSE, true));
        }
        if (onlyDecreasing) {
            opacity = 1;
            increasingState = 0;
            stableState = 20;
            decreasingState = 20;
        } else {
            opacity = 0;
            increasingState = 20;
            stableState = 40;
            decreasingState = 20;
        }
        state = 0;
        this.aiAlreadyPaused = aiAlreadyPaused;
        this.panel = panel;
        this.firstTimeStable = true;
        this.timer = new Timer(2000 / (increasingState + stableState + decreasingState), this);
        this.hexGlow = hexGlow;
        this.gui = gui;
        if (hexGlow != null){
            hexGlow.getTimer().stop();
        }
        panel.setText(text);
        panel.setVisible(true);
        timer.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        state++;
        if (state < increasingState) {
            opacity += 1 / (float) increasingState; // Lighten
        } else if (state >= increasingState + stableState && state < increasingState + stableState + decreasingState) {
            opacity -= 1 / (float) decreasingState; // Darken
        } else if (state >= decreasingState + stableState + increasingState) {
            panel.setVisible(false);
            gui.removeAllFromOverlay();
            if (hexGlow != null){
                hexGlow.getTimer().restart();
            }
            if (!aiAlreadyPaused){
                gui.getEventsToModel().add(new Action(ActionType.AI_PAUSE, false));
            }
            timer.stop();
        } else {
            if (!firstTimeStable) {
                return;
            } else {
                firstTimeStable = false;
                opacity = 1;
            }
        }
        opacity = Math.max(opacity, 0);
        panel.setOpacity(opacity);
    }
}
