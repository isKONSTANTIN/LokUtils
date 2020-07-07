package ru.konstanteam.lokutils;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.applications.ApplicationPreference;
import ru.konstanteam.lokutils.render.GLFW;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.ui.core.windows.UIWindowSystem;
import ru.konstanteam.lokutils.ui.core.windows.bar.BaseWindowBar;
import ru.konstanteam.lokutils.ui.core.windows.window.BaseWindow;
import ru.konstanteam.lokutils.ui.layout.Alignment;
import ru.konstanteam.lokutils.ui.layout.FreeLayout;
import ru.konstanteam.lokutils.ui.objects.UIButton.UIButton;
import ru.konstanteam.lokutils.ui.objects.UICheckBox;
import ru.konstanteam.lokutils.ui.objects.UIText;

public class Main extends Application<UIWindowSystem> {
    Main() {
        super(new ApplicationPreference<>(UIWindowSystem.class).setWindow(new Window().setResizable(true)));
    }

    public static void main(String[] args) {
        GLFW.init();

        new Main().open();
    }

    @Override
    public void initEvent() {
        BaseWindow<FreeLayout, BaseWindowBar> window = new BaseWindow<>(new FreeLayout());
        uiController.addWindow(window);
        uiController.addWindow(new BaseWindow<>(new FreeLayout()));
        window.getBar().setTitle("Привет, мир!");

        UIButton button = new UIButton();
        button.setAction(() -> System.out.println("1"));

        window.getLayout().addObject(new UICheckBox().setText(new UIText().setText("Чек бокс")), Alignment.CENTER);
        window.getLayout().addObject(button, Alignment.BOTTOM_RIGHT);
    }

}
