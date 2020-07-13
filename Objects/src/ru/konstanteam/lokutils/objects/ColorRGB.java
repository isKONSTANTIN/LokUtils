package ru.konstanteam.lokutils.objects;

public class ColorRGB extends Color {
    public ColorRGB(int red, int green, int blue, int alpha) {
        super(red / 255f, green / 255f, blue / 255f, alpha / 255f);
    }
}
