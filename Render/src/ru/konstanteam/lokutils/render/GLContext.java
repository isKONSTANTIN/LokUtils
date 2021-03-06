package ru.konstanteam.lokutils.render;

import org.lwjgl.opengl.GL;
import ru.konstanteam.lokutils.render.tools.ViewTools;

import java.util.concurrent.locks.ReentrantLock;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class GLContext {
    private final static ReentrantLock lock = new ReentrantLock();
    private static volatile GLContext bindedContext;
    private final Window window;
    private final long threadID;
    private ViewTools viewTools;

    public GLContext(Window window) {
        if (window.glContext != null) throw new RuntimeException("This window already has context!");

        threadID = Thread.currentThread().getId();
        viewTools = new ViewTools(window);
        this.window = window;
    }

    public static boolean check(GLContext context) {
        return bindedContext != null && context != null && context.getWindow().getGLFWWindow() == bindedContext.getWindow().getGLFWWindow();
    }

    public static GLContext getCurrent() {
        return bindedContext;
    }

    public Window getWindow() {
        return window;
    }

    public ViewTools getViewTools() {
        return viewTools;
    }

    public long getThreadID() {
        return threadID;
    }

    public synchronized void bind() {
        if (check(this)) return;

        if (bindedContext != null && bindedContext.getThreadID() == Thread.currentThread().getId()) {
            throw new RuntimeException("Cannot bind 2 contexts that have the same thread at the same time!");
        }

        try {
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bindedContext = this;
        glfwMakeContextCurrent(window.getGLFWWindow());
        GL.createCapabilities();
    }

    public synchronized void unbind() {
        glfwMakeContextCurrent(0);
        bindedContext = null;
        lock.unlock();
    }
}
