package ru.lokincompany.lokutils.ui.positioning;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.ui.UIObject;

public class PositioningSetter {

    Vector2f modifier = new Vector2f();
    PositioningСonsider consider;
    Position position;
    UIObject object;

    public PositioningSetter(PositioningСonsider consider){
        this.consider = consider;
    }

    public PositioningSetter(Position position){
        this.position = position;
    }

    public Vector2f get() {
        Vector2f result = consider.calculate();

        result.x += modifier.x;
        result.y += modifier.y;

        return result;
    }

    public PositioningSetter increaseModifier(float factor){
        modifier.x += factor;
        modifier.y += factor;

        return this;
    }

    public PositioningSetter increaseModifier(Vector2f factor){
        modifier.x += factor.x;
        modifier.y += factor.y;

        return this;
    }

    public PositioningSetter reduceModifier(float factor){
        modifier.x -= factor;
        modifier.y -= factor;

        return this;
    }

    public PositioningSetter reduceModifier(Vector2f factor){
        modifier.x -= factor.x;
        modifier.y -= factor.y;

        return this;
    }

    public PositioningSetter setModifier(Vector2f modifier){
        this.modifier.x = modifier.x;
        this.modifier.y = modifier.y;

        return this;
    }

    public void init(UIObject object){
        this.object = object;

        if (position != null)
            consider = PositioningAlgorithms.getAlgorithm(object, position);
    }
}
