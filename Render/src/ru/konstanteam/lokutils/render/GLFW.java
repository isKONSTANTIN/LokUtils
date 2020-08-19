package ru.konstanteam.lokutils.render;

import static org.lwjgl.glfw.GLFW.*;

public class GLFW {

    private static boolean inited;

    public static synchronized boolean init() {
        if (inited)
            return true;

        inited = glfwInit();

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        return inited;
    }

}
