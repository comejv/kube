package kube.view.animations;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;;

public class PanelGlow implements ActionListener {
    private Timer timer;
    private int yellowIntensity;
    private int state;
    private HashMap<JPanel, String> toGlow;

    public PanelGlow() {
        yellowIntensity = 1;
        toGlow = new HashMap<>();
        this.timer = new Timer(1000 / 30, this);
        timer.start();
    }

    public void setToRedraw(HashMap<JPanel, String> toGlow) {
        this.toGlow = toGlow;
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
        if (toGlow != null){
            for (JPanel pan : toGlow.keySet()) {
                if (toGlow.containsKey(pan) && toGlow.get(pan).length() > 0){ // additionnal pan
                    LineBorder line = new LineBorder(new Color(255, 255, yellowIntensity),10);
                    TitledBorder title = BorderFactory.createTitledBorder(line, "Pieces additionnelles du " + toGlow.get(pan) ,TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Jomhuria", Font.PLAIN, 40));
                    pan.setBorder(title);
                }
                else {
                    pan.setBorder(BorderFactory.createLineBorder(new Color(255, 255, yellowIntensity),10));
                }
                pan.repaint();
            }
        }
    }
}
