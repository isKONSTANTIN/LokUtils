package ru.lokincompany.lokutils.objects;

import org.lwjgl.util.vector.Vector4f;

public class Color {

    protected Vector4f rawColor = new Vector4f();

    public Color() {
    }

    public Color(float red, float green, float blue, float alpha) {
        rawColor.x = red;
        rawColor.y = green;
        rawColor.z = blue;
        rawColor.w = alpha;
    }

    public Color clone() {
        return new Color(rawColor.x, rawColor.y, rawColor.z, rawColor.w);
    }

    public float getRawRed() {
        return rawColor.x;
    }

    public Color setRawRed(float red) {
        rawColor.x = red;
        return this;
    }

    public float getRawGreen() {
        return rawColor.y;
    }

    public Color setRawGreen(float green) {
        rawColor.y = green;
        return this;
    }

    public float getRawBlue() {
        return rawColor.z;
    }

    public Color setRawBlue(float blue) {
        rawColor.z = blue;
        return this;
    }

    public float getRawAlpha() {
        return rawColor.w;
    }

    public Color setRawAlpha(float alpha) {
        rawColor.w = alpha;
        return this;
    }

    public int getRGBRed() {
        return (int) (rawColor.x * 255);
    }

    public Color setRGBRed(int red) {
        rawColor.x = red / 255f;
        return this;
    }

    public int getRGBGreen() {
        return (int) (rawColor.y * 255);
    }

    public Color setRGBGreen(int green) {
        rawColor.y = green / 255f;
        return this;
    }

    public int getRGBBlue() {
        return (int) (rawColor.z * 255);
    }

    public Color setRGBBlue(int blue) {
        rawColor.z = blue / 255f;
        return this;
    }

    public int getRGBAlpha() {
        return (int) (rawColor.w * 255);
    }

    public Color setRGBAlpha(int alpha) {
        rawColor.w = alpha / 255f;
        return this;
    }

}