package ru.lokincompany.lokutils;

import org.lwjgl.opengl.GL11;
import ru.lokincompany.lokutils.applications.Application;

import ru.lokincompany.lokutils.applications.ApplicationPreference;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;
import ru.lokincompany.lokutils.objects.Vector2i;
import ru.lokincompany.lokutils.render.GLContext;
import ru.lokincompany.lokutils.render.GLFW;
import ru.lokincompany.lokutils.render.Texture;
import ru.lokincompany.lokutils.render.Window;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.core.windows.UIWindow;
import ru.lokincompany.lokutils.ui.core.windows.UIWindowSystem;
import ru.lokincompany.lokutils.ui.objects.*;

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

        window.getCanvas().addObject(button);

        UIButton button2 = new UIButton();
        button2.setPosition(TOP_CENTER);

        window2.getCanvas().addObject(button2);
    }

}
