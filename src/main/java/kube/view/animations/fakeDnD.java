package kube.view.animations;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import kube.model.ModelColor;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.view.GUI;
import kube.view.components.HexIcon;
import kube.view.panels.DrawHexPanel;

public class fakeDnD implements ActionListener {
    private Timer timer;
    private int state;
    private Point decale;
    private GUI gui;
    private DrawHexPanel hexPanel;
    private int maxState;
    private Object lock;

    public fakeDnD(ModelColor c, Point from, Point to, GUI gui, Object lock) {
        gui.getEventsToModel().add(new Action(ActionType.AI_PAUSE, true));
        this.gui = gui;
        this.lock = lock;
        maxState = 30;
        decale = new Point((to.x - from.x) / maxState, (to.y - from.y) / maxState);
        hexPanel = new DrawHexPanel(from, new HexIcon(c));
        hexPanel.setPreferredSize(gui.getMainFrame().getSize());
        hexPanel.setVisible(true);
        gui.addToOverlay(hexPanel);
        hexPanel.repaint();
        timer = new Timer(1000 / 30, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        state = state + 1;
        if (state > maxState) {
            gui.removeAllFromOverlay();
            timer.stop();
            synchronized (lock) {
                lock.notifyAll();
            }
            gui.getEventsToModel().add(new Action(ActionType.AI_PAUSE, false));
        }
        hexPanel.setPoint(hexPanel.getPoint().x + decale.x, hexPanel.getPoint().y + decale.y);
        hexPanel.repaint();
    }

    public Timer getTimer() {
        return timer;
    }
}
