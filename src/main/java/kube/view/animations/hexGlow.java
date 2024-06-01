package kube.view.animations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;
import kube.view.components.HexIcon;
import kube.configuration.Config;

public class hexGlow implements ActionListener {
    private Timer timer;
    private float brightness;
    private int state;
    private ArrayList<HexIcon> toRedraw;

    public hexGlow() {
        brightness = 1;
        toRedraw = new ArrayList<>();
        this.timer = new Timer(1000 / 30, this);
        timer.start();
    }

    public void setToRedraw(ArrayList<HexIcon> toRedraw) {
        Config.debug(toRedraw.size());
        this.toRedraw = toRedraw;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        state = state + 1;
        if (state < 25) {
            brightness += 0.015f;
        } else if (state < 50) {
            brightness -= 0.015f;
        } else {
            brightness = 1;
            state = 0;
        }
        //Config.debug(brightness );
        if (toRedraw != null){
            for (HexIcon hex : toRedraw) {
                hex.setBrightness(brightness);
                hex.repaint();
            }
        }
    }
}
