package ru.lokincompany.lokutils.ui.positioning;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.ui.UIObject;

public interface PositioningSetter {
    Vector2f modifier = new Vector2f();

    Vector2f calculate();

    default Vector2f get() {
        Vector2f result = calculate();

        result.x += modifier.x;
        result.y += modifier.y;

        return result;
    }

    default PositioningSetter increaseModifier(float factor){
        modifier.x += factor;
        modifier.y += factor;

        return this;
    }

    default PositioningSetter increaseModifier(Vector2f factor){
        modifier.x += factor.x;
        modifier.y += factor.y;

        return this;
    }

    default PositioningSetter reduceModifier(float factor){
        modifier.x -= factor;
        modifier.y -= factor;

        return this;
    }

    default PositioningSetter reduceModifier(Vector2f factor){
        modifier.x -= factor.x;
        modifier.y -= factor.y;

        return this;
    }

    default PositioningSetter setModifier(Vector2f modifier){
        this.modifier.x = modifier.x;
        this.modifier.y = modifier.y;

        return this;
    }

    default void init(UIObject object){}
}
