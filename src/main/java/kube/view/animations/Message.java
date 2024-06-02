package kube.view.animations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.Timer;
import kube.view.panels.TransparentPanel;

public class Message implements ActionListener {
    private Timer timer;
    private float opacity;
    private int state;
    private TransparentPanel panel;
    private HexGlow hexGlow;
    private boolean onlyDecreasing;
    public Message(TransparentPanel panel, String text, HexGlow hexGlow) {
        this(panel, text, hexGlow, false);
    }

    public Message(TransparentPanel panel, String text, HexGlow hexGlow, boolean onlyDecreasing) {
        if (onlyDecreasing) {
            opacity = 1;
            state = 25;
        } else {
            opacity = 0;
            state = 0;
        }
        state = 0;
        this.panel = panel;
        this.onlyDecreasing = onlyDecreasing;
        this.timer = new Timer(1000 / 30, this);
        this.hexGlow = hexGlow;
        hexGlow.getTimer().stop();
        panel.setText(text);
        panel.setVisible(true);
        timer.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        state = state + 1;
        if (state < 25 && !onlyDecreasing) {
            opacity += 0.04; // Lighten
        } else if (state >= 50 && state < 75) {
            opacity -= 0.04; // Darken
        } else if (state >= 75) {
            panel.setVisible(false);
            hexGlow.getTimer().restart();
            timer.stop();
        }
        opacity = Math.max(opacity, 0);
        panel.setOpacity(opacity);

    }
}
