package ru.konstanteam.lokutils.applications;

import org.lwjgl.util.vector.Vector4f;
import ru.konstanteam.lokutils.gui.GUIStyle;
import ru.konstanteam.lokutils.gui.core.GUIController;
import ru.konstanteam.lokutils.objects.Vector2i;
import ru.konstanteam.lokutils.render.GLFW;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.tools.ExecutorServices;

import static org.lwjgl.opengl.GL11.*;

public class Application<T extends GUIController> implements Runnable {

    protected Window window;
    protected T uiController;
    protected boolean opened;

    public Application(T uiController, Window window) {
        this.uiController = uiController;
        this.window = window;

        opened = true;
    }

    public Application(T uiController) {
        this(uiController, new Window());
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
            if (!GLFW.init())
                throw new RuntimeException("GLFW cannot be inited");

            window.create();
            window.getGlContext().bind();
            GUIStyle.generateDefaultStyle();

            uiController.init(window, GUIStyle.getDefault());

            initEvent();

            window.getGlContext().unbind();

            window.show();
            while (opened) {
                Vector2i resolution = window.getResolution();

                updateEvent();
                uiController.update();

                window.getGlContext().bind();
                window.getGlContext().update();

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
