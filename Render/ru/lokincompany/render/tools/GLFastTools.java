package ru.lokincompany.render.tools;

import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class GLFastTools {
    private static float[] defaultTexCoords = new float[]{
            0, 1,
            1, 1,
            1, 0,
            0, 0,
    };

    public static void drawSquare(Vector2f position, Vector2f size, float[] texCoords) {
        glBegin(GL_POLYGON);

        glTexCoord2f(texCoords[0], texCoords[1]);
        glVertex3f(position.x, position.y, 0);

        glTexCoord2f(texCoords[2], texCoords[3]);
        glVertex3f(size.x + position.x, position.y, 0);

        glTexCoord2f(texCoords[4], texCoords[5]);
        glVertex3f(size.x + position.x, size.y + position.y, 0);

        glTexCoord2f(texCoords[6], texCoords[7]);
        glVertex3f(position.x,size.y + position.y, 0);

        glEnd();
    }

    public static void drawSquare(Vector2f position, Vector2f size) {
        drawSquare(position, size, defaultTexCoords);
    }

    public static void drawHollowSquare(Vector2f position, Vector2f size) {
        glBegin(GL_LINE_STRIP);

        glVertex3f(position.x, position.y, 0);
        glVertex3f(size.x + position.x, position.y, 0);
        glVertex3f(size.x + position.x, size.y + position.y, 0);
        glVertex3f(position.x, size.y + position.y, 0);
        glVertex3f(position.x, position.y, 0);

        glEnd();
    }
}
