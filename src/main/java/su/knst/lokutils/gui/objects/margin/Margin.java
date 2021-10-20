package su.knst.lokutils.gui.objects.margin;

public class Margin {
    public static final Margin PIXEL_MARGIN = new Margin(1, 1, 1, 1);

    protected final float left;
    protected final float right;
    protected final float top;
    protected final float bottom;

    public Margin(float left, float right, float top, float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public Margin(float general) {
        this.left = general;
        this.right = general;
        this.top = general;
        this.bottom = general;
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }

    public float getBottom() {
        return bottom;
    }
}
