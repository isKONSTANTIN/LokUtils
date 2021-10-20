package su.knst.lokutils.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import su.knst.lokutils.input.Inputs;
import su.knst.lokutils.objects.Vector2i;
import su.knst.lokutils.objects.Vector4i;
import su.knst.lokutils.render.context.GLContext;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    protected long window;

    protected Vector2i resolution = new Vector2i(512, 512);
    protected Vector2i position = new Vector2i(-1, -1);
    protected Vector2i aspectRatio = new Vector2i(-1, -1);
    protected Vector4i resolutionLimits = new Vector4i(GLFW_DONT_CARE, GLFW_DONT_CARE, GLFW_DONT_CARE, GLFW_DONT_CARE);

    protected String title = "Window";

    protected boolean isFullscreen;
    protected boolean isCreated;
    protected boolean isShow;
    protected boolean isResizable;
    protected String[] iconPaths;

    protected int MSAASamples = 8;

    protected GLContext glContext;
    protected Inputs inputs;
    protected Monitor currentMonitor;
    protected boolean decorated = true;
    protected HashMap<Integer, Integer> windowCreationHints = new HashMap<>();

    public Window() {

    }

    public void update() {
        inputs.update();
        Vector2i resolution = getResolution();
        glViewport(0, 0, resolution.getX(), resolution.getY());

        glContext.update();
    }

    public void swapBuffer() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public Window create() {
        if (isCreated) return this;

        currentMonitor = Monitor.getPrimary();

        Vector2i monitorResolution = new Vector2i(currentMonitor.getVideoMode().width(), currentMonitor.getVideoMode().height());

        if (isFullscreen)
            resolution = monitorResolution;

        glfwDefaultWindowHints();

        for (Map.Entry<Integer, Integer> hint : windowCreationHints.entrySet())
            glfwWindowHint(hint.getKey(), hint.getValue());

        glfwWindowHint(GLFW_SAMPLES, MSAASamples);
        glfwWindowHint(GLFW_DECORATED, decorated ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        windowCreationHints.clear();
        windowCreationHints = null;

        window = glfwCreateWindow(resolution.getX(), resolution.getY(), title, isFullscreen ? currentMonitor.getMonitor() : NULL, NULL);

        isCreated = window != NULL;
        if (!isCreated) return null;

        if (iconPaths != null)
            setIcon(iconPaths);

        glContext = new GLContext(this);

        glContext.bind();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER, 0.0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        inputs = new Inputs();

        glContext.unbind();

        if (!isFullscreen) {
            if (position.getX() == -1) {
                position = new Vector2i(
                        monitorResolution.getX() / 2 - resolution.getX() / 2,
                        monitorResolution.getY() / 2 - resolution.getY() / 2
                );
                setPosition(position);
            }
            updateResolutionLimits();

            if (aspectRatio.getX() != -1)
                setAspectRatio(aspectRatio);

            setResizable(isResizable);
        }

        return this;
    }

    public Window setHint(int hint, int value){
        windowCreationHints.put(hint, value);

        return this;
    }

    public Window createAndShow() {
        create();
        show();

        return this;
    }

    public boolean isDecorated() {
        return decorated;
    }

    public Window setDecorated(boolean decorated) {
        this.decorated = decorated;

        return this;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public boolean isShow() {
        return isShow;
    }

    public GLContext getGlContext() {
        return glContext;
    }

    public long getGLFWWindow() {
        return window;
    }

    public Inputs getInputs() {
        return inputs;
    }

    public Window setResizable(boolean resizable) {
        if (isCreated) glfwSetWindowAttrib(window, GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);

        this.isResizable = resizable;

        return this;
    }

    public Vector2f getContentScale() {
        if (!isCreated) return new Vector2f();

        FloatBuffer xBuffer = BufferUtils.createFloatBuffer(1);
        FloatBuffer yBuffer = BufferUtils.createFloatBuffer(1);

        glfwGetWindowContentScale(window, xBuffer, yBuffer);

        return new Vector2f(xBuffer.get(0), yBuffer.get(0));
    }

    public Monitor getMonitor() {
        return currentMonitor;
    }

    public Window setAspectRatio(Vector2i aspectRatio) {
        if (isCreated) glfwSetWindowAspectRatio(window, aspectRatio.getX(), aspectRatio.getY());

        this.aspectRatio = aspectRatio;

        return this;
    }

    public Vector4i getResolutionLimits() {
        return resolutionLimits;
    }

    public Window setResolutionLimits(Vector2i minResolution) {
        return setResolutionLimits(minResolution, null);
    }

    public Window setResolutionLimits(Vector2i minResolution, Vector2i maxResolution) {
        if (minResolution != null) {
            resolutionLimits.setX(minResolution.getX());
            resolutionLimits.setY(minResolution.getY());
        }

        if (maxResolution != null) {
            resolutionLimits.setZ(maxResolution.getX());
            resolutionLimits.setW(maxResolution.getY());
        }

        updateResolutionLimits();

        return this;
    }

    public int getMSAASamples() {
        return MSAASamples;
    }

    public Window setMSAASamples(int MSAASamples) {
        this.MSAASamples = MSAASamples;

        return this;
    }

    public Vector2i getResolution() {
        if (!isCreated) return resolution;

        IntBuffer xBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer yBuffer = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(window, xBuffer, yBuffer);

        return new Vector2i(xBuffer.get(0), yBuffer.get(0));
    }

    public Window setResolution(Vector2i resolution) {
        if (isCreated) glfwSetWindowSize(window, resolution.getX(), resolution.getY());
        this.resolution = resolution;

        return this;
    }

    public Vector2i getPosition() {
        if (!isCreated) return position;

        IntBuffer xBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer yBuffer = BufferUtils.createIntBuffer(1);

        glfwGetWindowPos(window, xBuffer, yBuffer);

        return new Vector2i(xBuffer.get(), yBuffer.get());
    }

    public Window setPosition(Vector2i position) {
        if (isCreated)
            glfwSetWindowPos(window, position.getX(), position.getY());
        else
            this.position = position;

        return this;
    }

    public Window movePosition(Vector2i delta) {
        setPosition(new Vector2i(position.getX() + delta.getX(), position.getY() + delta.getY()));

        return this;
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }

    public Window setFullscreen(boolean fullscreen) {
        Monitor monitor = getMonitor();

        if (isCreated)
            glfwSetWindowMonitor(window,
                    fullscreen ? monitor.getMonitor() : NULL,
                    fullscreen ? 0 : position.getX(),
                    fullscreen ? 0 : position.getY(),
                    fullscreen ? monitor.getVideoMode().width() : resolution.getX(),
                    fullscreen ? monitor.getVideoMode().height() : resolution.getY(),
                    monitor.getVideoMode().refreshRate());

        isFullscreen = fullscreen;

        return this;
    }

    public Window setIcon(String[] paths){
        if (isCreated)
            glfwSetWindowIcon(window, WindowIconLoader.load(paths));
        else
            iconPaths = paths;

        return this;
    }

    public String getTitle() {
        return title;
    }

    public Window setTitle(String title) {
        if (isCreated) glfwSetWindowTitle(window, title);
        this.title = title;

        return this;
    }

    public Window show() {
        if (!isCreated) return this;

        glfwShowWindow(window);
        isShow = true;

        return this;
    }

    public Window hide() {
        if (!isCreated) return this;

        glfwHideWindow(window);
        isShow = false;

        return this;
    }

    public Window iconify() {
        if (!isCreated) return this;

        glfwIconifyWindow(window);

        return this;
    }

    public void destroy() {
        if (!isCreated) return;

        glfwDestroyWindow(window);
    }

    public Window restore() {
        if (!isCreated) return this;

        glfwRestoreWindow(window);

        return this;
    }

    public void setWindowCloseCallback(WindowCloseCallback closeCallback) {
        glfwSetWindowCloseCallback(window, (window) -> closeCallback.windowCloseAccept(this));
    }

    protected void updateResolutionLimits() {
        if (isCreated)
            glfwSetWindowSizeLimits(window, resolutionLimits.getX(), resolutionLimits.getY(), resolutionLimits.getZ(), resolutionLimits.getW());
    }

}
