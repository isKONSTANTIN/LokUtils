package ru.lokincompany.lokutils.ui.positioning;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.ui.UIObject;

public class PositioningAlgorithms {
    public static PositioningСonsider getAlgorithm(UIObject object, Position position) {
        PositioningСonsider algorithm;

        switch (position) {
            case TopLeft:
                algorithm = () -> new Vector2f(0, 0);
                break;

            case TopCenter:
                algorithm = () -> new Vector2f(object.getLastParent().getSize().x / 2 - object.getSize().x / 2, 0);
                break;

            case TopRight:
                algorithm = () -> new Vector2f(object.getLastParent().getSize().x - object.getSize().x, 0);
                break;

            case CenterLeft:
                algorithm = () -> new Vector2f(0, object.getLastParent().getSize().y / 2 - object.getSize().y / 2);
                break;

            case Center:
                algorithm = () -> new Vector2f(object.getLastParent().getSize().x / 2 - object.getSize().x / 2, object.getLastParent().getSize().y / 2 - object.getSize().y / 2);
                break;

            case CenterRight:
                algorithm = () -> new Vector2f(object.getLastParent().getSize().x - object.getSize().x, object.getLastParent().getSize().y / 2 - object.getSize().y / 2);
                break;

            case BottomLeft:
                algorithm = () -> new Vector2f(0, object.getLastParent().getSize().y - object.getSize().y);
                break;

            case BottomCenter:
                algorithm = () -> new Vector2f(object.getLastParent().getSize().x / 2 - object.getSize().x / 2, object.getLastParent().getSize().y - object.getSize().y);
                break;

            case BottomRight:
                algorithm = () -> new Vector2f(object.getLastParent().getSize().x - object.getSize().x, object.getLastParent().getSize().y - object.getSize().y);
                break;

            default:
                algorithm = () -> new Vector2f();
        }

        return algorithm;
    }
}
