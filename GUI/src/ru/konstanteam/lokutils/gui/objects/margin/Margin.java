package ru.konstanteam.lokutils.gui.objects.margin;

public class Margin {
    public static final Margin PIXEL_MARGIN = new Margin(1,1,1,1);

    protected final int left;
    protected final int right;
    protected final int top;
    protected final int bottom;

    public Margin(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }
}
