package ru.konstanteam.lokutils.render.context;

import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class ContextsList {
    private GLContext binded;

    private Vector<GLContext> contexts = new Vector<>();

    private final ReentrantLock lock = new ReentrantLock();

    public void add(GLContext context){
        contexts.add(context);
    }

    public GLContext getBinded() {
        return binded;
    }

    public synchronized void bind(GLContext context) {
        if (lock.isLocked())
            throw new RuntimeException("Cannot bind 2 contexts that have the same thread at the same time!");

        if (!contexts.contains(context))
            throw new RuntimeException("Context not found in thread list!");

        lock.lock();

        glfwMakeContextCurrent(context.getWindow().getGLFWWindow());
        GL.createCapabilities();
        binded = context;
    }

    public synchronized void unbind() {
        glfwMakeContextCurrent(0);
        lock.unlock();
    }
}
