package kube.view.animations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

import kube.configuration.Config;
import kube.view.panels.RulesPanel;

public class AnimatedRule implements ActionListener {

    private Timer timer;
    private JLabel[] frames;
    private int currentFrame;

    public AnimatedRule (int ruleNb, RulesPanel.AnimationPanel animationPanel){
        currentFrame = 0;
        frames = animationPanel.getFrames();
        timer = new Timer(500, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Config.debug(currentFrame);
        frames[currentFrame].setVisible(false);
        currentFrame = (currentFrame + 1) %4;
        frames[currentFrame].setVisible(true);
    }
}
