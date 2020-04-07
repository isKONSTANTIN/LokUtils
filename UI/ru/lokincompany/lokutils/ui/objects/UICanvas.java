package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.input.Inputs;
import ru.lokincompany.lokutils.render.GLContext;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.render.tools.ViewTools;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;

import java.util.ArrayList;
import java.util.Vector;

public class UICanvas extends UIObject {

    protected Vector<UIObject> objects = new Vector<>();
    protected Vector<RenderPart> renderParts = new Vector<>();
    protected UICanvasRender render;
    protected Inputs inputs;

    protected final Object updateSync = new Object();

    public UICanvas(Inputs inputs){
        this.inputs = inputs;
        render = new UICanvasRender(this);
        setSize(new Vector2f(256, 256));
    }

    public UICanvas(){
        this(GLContext.getCurrent().getWindow().getInputs());
    }

    public Inputs getInputs() {
        return inputs;
    }

    public UIObject getObject(String name){
        for (UIObject object : objects){
            if (object.getName().equals(name))
                return object;
        }

        return null;
    }

    public UICanvas addObject(UIObject object){
        objects.add(object);
        return this;
    }

    public boolean removeObject(String name){
        for (int i = 0; i < objects.size(); i++){
            UIObject object = objects.get(i);

            if (object.getName().equals(name)){
                objects.remove(i);
                return true;
            }

        }
        return false;
    }

    @Override
    public RenderPart update(UICanvas parent) {
        super.update(parent != null ? parent : this);

        ArrayList<RenderPart> rp = new ArrayList<>();

        for (UIObject object : objects)
            rp.add(object.update(this));

        synchronized (updateSync){
            renderParts.clear();
            renderParts.addAll(rp);
        }

        return render;
    }
}

class UICanvasRender extends UIRenderPart<UICanvas> {

    public UICanvasRender(UICanvas object) {
        super(object);
    }

    @Override
    public void render() {
        ViewTools.moveOrtho2DView(object.getPosition().x, object.getPosition().y);

        synchronized (object.updateSync) {
            for (RenderPart renderPart : object.renderParts)
                renderPart.render();
        }
        ViewTools.moveOrtho2DView(-object.getPosition().x, -object.getPosition().y);
    }
}