package kube.view.animations;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import kube.configuration.Config;
import kube.model.ModelColor;
import kube.view.GUI;
import kube.view.components.HexIcon;
import kube.view.panels.DrawHexPanel;
import kube.view.panels.GlassPanel;

public class fakeDnD implements ActionListener {
    private Timer timer;
    private int state;
    private HexIcon hex;
    private Point from, to;
    private GUI gui;
    private DrawHexPanel hexPanel;
    private ModelColor c;
    private int nState;
    public fakeDnD(ModelColor c, Point from, Point to, GUI gui) {
        Config.debug("Start fake DnD");
        this.from = from;
        this.to = to; 
        this.gui = gui;
        this.c = c;
        hexPanel = new DrawHexPanel(from, new HexIcon(c));
        hexPanel.setPreferredSize(gui.getMainFrame().getSize());
        hexPanel.setVisible(true);
        gui.addToOverlay(hexPanel);
        hexPanel.repaint();
        timer = new Timer(200, this);
        timer.start();
    }

   

    @Override
    public void actionPerformed(ActionEvent e) {
        state = state + 1;

        hexPanel.setPoint(hexPanel.getPoint().x + 1, hexPanel.getPoint().y + 1);
        hexPanel.repaint();
    }

    public Timer getTimer() {
        return timer;
    }

}
