package ru.lokincompany.lokutils.objects.glfw;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.objects.Vector2i;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    public int buttonID = GLFW_MOUSE_BUTTON_LEFT;
    private Vector2i mousePosition = new Vector2i();
    private Vector2i lastMousePosition = new Vector2i(-1, 0);
    private Vector2i mouseDeltaPosition = new Vector2i();
    private Vector2f mouseScroll = new Vector2f();
    private Vector2f lastMouseScroll = new Vector2f();
    private boolean mousePressed = false;
    private long windowID;

    public Mouse(long windowID) {
        this.windowID = windowID;

        glfwSetMouseButtonCallback(windowID, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                mousePressed = button == buttonID && action == GLFW_PRESS;
            }
        });

        glfwSetScrollCallback(windowID, new GLFWScrollCallback() {
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

    public Vector2i getMousePosition() {
        return new Vector2i(mousePosition.getX(), mousePosition.getY());
    }

    public Vector2i getMouseDeltaPosition() {
        return new Vector2i(mouseDeltaPosition.getX(), mouseDeltaPosition.getY());
    }

    public Vector2f getMouseScroll() {
        return new Vector2f(mouseScroll.x, mouseScroll.y);
    }

    public void update() {
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(windowID, xBuffer, yBuffer);

        mousePosition.setX((int)xBuffer.get(0));
        mousePosition.setY((int)yBuffer.get(0));

        if (lastMousePosition.getX() != -1){
            mouseDeltaPosition.setX(mousePosition.getX()- lastMousePosition.getX());
            mouseDeltaPosition.setY(mousePosition.getY() - lastMousePosition.getY());
        }

        if (lastMouseScroll.equals(mouseScroll) && (mouseScroll.x != 0 || mouseScroll.y != 0)) {
            mouseScroll.x = 0;
            mouseScroll.y = 0;
        }
        lastMouseScroll = getMouseScroll();
        lastMousePosition = getMousePosition();
    }

    public boolean inField(Vector2i position, Vector2i size) {
        return (mousePosition.getX() >= position.getX() && mousePosition.getX() <= size.getX() + position.getX()) &&
                (mousePosition.getY() >= position.getY() && mousePosition.getY() <= size.getY() + position.getY());
    }
}
