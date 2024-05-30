package kube.view.components;

import kube.configuration.Config;
import kube.view.GUIColors;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class Entryfields {

    public static class askIP extends JTextField {
        public askIP(String name) {
            super(name);
            String def = "localhost";
            setText(def);
            setPreferredSize(new Dimension(Config.getInitWidth() / 3,
                    Config.getInitWidth() / 15));
            setBackground(GUIColors.ACCENT.toColor());
            setForeground(GUIColors.TEXT.toColor());
            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 10)));
        }
    }

    public static class askPort extends JTextField {
        public askPort(String name) {
            super(name);
            String def = "25565";
            setText(def);


            ((PlainDocument) this.getDocument()).setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                        throws BadLocationException {
                    if (string.matches("\\d*")) {
                        super.insertString(fb, offset, string, attr);
                    }
                }
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                        throws BadLocationException {
                    if (text.matches("\\d*")) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            });

            setPreferredSize(new Dimension(Config.getInitWidth() / 3,
                    Config.getInitWidth() / 15));
            setBackground(GUIColors.ACCENT.toColor());
            setForeground(GUIColors.TEXT.toColor());
            setFont(new Font("Jomhuria", Font.BOLD, (int) (Config.getInitHeight() / 10)));
        }
    }

}
