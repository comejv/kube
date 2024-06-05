package kube.view.animations;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import kube.model.Kube;
import kube.view.GUIColors;

public class PanelGlow implements ActionListener {
    private Timer timer;
    private int yellowIntensity;
    private int state;
    private HashMap<JPanel, String> toGlow;
    private Kube k3;

    public PanelGlow(Kube k3) {
        this.k3 = k3;
        yellowIntensity = 1;
        toGlow = new HashMap<>();
        this.timer = new Timer(1000 / 30, this);
        timer.start();
    }

    public void setToRedraw(HashMap<JPanel, String> toGlow) {
        this.toGlow = toGlow;
    }

    public Timer getTimer() {
        return timer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        state = state + 1;
        if (state < 25) {
            yellowIntensity += (200 / 25); // Lighten
        } else if (state < 50) {
            yellowIntensity -= (200 / 25); // Darken
        } else {
            yellowIntensity = 0; // Base value
            state = 0;
        }
        yellowIntensity = Math.max(yellowIntensity, 0);
        if (toGlow != null) {
            Color col = k3.getPenality() ? new Color(255, yellowIntensity, yellowIntensity)
                    : new Color(255, 255, yellowIntensity);
            for (JPanel pan : toGlow.keySet()) {
                if (toGlow.containsKey(pan) && toGlow.get(pan).length() > 0) { // additionnal pan
                    LineBorder line = new LineBorder(col, 10);
                    TitledBorder title = BorderFactory.createTitledBorder(line,
                            "Pieces additionnelles du " + toGlow.get(pan), TitledBorder.CENTER, TitledBorder.TOP,
                            new Font("Jomhuria", Font.PLAIN, 40), GUIColors.ACCENT.toColor());
                    pan.setBorder(title);
                } else {
                    pan.setBorder(BorderFactory.createLineBorder(col, 10));
                }
                pan.repaint();
            }
        }
    }
}
