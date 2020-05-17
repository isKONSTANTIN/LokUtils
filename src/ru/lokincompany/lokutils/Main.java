package ru.lokincompany.lokutils;

import ru.lokincompany.lokutils.applications.Application;

import ru.lokincompany.lokutils.applications.ApplicationPreference;
import ru.lokincompany.lokutils.objects.Vector2i;
import ru.lokincompany.lokutils.render.GLFW;
import ru.lokincompany.lokutils.render.Window;
import ru.lokincompany.lokutils.ui.UIObject;
import ru.lokincompany.lokutils.ui.eventsystem.events.MouseEvent;
import ru.lokincompany.lokutils.ui.objects.UIButton;
import ru.lokincompany.lokutils.ui.objects.UICheckBox;
import ru.lokincompany.lokutils.ui.objects.UIMainCanvas;
import ru.lokincompany.lokutils.ui.positioning.Position;
import ru.lokincompany.lokutils.ui.positioning.PositioningSetter;

public class Main extends Application {
    public static void main(String[] args) {
        GLFW.init();

        new Main().open();
    }

    Main(){
        super(new ApplicationPreference());
    }

    @Override
    public void initEvent(){
        canvas.addObject(new UICheckBox().setPosition(new PositioningSetter(Position.Center)));
        UIButton button = new UIButton();
        button.getEventHandler().putEvent(new MouseEvent().setRealizedAction((event) -> System.out.println("На меня кликнули!")));
        canvas.addObject(button);
    }

}
