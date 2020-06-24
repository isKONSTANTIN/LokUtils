package ru.konstanteam.lokutils.render.tools;

import org.lwjgl.util.vector.Vector2f;
import ru.konstanteam.lokutils.objects.Circle;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;

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

    public static void drawCircle(Circle circle){
        int pieces = (int)max(circle.radius * PI, 2);
        double twicePi = PI * 2;

        glBegin(GL_TRIANGLE_FAN);
        glVertex2f(circle.position.x, circle.position.y);

        for (int i = 0; i <= pieces; i++) {
            glVertex2f(
                    (float) (circle.position.x + (circle.radius * cos(i * twicePi / pieces))),
                    (float) (circle.position.y + (circle.radius * sin(i * twicePi / pieces)))
            );
        }
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

    public static void drawRoundedSquare(Rect rect, float glRadius, int roundingPieces, boolean[] corners) {
        glBegin(GL_POLYGON);

        if (corners[0]) drawRoundedCorner(rect.getX(), rect.getY() + glRadius, 3 * PI / 2, glRadius, roundingPieces);
        else glVertex2d(rect.getX(), rect.getY());

        if (corners[1]) drawRoundedCorner(rect.getX() + rect.getWidth() - glRadius, rect.getY(), 0.0, glRadius, roundingPieces);
        else glVertex2d(rect.getX() + rect.getWidth(), rect.getY());

        if (corners[2]) drawRoundedCorner(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight() - glRadius, PI / 2, glRadius, roundingPieces);
        else glVertex2d(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());

        if (corners[3]) drawRoundedCorner(rect.getX() + glRadius, rect.getY() + rect.getHeight(), PI, glRadius, roundingPieces);
        else glVertex2d(rect.getX(), rect.getY() + rect.getHeight());

        glEnd();
    }

    public static void drawRoundedSquare(Rect rect, float glRadius, int roundingPieces) {
        drawRoundedSquare(rect, glRadius, roundingPieces, new boolean[]{true, true, true, true});
    }

    public static float getOptimalGlRadius(Rect rect, float radius){
        return min(rect.getWidth(), rect.getHeight()) / 2 * radius;
    }

    public static int getOptimalRoundingPieces(float glRadius){
        return (int) max(Math.ceil(glRadius * PI), 2);
    }

    public static void drawRoundedSquare(Rect rect, float radius) {
        float glRadius = getOptimalGlRadius(rect, radius);
        int roundingPieces = getOptimalRoundingPieces(glRadius);

        drawRoundedSquare(rect, glRadius, roundingPieces);
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
