package ru.lokincompany.lokutils.ui;

import ru.lokincompany.lokutils.render.RenderPart;

public abstract class UIRenderPart<T extends UIObject> extends RenderPart {
    protected T object;

    public UIRenderPart(T object){
        this.object = object;
    }
}
