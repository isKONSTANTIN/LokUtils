package ru.lokincompany.lokutils.ui.positioning;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.ui.UIObject;

public class PositioningSetter {

    Position position;
    UIObject object;

    PositioningSetter childSetter;
    PositioningСonsider consider;

    Vector2f modifier = new Vector2f();

    public PositioningSetter(PositioningСonsider consider) {
        this.consider = consider;
    }

    public PositioningSetter(Position position) {
        this.position = position;
    }

    public Vector2f get() {
        Vector2f result = new Vector2f();

        result.translate(modifier.x, modifier.y);

        if (childSetter != null){
            Vector2f childResult = childSetter.get();
            result.translate(childResult.x, childResult.y);
        }

        if (consider != null){
            Vector2f considerResult = consider.calculate();
            result.translate(considerResult.x, considerResult.y);
        }

        return result;
    }

    public void init(UIObject object) {
        this.object = object;

        if (position != null)
            consider = PositioningAlgorithms.getAlgorithm(object, position);
    }

    public PositioningSetter setConsider(PositioningСonsider consider){
        this.consider = consider;

        return this;
    }

    public PositioningSetter setChildSetter(PositioningSetter childSetter){
        childSetter.init(object);
        this.childSetter = childSetter;

        return this;
    }

    public PositioningSetter setChildSetter(PositioningСonsider consider){
        PositioningSetter setter = new PositioningSetter(consider);
        setter.init(object);
        this.childSetter = setter;

        return this;
    }

    public PositioningSetter setChildSetter(Position position){
        PositioningSetter setter = new PositioningSetter(position);
        setter.init(object);
        this.childSetter = setter;

        return this;
    }

    public PositioningSetter getChildSetter() {
        return childSetter;
    }

    public PositioningСonsider getConsider() {
        return consider;
    }

    public Vector2f getModifier() {
        return modifier;
    }

    public PositioningSetter increaseModifier(float factor) {
        modifier.x += factor;
        modifier.y += factor;

        return this;
    }

    public PositioningSetter increaseModifier(Vector2f factor) {
        modifier.x += factor.x;
        modifier.y += factor.y;

        return this;
    }

    public PositioningSetter reduceModifier(float factor) {
        modifier.x -= factor;
        modifier.y -= factor;

        return this;
    }

    public PositioningSetter reduceModifier(Vector2f factor) {
        modifier.x -= factor.x;
        modifier.y -= factor.y;

        return this;
    }

    public PositioningSetter setModifier(Vector2f modifier) {
        this.modifier.x = modifier.x;
        this.modifier.y = modifier.y;

        return this;
    }
}
