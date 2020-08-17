package ru.konstanteam.lokutils;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.applications.ApplicationPreference;
import ru.konstanteam.lokutils.render.GLFW;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.ui.core.maincanvas.UIMainCanvasSystem;
import ru.konstanteam.lokutils.ui.core.windows.UIWindowSystem;
import ru.konstanteam.lokutils.ui.core.windows.window.BaseWindow;
import ru.konstanteam.lokutils.ui.layout.Alignment;
import ru.konstanteam.lokutils.ui.layout.FreeLayout;
import ru.konstanteam.lokutils.ui.objects.UILineSpace;
import ru.konstanteam.lokutils.ui.objects.UITextField;

import java.util.Random;

public class Main extends Application<UIMainCanvasSystem<FreeLayout>> {
    public static void main(String[] args) {
        GLFW.init();

        new Main().open();
    }

    @Override
    public void initEvent() {
        window.setWindowCloseCallback((window) -> this.close());
        uiController.getLayout().addObject(new UITextField().setEnterAction(() -> System.out.println(new Random().nextInt())), Alignment.CENTER);

    }

}
