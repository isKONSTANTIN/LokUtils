package ru.lokincompany.lokutils;

import ru.lokincompany.lokutils.applications.Application;

import ru.lokincompany.lokutils.applications.ApplicationPreference;
import ru.lokincompany.lokutils.render.GLFW;
import ru.lokincompany.lokutils.render.Window;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.objects.UIButton;
import ru.lokincompany.lokutils.ui.objects.UICheckBox;

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
    public void initEvent(){
        canvas.addObject(new UIButton().setPosition(CENTER));
    }

}
