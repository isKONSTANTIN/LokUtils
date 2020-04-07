package ru.lokincompany.lokutils.ui.positioning;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.ui.UIObject;

public abstract class PositioningSetter {
    public abstract Vector2f update(UIObject object);
}
