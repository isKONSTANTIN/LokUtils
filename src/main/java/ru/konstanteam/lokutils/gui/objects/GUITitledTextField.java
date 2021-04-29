package ru.konstanteam.lokutils.gui.objects;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.layout.BaseLayout;
import ru.konstanteam.lokutils.gui.layout.ListLayout;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.tools.property.PropertyChangeListener;

public class GUITitledTextField extends GUIObject {
    protected ListLayout layout;
    protected GUITextField field;
    protected GUIText title;

    public GUITitledTextField(GUIText title) {
        this.title = title;

        layout = new ListLayout();
        layout.size().set(() -> size().get().setHeight(layout.minimumSize().get().height));

        layout.setGap(1);
        layout.addObject(title);

        field = new GUITextField();
        float initHeight = field.size().get().height;

        field.size().set(() -> size().get().setHeight(initHeight));

        layout.addObject(field);

        customersContainer.setCustomer(layout.getCustomersContainer());

        layout.update();
        size().set(layout.size().get().setWidth(100));
    }

    public GUITitledTextField(String title) {
        this(new GUIText());

        this.title.setStyleFontName("textFieldTitle").string().set(title);
    }

    public GUIText getTitle() {
        return title;
    }

    public GUITextField getField() {
        return field;
    }

    @Override
    public void update() {
        super.update();

        layout.update();
        layout.size().checkParentChanges();
    }

    @Override
    public void render() {
        layout.render();
    }
}
