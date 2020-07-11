package ru.konstanteam.lokutils;

import ru.konstanteam.lokutils.applications.Application;
import ru.konstanteam.lokutils.applications.ApplicationPreference;
import ru.konstanteam.lokutils.render.GLFW;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.ui.core.UIController;
import ru.konstanteam.lokutils.ui.core.maincanvas.UIMainCanvasSystem;
import ru.konstanteam.lokutils.ui.core.windows.UIWindowSystem;
import ru.konstanteam.lokutils.ui.core.windows.bar.BaseWindowBar;
import ru.konstanteam.lokutils.ui.core.windows.window.BaseWindow;
import ru.konstanteam.lokutils.ui.layout.Alignment;
import ru.konstanteam.lokutils.ui.layout.BaseLayout;
import ru.konstanteam.lokutils.ui.layout.FreeLayout;
import ru.konstanteam.lokutils.ui.objects.UIButton.UIButton;
import ru.konstanteam.lokutils.ui.objects.UICheckBox;
import ru.konstanteam.lokutils.ui.objects.UISeparate;
import ru.konstanteam.lokutils.ui.objects.UIText;
import ru.konstanteam.lokutils.ui.objects.UITextField;

public class Main extends Application<UIMainCanvasSystem> {
    Main() {
        super(
                new ApplicationPreference<>(UIMainCanvasSystem.class).setWindow(new Window().setResizable(true))
        );
    }

    public static void main(String[] args) {
        GLFW.init();

        new Main().open();
    }

    @Override
    public void initEvent() {
        FreeLayout layout = new FreeLayout();

        uiController.setLayout(layout);
        UITextField textField = new UITextField().setText(new UIText().setStyleFontName("TEST"));
        textField.size().set(() -> layout.size().get().setHeight(40).relativeTo(50, 0));

        layout.addObject(textField, Alignment.CENTER);
    }

}
