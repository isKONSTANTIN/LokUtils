package ru.lokincompany.lokutils.render.tools;

import org.lwjgl.util.vector.Vector2f;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;

public class GLFastTools {
    private static float[] defaultTexCoords = new float[]{
            0, 0,
            1, 0,
            1, 1,
            0, 1,
    };

    private static float[] invertedTexCoords = new float[]{
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
        glVertex3f(position.x, size.y + position.y, 0);

        glEnd();
    }

    public static void drawSquare(Vector2f position, Vector2f size) {
        drawSquare(position, size, defaultTexCoords);
    }

    public static void drawInvertedSquare(Vector2f position, Vector2f size) {
        drawSquare(position, size, invertedTexCoords);
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

    public static void drawRoundedSquare(Vector2f position, Vector2f size, float radius) {
        float glRadius = min(size.x, size.y) / 2 * radius;
        int roundingPieces = (int) max(Math.ceil(glRadius / 2f), 2);

        glBegin(GL_POLYGON);

        drawRoundedCorner(position.x, position.y + glRadius, 3 * PI / 2, glRadius, roundingPieces);
        drawRoundedCorner(position.x + size.x - glRadius, position.y, 0.0, glRadius, roundingPieces);
        drawRoundedCorner(position.x + size.x, position.y + size.y - glRadius, PI / 2, glRadius, roundingPieces);
        drawRoundedCorner(position.x + glRadius, position.y + size.y, PI, glRadius, roundingPieces);

        glEnd();
    }

    private static void drawRoundedCorner(float x, float y, double sa, float r, int roundingPieces) {
        double cent_x = x + r * cos(sa + PI / 2);
        double cent_y = y + r * sin(sa + PI / 2);

        int n = (int) ceil(roundingPieces * 1.5707963267948966 / PI * 2);
        for (int i = 0; i <= n; i++) {
            double ang = sa + 1.5707963267948966 * (double) i / (double) n;

            double next_x = cent_x + r * sin(ang);
            double next_y = cent_y - r * cos(ang);

            glVertex2d(next_x, next_y);
        }
    }

}
