package ru.lokincompany.lokutils;

import ru.lokincompany.lokutils.applications.Application;

import ru.lokincompany.lokutils.applications.ApplicationPreference;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.render.GLFW;
import ru.lokincompany.lokutils.render.Window;
import ru.lokincompany.lokutils.ui.core.windows.UIWindow;
import ru.lokincompany.lokutils.ui.core.windows.UIWindowSystem;
import ru.lokincompany.lokutils.ui.objects.*;
import ru.lokincompany.lokutils.ui.objects.UIButton.UIButton;

import static ru.lokincompany.lokutils.ui.positioning.AdvancedRect.*;

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
        UIWindow<UICanvas> window = new UIWindow<>(new UICanvas());
        window.setTitle("Привет, мир!");
        uiController.addWindow(window);

        UIButton button = new UIButton();
        button.setAction(() -> System.out.println("1"));

        window.getCanvas().addObject(new UICheckBox().setText(new UIText().setText("Чек бокс")), CENTER);
        window.getCanvas().addObject(button);
    }

}
