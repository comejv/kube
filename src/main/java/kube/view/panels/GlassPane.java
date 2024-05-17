package kube.view.panels;

import java.awt.Dimension;
import java.awt.event.MouseEvent;

import java.awt.event.MouseListener;

import javax.swing.JPanel;

import kube.configuration.Config;

public class GlassPane extends JPanel{
    
    public GlassPane (){
        setPreferredSize( new Dimension(Config.getInitHeight(), Config.getInitWidth()));
        setVisible(false);
        setOpaque(false);

        addMouseListener(new GlassMouseListener() {
            
        });
    }

    private class GlassMouseListener implements MouseListener{
        public void mouseExited(MouseEvent e){
        }

        public void mouseReleased(MouseEvent e){
        }

        public void mouseClicked(MouseEvent e){
        }

        public void mousePressed(MouseEvent e){
        }

        public void mouseEntered(MouseEvent e){
        }
    }
}
