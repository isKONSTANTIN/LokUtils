package ru.lokincompany.lokutils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import ru.lokincompany.lokutils.input.Keyboard;
import ru.lokincompany.lokutils.objects.Color;
import ru.lokincompany.lokutils.objects.Vector4i;
import ru.lokincompany.lokutils.render.FBO;
import ru.lokincompany.lokutils.render.GLFW;
import ru.lokincompany.lokutils.render.RenderPart;
import ru.lokincompany.lokutils.render.Window;
import ru.lokincompany.lokutils.render.tools.GLFastTools;
import ru.lokincompany.lokutils.render.tools.MatrixTools;
import ru.lokincompany.lokutils.objects.Vector2i;
import ru.lokincompany.lokutils.render.tools.ViewTools;
import ru.lokincompany.lokutils.ui.UIStyle;
import ru.lokincompany.lokutils.ui.animation.Animation;
import ru.lokincompany.lokutils.ui.objects.UICanvas;
import ru.lokincompany.lokutils.ui.objects.UIMainCanvas;
import ru.lokincompany.lokutils.ui.objects.UIPanel;
import ru.lokincompany.lokutils.ui.objects.UIText;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

public class Main {
    public static void main(String[] args) {
        GLFW.init();

        Window window = new Window().create();

        window.getGlContext().bind();
        UIStyle.generateDefaultStyle();
        UIMainCanvas canvas = new UIMainCanvas();
        UIPanel panel = (UIPanel)new UIPanel().setName("Panel");
        canvas.addObject(panel);
        panel.getCanvas().addObject(new UIText().setText("TEST"));
        window.getGlContext().unbind();

        while (true) {
            Vector2i resolution = window.getResolution();
            canvas.update(null);
            window.getGlContext().bind();

            ViewTools.setOrtho2DView(new Vector4f(0, resolution.getX(), resolution.getY(), 0));
            canvas.setSize(new Vector2f(resolution.getX(), resolution.getY()));
            canvas.render();
            glBindTexture(GL_TEXTURE_2D, canvas.getFbo().getTextureBuffer());
            glClearColor(0.6f,0.6f,0.6f,1);
            glClear(GL_COLOR_BUFFER_BIT);
            glColor4f(1,1,1, 1);

            GLFastTools.drawSquare(new Vector2f(0,0),new Vector2f(resolution.getX(),resolution.getY()));
            glBindTexture(GL_TEXTURE_2D, 0);

            if (!window.isShow())
                window.show();

            window.update();

            window.getGlContext().unbind();
        }
    }
}
