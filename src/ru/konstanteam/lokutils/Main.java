package ru.konstanteam.lokutils;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.render.GLFW;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.ui.core.maincanvas.UIMainCanvasSystem;
import ru.konstanteam.lokutils.ui.core.windows.UIWindowSystem;
import ru.konstanteam.lokutils.ui.layout.Alignment;
import ru.konstanteam.lokutils.ui.layout.FreeLayout;
import ru.konstanteam.lokutils.ui.layout.ListLayout;
import ru.konstanteam.lokutils.ui.layout.ScrollLayout;
import ru.konstanteam.lokutils.ui.objects.UIBlackout;
import ru.konstanteam.lokutils.ui.objects.UITextField;

import java.util.Random;

public class Main extends Application<UIMainCanvasSystem<FreeLayout>> {
    public Main() {
        super(
                new UIMainCanvasSystem<>(new FreeLayout())
        );
    }

    public static void main(String[] args) {
        new Main().open();
    }

    @Override
    public void initEvent() {
        window.setWindowCloseCallback((window) -> this.close());
        UITextField field = new UITextField().setEnterAction(() -> System.out.println(new Random().nextInt()));
        ScrollLayout layout = new ScrollLayout();

        layout.addObject(field, Alignment.CENTER);
        UIBlackout blackout = new UIBlackout();
        blackout.size().set(layout.size());

        uiController.getLayout().addObject(blackout, Alignment.CENTER);
        uiController.getLayout().addObject(layout, Alignment.CENTER);
    }

}
