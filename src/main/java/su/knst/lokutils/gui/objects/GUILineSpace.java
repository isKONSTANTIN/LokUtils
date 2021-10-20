package su.knst.lokutils.gui.objects;

import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.objects.Size;

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
