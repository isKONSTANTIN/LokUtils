package su.knst.lokutils.testing;

import su.knst.lokutils.applications.Application;
import su.knst.lokutils.gui.core.maincanvas.GUIMainCanvasSystem;
import su.knst.lokutils.gui.layout.Alignment;
import su.knst.lokutils.gui.layout.BaseLayout;
import su.knst.lokutils.gui.layout.ListLayout;
import su.knst.lokutils.gui.objects.*;
import su.knst.lokutils.gui.objects.*;
import su.knst.lokutils.gui.objects.button.GUIButton;
import su.knst.lokutils.gui.objects.margin.GUIMargin;
import su.knst.lokutils.gui.objects.slider.GUISlider;
import su.knst.lokutils.gui.style.GUIStyle;
import su.knst.lokutils.objects.Color;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.Window;
import su.knst.lokutils.render.text.Font;
import su.knst.lokutils.render.text.Style;

public class DialogTest extends Application<GUIMainCanvasSystem> {
    ListLayout layout;

    public DialogTest() {
        super(new GUIMainCanvasSystem(), new Window().setTitle("LokUtils - GUI test").setResizable(true));
    }

    @Override
    public void updateEvent() {
        super.updateEvent();
    }

    @Override
    public void initEvent() {
        this.window.setWindowCloseCallback((win) -> this.close());

        GUIStyle style = GUIStyle.generateDefaultStyle();
        style.asset(GUISeparate.class).color("separateColor", Color.BLACK.setAlpha(0.4f));
        style.asset(GUIText.class).font("title", new Font("TimesRoman", 36, Style.BOLD));

        this.GUIController.getLayout().setStyle(style);

        BaseLayout buttonsLayout = new BaseLayout();
        layout = new ListLayout();
        layout.minimumSize().set(layout.size().get());
        layout.size().track(() -> layout.minimumSize().get().setWidth(Math.max(window.getResolution().getX() / 2f, buttonsLayout.minimumSize().get().width)));
        layout.setGap(5);

        GUIText title = new GUIText().setStyleFontName("title");
        title.string().set("LOGIN");

        layout.addObject(title, Alignment.CENTER);

        GUISeparate separate = new GUISeparate();
        separate.size().track(() -> layout.size().get().setHeight(11), layout.size());
        separate.setLineSizePercent(1.2f);
        layout.addObject(separate);

        layout.addObject(new GUITitledTextField("Username:"));
        layout.addObject(new GUITitledTextField("Password:"));
        GUISlider slider = new GUISlider();
        slider.maximumSize().track(slider.size());
        layout.addObject(slider);

        layout.addObject(new GUILineSpace().setHeight(3));
        layout.addObject(new GUICheckBox().setText("Remember me"));
        layout.addObject(new GUILineSpace().setHeight(6));

        buttonsLayout.minimumSize().set(layout.size().get());
        buttonsLayout.size().track(buttonsLayout.minimumSize());

        GUIButton button = new GUIButton();
        button.getText().string().set("Login");
        buttonsLayout.addObject(button);

        button = new GUIButton();
        button.getText().string().set("Cancel");
        buttonsLayout.addObject(button);

        layout.addObject(buttonsLayout, Alignment.TOP_RIGHT);

        separate = new GUISeparate();
        separate.size().track(() -> layout.size().get().setHeight(11));
        separate.setLineSizePercent(1.2f);
        layout.addObject(separate);

        layout.maximumSize().set(new Size(200,200));

        GUIController.getLayout().addObject(new GUIMargin(layout), Alignment.CENTER);
    }
}
