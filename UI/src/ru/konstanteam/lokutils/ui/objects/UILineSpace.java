package ru.konstanteam.lokutils.ui.objects;

import org.omg.PortableServer.THREAD_POLICY_ID;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.tools.property.SoftValue;
import ru.konstanteam.lokutils.ui.UIObject;
import ru.konstanteam.lokutils.ui.layout.ObjectFreeLayout;
import ru.konstanteam.lokutils.ui.layout.UIAbstractLayout;

public class UILineSpace extends UIObject {
    protected float height = 4;

    public UILineSpace() {
        size().set(() -> {
            UIAbstractLayout abstractLayout = getOwner();

            if (abstractLayout instanceof ObjectFreeLayout) {
                Point objectPosition = ((ObjectFreeLayout) abstractLayout).getLazyObjectPosition(this);

                if (objectPosition != null)
                    return new Size(abstractLayout.size().lazyGet().width - objectPosition.x, height);
            }

            return Size.ZERO;
        });
    }

    public float getHeight() {
        return height;
    }

    public UILineSpace setHeight(float height) {
        this.height = height;

        return this;
    }
}
