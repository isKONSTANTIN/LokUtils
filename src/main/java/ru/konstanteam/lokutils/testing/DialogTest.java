package ru.konstanteam.lokutils.testing;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.gui.core.maincanvas.GUIMainCanvasSystem;
import ru.konstanteam.lokutils.gui.layout.Alignment;
import ru.konstanteam.lokutils.gui.layout.BaseLayout;
import ru.konstanteam.lokutils.gui.layout.ListLayout;
import ru.konstanteam.lokutils.gui.objects.*;
import ru.konstanteam.lokutils.gui.objects.button.GUIButton;
import ru.konstanteam.lokutils.gui.objects.margin.GUIMargin;
import ru.konstanteam.lokutils.gui.objects.slider.GUISlider;
import ru.konstanteam.lokutils.gui.style.GUIStyle;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.render.text.Font;
import ru.konstanteam.lokutils.render.text.Style;

public class DialogTest extends Application<GUIMainCanvasSystem> {
    public DialogTest() {
        super(new GUIMainCanvasSystem(), new Window());
    }

    @Override
    public void initEvent() {
        this.window.setWindowCloseCallback((win) -> this.close());

        GUIStyle style = GUIStyle.generateDefaultStyle();
        style.asset(GUISeparate.class).color("separateColor", Color.BLACK.setAlpha(0.4f));
        style.asset(GUIText.class).font("title", new Font("TimesRoman", 36, Style.BOLD));

        this.GUIController.getLayout().setStyle(style);

        window.setResizable(true);
        window.setTitle("GUI Test");

        BaseLayout buttonsLayout = new BaseLayout();
        ListLayout layout = new ListLayout();
        layout.minimumSize().set(layout.size().get());
        layout.size().set(() -> layout.minimumSize().get().setWidth(Math.max(window.getResolution().getX() / 2f, buttonsLayout.minimumSize().get().width)));
        layout.setGap(5);

        GUIText title = new GUIText().setStyleFontName("title");
        title.string().set("LOGIN");

        layout.addObject(title, Alignment.CENTER);

        GUISeparate separate = new GUISeparate();
        separate.size().set(() -> layout.size().get().setHeight(11));
        separate.setLineSizePercent(1.2f);
        layout.addObject(separate);

        layout.addObject(new GUITitledTextField("Username:"));
        layout.addObject(new GUITitledTextField("Password:"));
        GUISlider slider = new GUISlider();
        slider.maximumSize().set(slider.size());
        layout.addObject(slider);

        layout.addObject(new GUILineSpace().setHeight(3));
        layout.addObject(new GUICheckBox().setText("Remember me"));
        layout.addObject(new GUILineSpace().setHeight(6));

        buttonsLayout.minimumSize().set(layout.size().get());
        buttonsLayout.size().set(buttonsLayout.minimumSize());

        GUIButton button = new GUIButton();
        button.getText().string().set("Login");
        buttonsLayout.addObject(button);

        button = new GUIButton();
        button.getText().string().set("Cancel");
        buttonsLayout.addObject(button);

        layout.addObject(buttonsLayout, Alignment.TOP_RIGHT);

        separate = new GUISeparate();
        separate.size().set(() -> layout.size().get().setHeight(11));
        separate.setLineSizePercent(1.2f);
        layout.addObject(separate);

        layout.maximumSize().set(new Size(200,200));

        GUIController.getLayout().addObject(new GUIMargin(layout), Alignment.CENTER);
    }
}
