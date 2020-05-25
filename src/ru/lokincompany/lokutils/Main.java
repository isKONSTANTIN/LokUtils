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
import ru.lokincompany.lokutils.ui.objects.*;

import java.io.IOException;

import static ru.lokincompany.lokutils.ui.positioning.AdvancedRect.*;

public class Main extends Application {
    public static void main(String[] args) {
        GLFW.init();

        new Main().open();
    }

    Main(){
        super(new ApplicationPreference().setWindow(new Window().setResizable(true)));
    }

    @Override
    public void initEvent() {
        UIPanel panel = new UIPanel();
        panel.setPosition(CENTER_RIGHT).setSize(new Size(200,300));

        UIButton button = new UIButton();
        button.setPosition(TOP_CENTER);

        panel.getCanvas().addObject(button);

        canvas.addObject(panel);
    }

}
