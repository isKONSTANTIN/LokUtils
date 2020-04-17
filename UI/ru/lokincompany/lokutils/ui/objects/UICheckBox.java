package ru.lokincompany.lokutils.ui.objects;

import ru.lokincompany.lokutils.render.Texture;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.UIRenderPart;

public class UICheckBox extends UIObject {
    protected UICheckBoxRender render;

    public UICheckBox(){
        render = new UICheckBoxRender(this);
    }

    @Override
    public void update(UIObject parent) {
        super.update(parent);

        parent.getCanvasParent().addRenderPart(render);
    }
}

class UICheckBoxRender extends UIRenderPart<UICheckBox> {
    public UICheckBoxRender(UICheckBox object) {
        super(object);
    }

    @Override
    public void render() {

    }
}