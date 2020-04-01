package ru.lokincompany;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import ru.lokincompany.objects.Vector2i;
import ru.lokincompany.render.*;
import ru.lokincompany.render.tools.MatrixTools;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

public class Main {

    public static boolean isKeyDown(int glfwKey, Window window) {
        return glfwGetKey(window.getGLFWWindow(), glfwKey) == 1;
    }

    public static void main(String[] args) {
        GLFW.init();

        Window window = new Window().setResolutionLimits(new Vector2i(500,500)).createAndShow();
        Camera camera = new Camera();

        int i = 0;
        while (window.isCreated()) {
            window.getGlContext().bind();

            Vector3f move = new Vector3f();

            move.x += isKeyDown(org.lwjgl.glfw.GLFW.GLFW_KEY_D, window) ? 0.2f : 0;
            move.x -= isKeyDown(org.lwjgl.glfw.GLFW.GLFW_KEY_A, window) ? 0.2f : 0;

            move.z += isKeyDown(org.lwjgl.glfw.GLFW.GLFW_KEY_S, window) ? 0.2f : 0;
            move.z -= isKeyDown(org.lwjgl.glfw.GLFW.GLFW_KEY_W, window) ? 0.2f : 0;

            move.y += isKeyDown(org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE, window) ? 0.2f : 0;
            move.y -= isKeyDown(org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT, window) ? 0.2f : 0;

            camera.movePosition(move.x, move.y, move.z);

            Vector2i resolution = window.getResolution();
            float ratio = resolution.getX() / (float) resolution.getY();

            MatrixTools.bindOrthographicMatrix(1,1);
            MatrixTools.bindPerspectiveMatrix(90, ratio, 0.1f, 100);

            glLoadIdentity();
            glTranslatef(-camera.getPosition().getX(),-camera.getPosition().getY(),-camera.getPosition().getZ());

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            glBegin(GL_QUADS);

            float x = (float) (Math.sin(i / 100f) + 1) / 2;

            glColor3f(x, 1, 0);

            glVertex3d(0.9f * x, 0.9f, -1);
            glVertex3d(-0.9f, 0.9f, -1);
            glVertex3d(-0.9f, -0.9f, -1);
            glVertex3d(0.9f, -0.9f, -1);

            glEnd();

            window.update();
            i++;

            window.getGlContext().unbind();
        }
    }
}
