package ru.konstanteam.lokutils.render.context;

import org.lwjgl.opengl.GL;
import ru.konstanteam.lokutils.render.Window;
import ru.konstanteam.lokutils.render.tools.ViewTools;

import java.util.HashMap;

public class GLContext {
    private static volatile HashMap<Long, ContextsList> contexts = new HashMap<>();

    private final Window window;
    private final long threadID;
    private ViewTools viewTools;

    public GLContext(Window window) {
        if (window.getGlContext() != null) throw new RuntimeException("This window already has context!");

        this.window = window;

        this.threadID = Thread.currentThread().getId();

        if (!contexts.containsKey(threadID)) {
            ContextsList contextsList = new ContextsList();
            contextsList.add(this);

            contexts.put(threadID, contextsList);
        } else
            contexts.get(threadID).add(this);

        try {
            bind();
            GL.createCapabilities();

            this.viewTools = new ViewTools(window);
            unbind();
        }catch (Exception e){
            e.printStackTrace();
        }

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

    public void update(){
        viewTools.update();
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
