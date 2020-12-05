package ru.konstanteam.lokutils.testing;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.gui.core.maincanvas.GUIMainCanvasSystem;
import ru.konstanteam.lokutils.gui.layout.*;
import ru.konstanteam.lokutils.gui.objects.button.GUIButton;
import ru.konstanteam.lokutils.gui.objects.margin.GUIMargin;
import ru.konstanteam.lokutils.gui.objects.margin.Margin;
import ru.konstanteam.lokutils.gui.panels.scroll.ScrollPanel;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;

public class Main extends Application<GUIMainCanvasSystem> {
    public Main() {
        super(new GUIMainCanvasSystem());
    }

    public static void main(String[] args) {
        new Main().open();
    }

    @Override
    public void initEvent() {
        window.setResizable(true);
        window.setTitle("GUI Test");

        BaseLayout layout = new BaseLayout();

        for (int i = 0; i < 1000; i++){
            GUIButton button = new GUIButton();
            layout.addObject(button);
        }

        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.layout().addObject(new GUIMargin(layout, new Margin(5, 5, 5, 5)), Point.ZERO);
        scrollPanel.size().set(uiController.getLayout().size());
        layout.size().set(() -> new Size(scrollPanel.size().get().width, layout.minimumSize().get().height));

        this.uiController.getLayout().addObject(scrollPanel, Point.ZERO);
        this.window.setWindowCloseCallback((win) -> this.close());
    }
}