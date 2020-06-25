package ru.konstanteam.lokutils.applications;

import org.lwjgl.util.vector.Vector4f;
import ru.konstanteam.lokutils.objects.Vector2i;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.tools.ExecutorServices;
import ru.konstanteam.lokutils.ui.UIStyle;
import ru.konstanteam.lokutils.ui.core.UIController;
import ru.konstanteam.lokutils.ui.core.windows.UIWindowSystem;

import static org.lwjgl.opengl.GL11.*;

public class Application<T extends UIController> implements Runnable {

    protected Window window;
    protected T uiController;
    protected ApplicationPreference<T> preference;
    protected boolean opened;

    public Application(ApplicationPreference<T> preference) {
        this.preference = preference;
        window = preference.window;

        opened = true;
    }

    public Application() {
        this(new ApplicationPreference(UIWindowSystem.class));
    }

    public boolean isOpened() {
        return opened;
    }

    public void close() {
        this.opened = false;
    }

    public void open() {
        ExecutorServices.getCachedService().submit(this);
    }

    public void renderEvent() {
        glClearColor(0.6f, 0.6f, 0.6f, 1);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void updateEvent() {

    }

    public void initEvent() {

    }

    @Override
    public void run() {
        try {
            if (window == null)
                window = new Window();

            window.create();
            window.getGlContext().bind();
            UIStyle.generateDefaultStyle();

            uiController = (T) preference.uiController.getConstructors()[0].newInstance(window);

            initEvent();

            window.getGlContext().unbind();

            window.show();
            while (opened) {
                Vector2i resolution = window.getResolution();

                updateEvent();
                uiController.update();

                window.getGlContext().bind();

                window.getGlContext().getViewTools().setOrtho2DView(new Vector4f(0, resolution.getX(), resolution.getY(), 0));

                renderEvent();
                uiController.render();
                window.update();

                window.getGlContext().unbind();
            }

            window.destroy();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
