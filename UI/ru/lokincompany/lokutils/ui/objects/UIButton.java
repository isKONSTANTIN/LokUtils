package ru.lokincompany.lokutils.ui.objects;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.positioning.Position;
import ru.lokincompany.lokutils.ui.positioning.PositioningSetter;

public class UIButton extends UIObject {

    UIText text;
    UIPanel panel;

    public UIButton() {
        this.panel = (UIPanel) new UIPanel().setPosition(new PositioningSetter(this::getPosition)).setSize(new PositioningSetter(this::getSize));
        this.text = (UIText) new UIText().setText("Button").setPosition(new PositioningSetter(Position.Center));
        panel.getCanvas().addObject(text);

        setSize(new Vector2f(100,30));
    }

    public UIText getText() {
        return text;
    }

    public UIPanel getPanel() {
        return panel;
    }

    @Override
    public void update(UIObject parent) {
        super.update(parent);

        panel.update(this);
    }
}
