package ru.lokincompany.lokutils;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import ru.lokincompany.lokutils.objects.Vector2i;
import ru.lokincompany.lokutils.render.GLFW;
import ru.lokincompany.lokutils.render.Window;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.render.tools.ViewTools;
import ru.lokincompany.lokutils.ui.UIStyle;
import ru.lokincompany.lokutils.ui.objects.UIButton;
import ru.lokincompany.lokutils.ui.objects.UICheckBox;
import ru.lokincompany.lokutils.ui.objects.UIMainCanvas;
import ru.lokincompany.lokutils.ui.positioning.Position;
import ru.lokincompany.lokutils.ui.positioning.PositioningSetter;

import static org.lwjgl.opengl.GL11.*;

class App implements Runnable {

    @Override
    public void run() {
        Window window = new Window().setResizable(true).create();

        window.getGlContext().bind();
        UIStyle.generateDefaultStyle();
        UIMainCanvas canvas = new UIMainCanvas().setMultisampleSamples(16);

        canvas.addObject(new UICheckBox().setPosition(new PositioningSetter(Position.Center)));

        window.getGlContext().unbind();

        while (true) {
            Vector2i resolution = window.getResolution();

            canvas.update(null);
            window.getGlContext().bind();

            ViewTools.setOrtho2DView(new Vector4f(0, resolution.getX(), resolution.getY(), 0));

            canvas.setSize(new Vector2f(resolution.getX(), resolution.getY()));

            canvas.getFbo().bind();

            glClearColor(0.6f, 0.6f, 0.6f, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            canvas.getFbo().unbind();

            canvas.render();

            glBindTexture(GL_TEXTURE_2D, canvas.getFbo().getTextureBuffer());

            glColor4f(1, 1, 1, 1);

            GLFastTools.drawInvertedSquare(new Vector2f(0, 0), new Vector2f(resolution.getX(), resolution.getY()));
            glBindTexture(GL_TEXTURE_2D, 0);

            if (!window.isShow())
                window.show();

            window.update();

            window.getGlContext().unbind();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        GLFW.init();

        new Thread(new App()).start();
        new Thread(new App()).start();
    }
}
