package ru.konstanteam.lokutils.input;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.util.vector.Vector2f;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Vector2i;
import ru.konstanteam.lokutils.render.GLContext;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    public int buttonID = GLFW_MOUSE_BUTTON_LEFT;

    protected Point mousePosition = Point.ZERO;
    protected Point lastMousePosition = new Point(-1, 0);
    protected Point mouseDeltaPosition = Point.ZERO;
    protected Vector2f mouseScroll = new Vector2f();
    protected Vector2f lastMouseScroll = new Vector2f();

    protected boolean mousePressed;
    private boolean mousePressedGLFW;
    protected boolean lastMousePressed;
    protected GLContext GLcontext;

    public Mouse() {
        GLcontext = GLContext.getCurrent();
        if (GLcontext == null) throw new RuntimeException("VBO cannot be created without OpenGL context!");

        glfwSetMouseButtonCallback(GLcontext.getWindow().getGLFWWindow(), new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                mousePressedGLFW = button == buttonID && action == GLFW_PRESS;
            }
        });

        glfwSetScrollCallback(GLcontext.getWindow().getGLFWWindow(), new GLFWScrollCallback() {
            @Override
            public void invoke(long l, double xoffset, double yoffset) {
                mouseScroll.x = (float) xoffset;
                mouseScroll.y = (float) yoffset;
            }
        });
    }

    public boolean getPressedStatus() {
        return mousePressed;
    }

    public boolean getLastMousePressed() {
        return lastMousePressed;
    }

    public Point getMousePosition() {
        return mousePosition;
    }

    public Point getMouseDeltaPosition() {
        return mouseDeltaPosition;
    }

    public Vector2f getMouseScroll() {
        return new Vector2f(mouseScroll.x, mouseScroll.y);
    }

    public void update() {
        if (!GLContext.check(GLcontext))
            throw new RuntimeException("Mouse cannot be updated without or another OpenGL context!");

        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(GLcontext.getWindow().getGLFWWindow(), xBuffer, yBuffer);

        mousePosition = mousePosition.setX((int) xBuffer.get(0)).setY((int) yBuffer.get(0));

        if (lastMousePosition.x != -1)
            mouseDeltaPosition = mouseDeltaPosition
                    .setX(mousePosition.x - lastMousePosition.x)
                    .setY(mousePosition.y - lastMousePosition.y);

        if (lastMouseScroll.equals(mouseScroll) && (mouseScroll.x != 0 || mouseScroll.y != 0)) {
            mouseScroll.x = 0;
            mouseScroll.y = 0;
        }

        lastMousePressed = mousePressed;

        mousePressed = mousePressedGLFW;

        lastMouseScroll = getMouseScroll();
        lastMousePosition = getMousePosition();
    }
}
