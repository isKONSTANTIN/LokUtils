package ru.lokincompany.lokutils.render;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class GLContext {

    private final Window window;
    private static volatile GLContext bindedContext;

    public GLContext(Window window) {
        this.window = window;
    }

    public Window getWindow() {
        return window;
    }

    public synchronized void bind() {
        while (glfwGetCurrentContext() != 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bindedContext = this;
        glfwMakeContextCurrent(window.getGLFWWindow());
        GL.createCapabilities();
    }

    public static boolean check(GLContext context){
        return bindedContext != null && context != null && context.getWindow().getGLFWWindow() == bindedContext.getWindow().getGLFWWindow();
    }

    public static GLContext getCurrent(){
        return bindedContext;
    }

    public synchronized void unbind() {
        glfwMakeContextCurrent(0);
        bindedContext = null;
    }
}
