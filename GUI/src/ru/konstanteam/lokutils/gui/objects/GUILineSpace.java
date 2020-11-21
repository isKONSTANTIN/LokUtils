package ru.konstanteam.lokutils.gui.objects;

import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.layout.ObjectFreeLayout;
import ru.konstanteam.lokutils.gui.layout.GUIAbstractLayout;

public class GUILineSpace extends GUIObject {
    protected float height = 4;

    public GUILineSpace() {
        size().set(() -> new Size(0, height));
    }

    public float getHeight() {
        return height;
    }

    public GUILineSpace setHeight(float height) {
        this.height = height;

        return this;
    }

    @Override
    public void render() {

    }
}
