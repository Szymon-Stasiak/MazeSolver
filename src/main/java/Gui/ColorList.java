package Gui;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import java.awt.Graphics;


public class ColorList<E> extends JList<ColorPack<E>> {
    ColorList(int elemSize, int borderSize) {
        super();
        setCellRenderer(new ColorListRenderer<E>(elemSize, borderSize));
    }

    ColorList(ColorPack<E>[] data, int elemSize, int borderSize) {
        super(data);
        setCellRenderer(new ColorListRenderer<E>(elemSize, borderSize));
    }
}

class ColorListRenderer<E> extends JCheckBox implements ListCellRenderer<ColorPack<E>> {
    private int elementSize;
    private int borderSize;

    public ColorListRenderer(int elementSize, int borderSize) {
        setOpaque(true);
        this.elementSize = elementSize;
        this.borderSize = borderSize;
    }

    private Color invertseColor(Color color) {
        return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
    }

    private Icon createPressedIcon(int color, int size, int borderSize) {
        Icon icon = new Icon() {
            private final int offset = 1;

            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.getClipBounds().getWidth();
                g.setColor(Color.BLACK);
                g.fillRect(offset, offset, size, size);
                g.setColor(new Color(color));
                g.fillRect(borderSize+offset, borderSize+offset, size - 2 * borderSize, size - 2 * borderSize);
            }

            public int getIconWidth() {
                return size;
            }
            public int getIconHeight() {
                return size;
            }
        };
        return icon;
    }

    private Icon createDisabledIcon(int color, int size, int borderSize) {
        Icon icon = new Icon() {
            private final int offset = 1;
            private final int selectIconSize = 3;

            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.getClipBounds().getWidth();
                g.setColor(Color.BLACK);
                g.fillRect(offset, offset, size, size);
                g.setColor(new Color(color));
                g.fillRect(borderSize+offset, borderSize+offset, size - 2 * borderSize, size - 2 * borderSize);
                g.setColor(invertseColor(new Color(color)));
                g.fillOval(borderSize+offset+selectIconSize, borderSize+offset+selectIconSize, size-2*borderSize-2*selectIconSize, size-2*borderSize-2*selectIconSize);
            }

            public int getIconWidth() {
                return size;
            }
            public int getIconHeight() {
                return size;
            }
        };
        return icon;
    }

    public Component getListCellRendererComponent(JList<? extends ColorPack<E>> list, ColorPack<E> value, int index, boolean isSelected, boolean cellHasFocus) {

        setComponentOrientation(list.getComponentOrientation());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setForeground(list.getForeground());
        if(isSelected) setForeground(Color.RED);
        setSelected(isSelected);
        setEnabled(list.isEnabled());
        setIcon(createPressedIcon(value.getColor(), elementSize, borderSize));
        //setSelectedIcon(createDisabledIcon(value.getColor(), elementSize, borderSize));
        setPressedIcon(createPressedIcon(value.getColor(), elementSize, borderSize));

        setText(value == null ? "" : value.toString());  

        return this;
    }
}
