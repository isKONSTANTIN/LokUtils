package ru.lokincompany.lokutils.ui.animation;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.ui.UIObject;

public abstract class Animation {
    protected String name;
    protected boolean isRun;
    protected UIObject object;

    public Animation(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void start(){
        isRun = true;
        started();
    }

    public void stop() {
        isRun = false;
        stopped();
    }

    public UIObject getObject() {
        return object;
    }

    public boolean isRun() {
        return isRun;
    }

    public void init(UIObject object){
        this.object = object;
    }

    protected void setPosition(float x, float y){
        object.getPosition().x = x;
        object.getPosition().y = y;
    }

    protected void setSize(float x, float y){
        object.getSize().x = x;
        object.getSize().y = y;
    }

    protected void moveObject(float x, float y){
        object.getPosition().x += x;
        object.getPosition().y += y;
    }

    protected void stretchObject(float x, float y){
        object.getSize().x += x;
        object.getSize().y += y;
    }

    protected void softSetPositionObject(float x, float y, float speed){
        object.getPosition().x = softChange(object.getPosition().x, x, speed);
        object.getPosition().y = softChange(object.getPosition().y, y, speed);
    }

    protected void softSetSizeObject(float x, float y, float speed){
        object.getSize().x = softChange(object.getSize().x, x, speed);
        object.getSize().y = softChange(object.getSize().y, y, speed);
    }

    protected float softChange(float source, float end, float speed){
        return source + (end - source) * (speed / 10f);
    }

    public abstract void update();
    public abstract void started();
    public abstract void stopped();
}
