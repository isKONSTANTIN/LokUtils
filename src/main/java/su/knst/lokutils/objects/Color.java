package su.knst.lokutils.objects;

import java.util.Objects;

public class Color {
    public static final Color RED = new Color(1, 0, 0, 1);
    public static final Color GREEN = new Color(0, 1, 0, 1);
    public static final Color BLUE = new Color(0, 0, 1, 1);
    public static final Color YELLOW = new Color(1, 1, 0, 1);
    public static final Color TURQUOISE = new Color(0, 1, 1, 1);
    public static final Color VIOLET = new Color(1, 0, 1, 1);
    public static final Color WHITE = new Color(1, 1, 1, 1);
    public static final Color BLACK = new Color(0, 0, 0, 1);

    public final float red;
    public final float green;
    public final float blue;
    public final float alpha;

    public Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color setRed(float red) {
        return new Color(red, green, blue, alpha);
    }

    public Color setGreen(float green) {
        return new Color(red, green, blue, alpha);
    }

    public Color setBlue(float blue) {
        return new Color(red, green, blue, alpha);
    }

    public Color setAlpha(float alpha) {
        return new Color(red, green, blue, alpha);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Float.compare(color.red, red) == 0 &&
                Float.compare(color.green, green) == 0 &&
                Float.compare(color.blue, blue) == 0 &&
                Float.compare(color.alpha, alpha) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue, alpha);
    }
}
