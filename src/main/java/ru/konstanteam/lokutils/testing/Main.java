package ru.konstanteam.lokutils.testing;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.GUIStyle;
import ru.konstanteam.lokutils.gui.core.maincanvas.GUIMainCanvasSystem;
import ru.konstanteam.lokutils.gui.layout.*;
import ru.konstanteam.lokutils.gui.objects.*;
import ru.konstanteam.lokutils.gui.objects.button.GUIButton;
import ru.konstanteam.lokutils.gui.objects.margin.GUIMargin;
import ru.konstanteam.lokutils.gui.objects.slider.GUISlider;
import ru.konstanteam.lokutils.gui.panels.scroll.ScrollPanel;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.Font;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.testing.shader.ShaderTest;

import static org.lwjgl.opengl.GL11.*;

public class Main extends Application<GUIMainCanvasSystem> {
    public static void main(String[] args) {
        if (args.length < 1)
            new Main().open();
        else
            new ShaderTest().open();
    }

    public Main() {
        super(new GUIMainCanvasSystem(), new Window());
    }

    @Override
    public void initEvent() {
        GUIStyle.getDefault().setColor("separateColor", Color.BLACK.setAlpha(0.4f));
        GUIStyle.getDefault().setFont("title", new Font().load(new java.awt.Font("TimesRoman", java.awt.Font.BOLD, 36)));

        window.setResizable(true);
        window.setTitle("GUI Test");

        BaseLayout buttonsLayout = new BaseLayout();
        ListLayout layout = new ListLayout();
        layout.minimumSize().set(layout.size().get());
        layout.size().set(() -> layout.minimumSize().get().setWidth(Math.max(window.getResolution().getX() / 2f, buttonsLayout.minimumSize().get().width)));
        layout.setGap(5);

        GUIText title = new GUIText().setStyleFontName("title").setText("LOGIN");

        layout.addObject(title, Alignment.CENTER);

        GUISeparate separate = new GUISeparate();
        separate.size().set(() -> layout.size().get().setHeight(11));
        separate.setLineSizePercent(1.2f);
        layout.addObject(separate);

        GUITitledTextField usernameField = new GUITitledTextField("Username:");
        float initHeight = usernameField.size().get().height;
        usernameField.size().set(() -> layout.size().get().setHeight(initHeight));
        layout.addObject(usernameField);

        GUITitledTextField passField = new GUITitledTextField("Password:");
        passField.size().set(() -> layout.size().get().setHeight(initHeight));
        layout.addObject(passField);

        GUISlider slider = new GUISlider();
        layout.addObject(slider);

        layout.addObject(new GUILineSpace().setHeight(3));

        layout.addObject(new GUICheckBox().setText("Remember me"));

        layout.addObject(new GUILineSpace().setHeight(6));

        buttonsLayout.minimumSize().set(layout.size().get());
        buttonsLayout.size().set(buttonsLayout.minimumSize());

        buttonsLayout.minimumSize().addListener((oldV, newV) -> {
            layout.minimumSize().set(layout.minimumSize().get().setWidth(newV.width));
        });

        GUIButton button = new GUIButton();
        button.getText().setText("Login");
        buttonsLayout.addObject(button);

        button = new GUIButton();
        button.getText().setText("Cancel");
        buttonsLayout.addObject(button);

        layout.addObject(buttonsLayout, Alignment.TOP_RIGHT);

        separate = new GUISeparate();
        separate.size().set(() -> layout.size().get().setHeight(11));
        separate.setLineSizePercent(1.2f);
        layout.addObject(separate);

        ScrollPanel panel = new ScrollPanel();
        panel.layout().addObject(new GUIMargin(layout), Point.ZERO);
        uiController.getLayout().addObject(panel, Alignment.CENTER);

        this.window.setWindowCloseCallback((win) -> this.close());
    }
}