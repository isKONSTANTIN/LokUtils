package ru.lokincompany.lokutils.render.tools;

import org.lwjgl.util.vector.Vector2f;
import ru.lokincompany.lokutils.objects.Rect;

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

    public static void drawSquare(Rect rect, float[] texCoords) {
        glBegin(GL_POLYGON);

        glTexCoord2f(texCoords[0], texCoords[1]);
        glVertex3f(rect.getX(), rect.getY(), 0);

        glTexCoord2f(texCoords[2], texCoords[3]);
        glVertex3f(rect.getWidth() + rect.getX(), rect.getY(), 0);

        glTexCoord2f(texCoords[4], texCoords[5]);
        glVertex3f(rect.getWidth() + rect.getX(), rect.getHeight() + rect.getY(), 0);

        glTexCoord2f(texCoords[6], texCoords[7]);
        glVertex3f(rect.getX(), rect.getHeight() + rect.getY(), 0);

        glEnd();
    }

    public static void drawSquare(Rect rect) {
        drawSquare(rect, defaultTexCoords);
    }

    public static void drawInvertedSquare(Rect rect) {
        drawSquare(rect, invertedTexCoords);
    }

    public static void drawHollowSquare(Rect rect) {
        glBegin(GL_LINE_STRIP);

        glVertex3f(rect.getX(), rect.getY(), 0);
        glVertex3f(rect.getWidth() + rect.getX(), rect.getY(), 0);
        glVertex3f(rect.getWidth() + rect.getX(), rect.getWidth() + rect.getY(), 0);
        glVertex3f(rect.getX(), rect.getWidth() + rect.getY(), 0);
        glVertex3f(rect.getX(), rect.getY(), 0);

        glEnd();
    }

    public static void drawRoundedHollowSquare(Rect rect, float radius) {
        float glRadius = min(rect.getWidth(), rect.getHeight()) / 2 * radius;
        int roundingPieces = (int) max(Math.ceil(glRadius / 2f), 2);

        glBegin(GL_LINE_LOOP);

        drawRoundedCorner(rect.getX(), rect.getY() + glRadius, 3 * PI / 2, glRadius, roundingPieces);
        drawRoundedCorner(rect.getX() + rect.getWidth() - glRadius, rect.getY(), 0.0, glRadius, roundingPieces);
        drawRoundedCorner(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight() - glRadius, PI / 2, glRadius, roundingPieces);
        drawRoundedCorner(rect.getX() + glRadius, rect.getY() + rect.getHeight(), PI, glRadius, roundingPieces);

        glEnd();
    }

    public static void drawRoundedSquare(Rect rect, float radius) {
        float glRadius = min(rect.getWidth(), rect.getHeight()) / 2 * radius;
        int roundingPieces = (int) max(Math.ceil(glRadius / 2f), 2);

        glBegin(GL_POLYGON);

        drawRoundedCorner(rect.getX(), rect.getY() + glRadius, 3 * PI / 2, glRadius, roundingPieces);
        drawRoundedCorner(rect.getX() + rect.getWidth() - glRadius, rect.getY(), 0.0, glRadius, roundingPieces);
        drawRoundedCorner(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight() - glRadius, PI / 2, glRadius, roundingPieces);
        drawRoundedCorner(rect.getX() + glRadius, rect.getY() + rect.getHeight(), PI, glRadius, roundingPieces);

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
