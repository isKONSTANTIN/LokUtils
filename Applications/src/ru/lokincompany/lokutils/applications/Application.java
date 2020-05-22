package ru.lokincompany.lokutils.applications;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import ru.lokincompany.lokutils.objects.Vector2i;
import ru.lokincompany.lokutils.render.Window;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.render.tools.ViewTools;
import ru.lokincompany.lokutils.tools.ExecutorServices;
import ru.lokincompany.lokutils.ui.UIStyle;
import ru.lokincompany.lokutils.ui.objects.UIMainCanvas;

import static org.lwjgl.opengl.GL11.*;

public class Application implements Runnable {

    protected Runnable canvasTask;
    protected Window window;
    protected UIMainCanvas canvas;
    protected boolean opened;

    public Application(ApplicationPreference preference){
        window = preference.window;

        canvasTask = () -> {
            long time;
            long sleepTime;

            try {

                while (opened) {
                    time = System.nanoTime();
                    canvas.update(null);
                    sleepTime = 16 - (System.nanoTime() - time) / 1000000;

                    if (sleepTime > 0)
                        Thread.sleep(16);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        opened = true;
    }

    public Application(){
        this(new ApplicationPreference());
    }

    public boolean isOpened() {
        return opened;
    }

    public void close() {
        this.opened = false;
    }

    public void open(){
        ExecutorServices.getCachedService().submit(this);
    }

    public void renderEvent(){
        glClearColor(0.6f, 0.6f, 0.6f, 1);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void updateEvent(){

    }

    public void initEvent(){

    }

    @Override
    public void run() {
        if (window == null)
            window = new Window();

        window.create();
        window.getGlContext().bind();
        UIStyle.generateDefaultStyle();

        canvas = new UIMainCanvas().setMultisampleSamples(16);

        initEvent();

        window.getGlContext().unbind();

        ExecutorServices.getCachedService().submit(canvasTask);

        window.show();
        while (opened){
            Vector2i resolution = window.getResolution();

            updateEvent();

            window.getGlContext().bind();

            ViewTools.setOrtho2DView(new Vector4f(0, resolution.getX(), resolution.getY(), 0));
            canvas.setSize(new Vector2f(resolution.getX(), resolution.getY()));

            canvas.getFbo().bind();

            renderEvent();

            canvas.getFbo().unbind();

            canvas.render();

            glBindTexture(GL_TEXTURE_2D, canvas.getFbo().getTextureBuffer());

            glColor4f(1, 1, 1, 1);

            GLFastTools.drawInvertedSquare(new Vector2f(0, 0), new Vector2f(resolution.getX(), resolution.getY()));
            glBindTexture(GL_TEXTURE_2D, 0);

            window.update();

            window.getGlContext().unbind();
        }

        window.destroy();

    }
}
