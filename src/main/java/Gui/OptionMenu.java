package Gui;

import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.CellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;

public class OptionMenu extends JPopupMenu {
    String[] options;
    BufferedImage mazeImg;
    Point mazePoint;
    int opt = -1;
    
    public OptionMenu(String labelText, Point pos, String[] options, Runnable[] actions, Runnable closeAction) {
        super();
        this.options = options;


        JLabel label = new JLabel(labelText);
        this.add(label);
        this.addSeparator();
        label.setAlignmentX(CENTER_ALIGNMENT);

        for (int i = 0; i < options.length; i++) {
            JButton button = new JButton(options[i]);
            button.setAlignmentX(CENTER_ALIGNMENT);
            button.setName(Integer.toString(i));
            button.addActionListener(e -> {
                actions[Integer.parseInt(((JButton)e.getSource()).getName())].run();
                setVisible(false);
                closeAction.run();
            });

            this.add(button);
        }

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event instanceof MouseEvent) {
                    MouseEvent me = (MouseEvent) event;
                    if (me.getID() == MouseEvent.MOUSE_PRESSED || me.getID() == MouseEvent.MOUSE_DRAGGED) {
                        if(!contains(me.getPoint())) {
                            setVisible(false);
                            Toolkit.getDefaultToolkit().removeAWTEventListener(this);
                            closeAction.run();
                        }
                    }
            }
        }}, AWTEvent.MOUSE_EVENT_MASK);

        this.setLocation(pos);
    }

    int getOption() {
        return opt;
    }
}
