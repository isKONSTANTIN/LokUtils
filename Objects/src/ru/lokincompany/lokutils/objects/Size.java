package ru.lokincompany.lokutils.objects;

import java.util.Objects;

public class Size {
    public static Size ZERO = new Size(0,0);

    public final float width;
    public final float height;

    public Size(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Size setWidth(float width){
        return new Size(width, height);
    }

    public Size setHeight(float height){
        return new Size(width, height);
    }

    public Size relativeTo(Size origin) {
        return relativeTo(origin.width, origin.height);
    }

    public Size relativeTo(float x, float y) {
        return offset(-x, -y);
    }

    public Size offset(Size size) {
        return offset(size.width, size.height);
    }

    public Size offset(float width, float height) {
        return new Size(this.width + width, this.height + height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Size size = (Size) o;
        return Float.compare(size.width, width) == 0 &&
                Float.compare(size.height, height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }
}
