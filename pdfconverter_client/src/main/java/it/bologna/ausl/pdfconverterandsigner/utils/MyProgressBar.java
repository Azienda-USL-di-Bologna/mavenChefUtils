package it.bologna.ausl.pdfconverterandsigner.utils;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import javax.swing.JDialog;

public class MyProgressBar extends JFrame {

    private JProgressBar current;
    private JApplet applet = null;

    public MyProgressBar() {
        this(null, true);
    }

    public MyProgressBar(JApplet applet, boolean external) {
        super("Inizializzazione...Attendi...");

        this.applet = applet;

        current = new JProgressBar(0, 100);
        current.setValue(0);

        if (external) {
            setAlwaysOnTop(true);
            setResizable(false);

    //        setEnabled(false);
            setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

            addWindowListener(new WindowListener() {

                @Override
                public void windowOpened(WindowEvent e) {
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    System.out.println("Premuto X");
                    UtilityFunctions.terminateApplication(1);
                }

                @Override
                public void windowClosed(WindowEvent e) {
                }

                @Override
                public void windowIconified(WindowEvent e) {
                }

                @Override
                public void windowDeiconified(WindowEvent e) {
                }

                @Override
                public void windowActivated(WindowEvent e) {
                }

                @Override
                public void windowDeactivated(WindowEvent e) {
                }
            });
            JPanel pane = new JPanel();
            pane.setLayout(null);

            current.setSize(300, 30);
            current.setStringPainted(true);
            pane.add(current);
            setContentPane(pane);
    //        pack();
            setLocationByPlatform(true);
            setSize(306, 60);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((int)(((screenSize.width - getWidth()) / 2) * .7),(int)(((screenSize.height - getHeight()) / 2) * .7));
        }
//        setContentPane(aaa);

        if (applet != null)
            applet.paint(applet.getGraphics());
    }

    public int getCurrentValue() {
        return current.getValue();
    }

    public int getMaxValue() {
        return current.getMaximum();
    }

    public void addStep(int amount) {

        if (current.getValue() + amount >= getMaxValue())
            current.setValue(getMaxValue());
        else
            current.setValue(getCurrentValue() + amount);
        update(getGraphics());
        //System.err.println("barra del progresso settata a " + getCurrentValue());

        if (applet != null) {
            applet.paint(applet.getGraphics());
        }
    }

    public void setValue(int value) {
        current.setValue(value);
        update(getGraphics());
        //System.err.println("barra del progresso settata a " + getCurrentValue());

        if (applet != null) {
            applet.paint(applet.getGraphics());
        }
    }

    public void reset() {
        current.setValue(0);
        update(getGraphics());
        System.err.println("barra del progresso settata a " + getCurrentValue());

        if (applet != null) {
            applet.paint(applet.getGraphics());
        }
    }

    public void setColor(Color color) {
        current.setForeground(color);

        if (applet != null) {
            applet.paint(applet.getGraphics());
        }
    }

    public Color getColor() {
        return current.getForeground();
    }

    public void terminate() {
        setTitle("Terminato");
        current.setValue(getMaxValue());

        if (applet != null) {
            applet.paint(applet.getGraphics());
        }
        setVisible(false);
        dispose();
    }


}
