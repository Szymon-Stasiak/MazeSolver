package Gui;

class ColorPack<E> {
    private int color;
    private E value;

    public ColorPack(E value, int color) {
        this.color = color;
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public E getValue() {
        return value;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String toString() {
        return value.toString();
    }
}