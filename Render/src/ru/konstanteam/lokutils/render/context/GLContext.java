package ru.konstanteam.lokutils.render.context;

import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.render.tools.ViewTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class GLContext {
    private static volatile HashMap<Long, ContextsList> contexts = new HashMap<>();

    private final Window window;
    private final long threadID;
    private final ViewTools viewTools;

    public GLContext(Window window) {
        if (window.getGlContext() != null) throw new RuntimeException("This window already has context!");

        this.window = window;

        this.threadID = Thread.currentThread().getId();
        this.viewTools = new ViewTools(window);

        if (!contexts.containsKey(threadID)){
            ContextsList contextsList = new ContextsList();
            contextsList.add(this);

            contexts.put(threadID, contextsList);
        }else
            contexts.get(threadID).add(this);
    }

    public static boolean check(GLContext context) {
        GLContext current = getCurrent();

        return current != null && current.equals(context);
    }

    public static GLContext getCurrent() {
        long currentThread = Thread.currentThread().getId();

        if (!contexts.containsKey(currentThread))
            return null;

        return contexts.get(currentThread).getBinded();
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
        contexts.get(threadID).bind(this);
    }

    public synchronized void unbind() {
        contexts.get(threadID).unbind();
    }
}
