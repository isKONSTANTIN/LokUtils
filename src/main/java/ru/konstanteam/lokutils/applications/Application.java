package ru.konstanteam.lokutils.applications;

import ru.konstanteam.lokutils.gui.style.GUIStyle;
import ru.konstanteam.lokutils.gui.core.GUIController;
import ru.konstanteam.lokutils.render.GLFW;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.tools.ExecutorServices;

import static org.lwjgl.opengl.GL11.*;

public class Application<T extends GUIController> implements Runnable {

    protected Window window;
    protected T GUIController;
    protected boolean opened;

    public Application(T GUIController, Window window) {
        this.GUIController = GUIController;
        this.window = window;

        opened = true;
    }

    public Application(T GUIController) {
        this(GUIController, new Window());
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
        glClearColor(0.9f, 0.9f, 0.9f, 1);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void updateEvent() {

    }

    public void initEvent() {

    }

    public void closeEvent() {

    }

    @Override
    public void run() {
        try {
            if (!GLFW.init())
                throw new RuntimeException("GLFW cannot be inited");

            window.create();
            window.getGlContext().bind();
            GUIStyle.generateDefaultStyle();

            GUIController.init(window);

            initEvent();

            window.getGlContext().unbind();

            window.show();
            while (opened) {
                window.getGlContext().bind();
                window.getGlContext().update();
                window.update();

                updateEvent();
                GUIController.update();

                renderEvent();
                GUIController.render();

                window.swapBuffer();
                window.getGlContext().unbind();
            }

            closeEvent();

            window.destroy();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
