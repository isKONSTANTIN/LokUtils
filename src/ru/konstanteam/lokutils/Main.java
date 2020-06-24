package ru.konstanteam.lokutils;

import ru.konstanteam.lokutils.applications.Application;

import ru.konstanteam.lokutils.applications.ApplicationPreference;
import ru.konstanteam.lokutils.render.GLFW;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.ui.core.windows.UIWindow;
import ru.konstanteam.lokutils.ui.core.windows.UIWindowSystem;
import ru.konstanteam.lokutils.ui.layout.BaseLayout;
import ru.konstanteam.lokutils.ui.layout.FreeLayout;
import ru.konstanteam.lokutils.ui.layout.StickyPosition;
import ru.konstanteam.lokutils.ui.objects.*;
import ru.konstanteam.lokutils.ui.objects.UIButton.UIButton;

public class Main extends Application<UIWindowSystem> {
    public static void main(String[] args) {
        GLFW.init();

        new Main().open();
    }

    Main(){
        super(new ApplicationPreference<>(UIWindowSystem.class).setWindow(new Window().setResizable(true)));
    }

    @Override
    public void initEvent() {
        UIWindow<FreeLayout> window = new UIWindow<>(new FreeLayout());
        window.setTitle("Привет, мир!");
        uiController.addWindow(window);

        UIButton button = new UIButton();
        button.setAction(() -> System.out.println("1"));

        window.getLayout().addObject(new UICheckBox().setText(new UIText().setText("Чек бокс")), StickyPosition.CENTER);
        window.getLayout().addObject(button, StickyPosition.BOTTOM_RIGHT);
    }

}
