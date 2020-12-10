package ru.konstanteam.lokutils.gui.objects;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.objects.Size;

public class GUILineSpace extends GUIObject {

    public GUILineSpace() {

    }

    public float getHeight() {
        return minimumSize().get().height;
    }

    public GUILineSpace setHeight(float height) {
        minimumSize().set(new Size(0, height));

        return this;
    }

    @Override
    public void render() {

    }
}
