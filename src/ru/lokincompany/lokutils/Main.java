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
    static int i = 0;

    public static void main(String[] args) {
        GLFW.init();

        Window window = new Window().createAndShow();

        window.getGlContext().bind();
        UIStyle.generateDefaultStyle();
        UIMainCanvas mainCanvas = (UIMainCanvas) new UIMainCanvas().setSize(new Vector2f(500,500));
        UIText text = new UIText().setText("Test Text!");

        UIPanel panel = (UIPanel)new UIPanel().setSize(new Vector2f()).setSize(new Vector2f(512,0));
        panel.getAnimations().addAnimation(new Animation("Test") {
            @Override
            public void update() {
                UIPanel panel = (UIPanel)object;
                panel.setRounded(softChange(panel.getRounded(), 0, 0.4f));
                softSetSizeObject(512,512, 1f);
            }

            @Override
            public void started() {
            }

            @Override
            public void stopped() {

            }
        });

        panel.getAnimations().addAnimation(new Animation("Test2") {
            @Override
            public void update() {
                UIPanel panel = (UIPanel)object;
                panel.setRounded(softChange(panel.getRounded(), 1, 1));
                softSetSizeObject(512,0, 1f);
            }

            @Override
            public void started() {

            }

            @Override
            public void stopped() {

            }
        });

        mainCanvas.addObject(panel).addObject(text);

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        window.getGlContext().unbind();

        Thread uiTread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    mainCanvas.update(null);
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        uiTread.setPriority(Thread.MIN_PRIORITY);
        uiTread.start();

        while (true) {
            i++;
            Vector2i resolution = window.getResolution();

            if (window.getInputs().keyboard.isKeyDown(GLFW_KEY_1)) {
                panel.getAnimations().startAnimation("Test").stopAnimation("Test2");
            }

            if (window.getInputs().keyboard.isKeyDown(GLFW_KEY_2)) {
                panel.getAnimations().startAnimation("Test2").stopAnimation("Test");
            }

            if (window.getInputs().keyboard.isKeyDown(GLFW_KEY_3)) {
                panel.getAnimations().stopAnimation("Test").stopAnimation("Test2");
            }

            mainCanvas.setSize(new Vector2f(resolution.getX(),resolution.getY()));
            window.getGlContext().bind();
            ViewTools.setOrtho2DView(new Vector4f(0, resolution.getX(), resolution.getY(), 0));
            mainCanvas.render();

            glBindTexture(GL_TEXTURE_2D, mainCanvas.getFbo().getTextureBuffer());
            glClearColor(0.6f,0.6f,0.6f,1);
            glClear(GL_COLOR_BUFFER_BIT);
            glColor4f(1,1,1, 1);

            GLFastTools.drawSquare(new Vector2f(0,0),new Vector2f(resolution.getX(),resolution.getY()));
            glBindTexture(GL_TEXTURE_2D, 0);

            window.update();

            window.getGlContext().unbind();
        }
    }
}
