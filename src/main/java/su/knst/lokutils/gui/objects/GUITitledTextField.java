package su.knst.lokutils.gui.objects;

import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.gui.layout.ListLayout;

public class GUITitledTextField extends GUIObject {
    protected ListLayout layout;
    protected GUITextField field;
    protected GUIText title;

    public GUITitledTextField(GUIText title) {
        this.title = title;

        layout = new ListLayout();
        layout.size().track(() -> size().get().setHeight(layout.minimumSize().get().height), layout.minimumSize(), size());

        layout.setGap(1);
        layout.addObject(title);

        field = new GUITextField();
        float initHeight = field.size().get().height;

        field.size().track(() -> size().get().setHeight(initHeight), size());

        layout.addObject(field);

        customersContainer.setCustomer(layout.getCustomersContainer());

        layout.calculateAll();
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
        //layout.size().checkParentChanges();
    }

    @Override
    public void render() {
        layout.render();
    }
}
