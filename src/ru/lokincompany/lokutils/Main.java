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
        uiController.addWindow(window);

        UIWindow<UICanvas> window2 = new UIWindow<>(new UICanvas());
        window2.setPosition(new Point(30,40));
        uiController.addWindow(window2);

        UIButton button = new UIButton();
        button.setPosition(TOP_CENTER);
        button.setAction(() -> System.out.println("1"));
        UIButton button2 = new UIButton();

        button2.setPosition(BOTTOM_CENTER);
        button2.setAction(() -> System.out.println("2"));

        window.getCanvas().addObject(new UICheckBox().setText(new UIText().setText("Чек бокс")).setPosition(CENTER));
        window.getCanvas().addObject(button);
        window.getCanvas().addObject(button2);
        UIButton button3 = new UIButton();
        button3.setPosition(TOP_CENTER);

        window2.getCanvas().addObject(button3);
    }

}
