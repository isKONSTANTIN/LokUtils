package ru.konstanteam.lokutils.testing;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.gui.core.maincanvas.GUIMainCanvasSystem;
import ru.konstanteam.lokutils.gui.layout.Alignment;
import ru.konstanteam.lokutils.gui.layout.ListLayout;
import ru.konstanteam.lokutils.gui.objects.button.GUIButton;
import ru.konstanteam.lokutils.gui.objects.margin.GUIMargin;
import ru.konstanteam.lokutils.gui.objects.margin.Margin;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.render.Window;

public class Main extends Application<GUIMainCanvasSystem> {
    public Main() {
        super(new GUIMainCanvasSystem(), new Window());
    }

    public static void main(String[] args) {
        new Main().open();
    }

    @Override
    public void initEvent() {
        this.window.setWindowCloseCallback((win) -> this.close());
        ListLayout list = new ListLayout();

        GUIController.getLayout().addObject(list, Alignment.CENTER);
        list.size().set(GUIController.getLayout().size());

        list.addObject(new GUIMargin(new GUIButton("Fonts").setAction(() -> new FontsTest().open()), new Margin(5)));
        list.addObject(new GUIMargin(new GUIButton("Shader").setAction(() -> new ShaderTest().open()), new Margin(5)));
        list.addObject(new GUIMargin(new GUIButton("Dialog").setAction(() -> new DialogTest().open()), new Margin(5)));
    }
}