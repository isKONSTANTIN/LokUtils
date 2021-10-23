package su.knst.lokutils.applications;

import su.knst.lokutils.gui.style.GUIStyle;
import su.knst.lokutils.gui.core.GUIController;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.render.GLFW;
import su.knst.lokutils.render.Window;
import su.knst.lokutils.render.tools.GLFastTools;
import su.knst.lokutils.tools.ExecutorServices;

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

    public void postRender(){

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
                window.update();
                glClearColor(0,0,0,0);
                glClear(GL_COLOR_BUFFER_BIT);
                window.getFbo().bind();

                Size resolution = window.getResolution();
                glViewport(0, 0, (int)resolution.width, (int)resolution.height);

                updateEvent();
                GUIController.update();

                renderEvent();
                GUIController.render();
                postRender();
                window.getFbo().unbind();

                GLFastTools.drawTexturedSquare(new Rect(window.getResolution()), window.getFbo().getTextureBuffer(), GLFastTools.invertedTexCoords);

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
