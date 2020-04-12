package ru.lokincompany.lokutils.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.objects.Vector2i;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Monitor {

    protected long monitor;

    public Monitor(long monitor) {
        this.monitor = monitor;
    }

    public static Monitor[] getAll() {
        PointerBuffer monitorsBuffer = glfwGetMonitors();

        int countMonitors = monitorsBuffer == null ? 0 : monitorsBuffer.sizeof() / 4;
        if (countMonitors == 0)
            return new Monitor[0];

        Monitor[] monitors = new Monitor[countMonitors];

        for (int i = 0; i < countMonitors; i++)
            monitors[i] = new Monitor(monitorsBuffer.get(i));

        return monitors;
    }

    public static Monitor getPrimary() {
        return new Monitor(glfwGetPrimaryMonitor());
    }

    public long getMonitor() {
        return monitor;
    }

    public GLFWVidMode getVideoMode() {
        return glfwGetVideoMode(monitor);
    }

    public Vector2i getPosition() {
        IntBuffer xBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer yBuffer = BufferUtils.createIntBuffer(1);

        glfwGetMonitorPos(monitor, xBuffer, yBuffer);

        return new Vector2i(xBuffer.get(), yBuffer.get());
    }

    public Vector2f getContentScale() {
        FloatBuffer xBuffer = BufferUtils.createFloatBuffer(1);
        FloatBuffer yBuffer = BufferUtils.createFloatBuffer(1);

        glfwGetMonitorContentScale(monitor, xBuffer, yBuffer);

        return new Vector2f(xBuffer.get(), yBuffer.get());
    }

}
