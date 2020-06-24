package ru.konstanteam.lokutils.input;

import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import ru.konstanteam.lokutils.render.GLContext;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {

    protected GLFWKeyCallback callbackKey;
    protected GLFWCharCallback callbackChar;
    protected ArrayList<KeyInfo> keysPressed = new ArrayList<>();
    protected GLContext GLcontext;

    public Keyboard() {
        GLcontext = GLContext.getCurrent();
        if (GLcontext == null) throw new RuntimeException("Keyboard cannot be created without OpenGL context!");

        callbackKey = GLFWKeyCallback.create(this::keyCallback);
        callbackChar = GLFWCharCallback.create(this::charCallback);

        glfwSetKeyCallback(GLcontext.getWindow().getGLFWWindow(), callbackKey);
        glfwSetCharCallback(GLcontext.getWindow().getGLFWWindow(), callbackChar);
    }

    public boolean next() {
        return keysPressed.size() > 0;
    }

    public KeyInfo getPressedKey() {
        if (keysPressed.size() > 0) {
            KeyInfo key = keysPressed.get(0);
            keysPressed.remove(0);
            return key;
        }
        return null;
    }

    private void keyCallback(long window, int key, int scancode, int action, int mods) {
        keysPressed.add(new KeyInfo((char) 0, key, action, mods));
    }

    private void charCallback(long window, int key) {
        keysPressed.add(new KeyInfo((char) key, 0, 0, 0));
    }

    public void close() {
        callbackKey.close();
        callbackChar.close();
    }

    public boolean isKeyDown(int glfwKey) {
        return glfwGetKey(GLcontext.getWindow().getGLFWWindow(), glfwKey) == 1;
    }
}
