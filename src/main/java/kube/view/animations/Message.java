package kube.view.animations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.Timer;

import kube.configuration.Config;
import kube.view.GUI;
import kube.view.panels.TransparentPanel;

public class Message implements ActionListener {
    private Timer timer;
    private float opacity;
    private int state, increasingState, stableState, decresingState;
    private TransparentPanel panel;
    private HexGlow hexGlow;
    private boolean onlyDecreasing;
    private GUI gui;

    public Message(TransparentPanel panel, String text, GUI gui, HexGlow hexGlow) {
        this(panel, text, gui, hexGlow, false);
    }

    public Message(TransparentPanel panel, String text, GUI gui, HexGlow hexGlow, boolean onlyDecreasing) {
        if (onlyDecreasing) {
            opacity = 1;
            increasingState = 0;
            stableState = 20;
            decresingState = 20;
        } else {
            opacity = 0;
            increasingState = 10;
            stableState = 20;
            decresingState = 10;
        }
        state = 0;
        this.panel = panel;
        this.onlyDecreasing = onlyDecreasing;
        this.timer = new Timer(2000 / (increasingState + stableState + decresingState), this);
        this.hexGlow = hexGlow;
        this.gui = gui;
        hexGlow.getTimer().stop();
        panel.setText(text);
        panel.setVisible(true);
        timer.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        state++;
        if (state < increasingState) {
            opacity += 1 / (float)increasingState; // Lighten
        } else if (state >= increasingState + stableState && state < increasingState + stableState + decresingState) {
            opacity -= 1 / (float)decresingState; // Darken
        } else if (state >= decresingState + stableState + increasingState) {
            panel.setVisible(false);
            gui.removeAllFromOverlay();
            hexGlow.getTimer().restart();
            timer.stop();
        } else {
            opacity = 1;
        }
        opacity = Math.max(opacity, 0);
        panel.setOpacity(opacity);
        Config.debug(opacity, state);
    }
}
