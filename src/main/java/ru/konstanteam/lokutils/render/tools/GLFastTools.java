package ru.konstanteam.lokutils.render.tools;

import ru.konstanteam.lokutils.objects.Circle;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.render.Texture;
import ru.konstanteam.lokutils.render.context.GLContext;

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

    public static void drawTexturedSquare(Rect rect, Texture texture, float[] texCoords) {
        GUIRenderBuffer buffer = GLContext.getCurrent().getViewTools().getGuiRenderBuffer();

        buffer.begin(texture);

        buffer.addRawTexCoord(texCoords[0], texCoords[1]);
        buffer.addVertex(rect.getX(), rect.getY());

        buffer.addRawTexCoord(texCoords[2], texCoords[3]);
        buffer.addVertex(rect.getWidth() + rect.getX(), rect.getY());

        buffer.addRawTexCoord(texCoords[4], texCoords[5]);
        buffer.addVertex(rect.getWidth() + rect.getX(), rect.getHeight() + rect.getY());

        buffer.addRawTexCoord(texCoords[6], texCoords[7]);
        buffer.addVertex(rect.getX(), rect.getHeight() + rect.getY());

        buffer.draw(GL_POLYGON);
    }

    public static void drawTexturedSquare(Rect rect, Texture texture) {
        drawTexturedSquare(rect, texture, defaultTexCoords);
    }

    public static void drawHollowSquare(Rect rect) {
        GUIRenderBuffer buffer = GLContext.getCurrent().getViewTools().getGuiRenderBuffer();

        buffer.begin();

        buffer.addVertex(rect.getX(), rect.getY());
        buffer.addVertex(rect.getWidth() + rect.getX(), rect.getY());
        buffer.addVertex(rect.getWidth() + rect.getX(), rect.getWidth() + rect.getY());
        buffer.addVertex(rect.getX(), rect.getWidth() + rect.getY());
        buffer.addVertex(rect.getX(), rect.getY());

        buffer.draw(GL_LINE_STRIP);
    }

    public static void drawCircle(Circle circle) {
        int pieces = (int) max(circle.radius * PI, 2);
        double twicePi = PI * 2;

        Point position = new Point(circle.position.x + circle.radius, circle.position.y + circle.radius);

        GUIRenderBuffer buffer = GLContext.getCurrent().getViewTools().getGuiRenderBuffer();
        buffer.begin();
        buffer.addVertex(position.x, position.y);

        for (int i = 0; i <= pieces; i++) {
            buffer.addVertex(
                    (float) (position.x + (circle.radius * cos(i * twicePi / pieces))),
                    (float) (position.y + (circle.radius * sin(i * twicePi / pieces)))
            );
        }

        buffer.draw(GL_TRIANGLE_FAN);
    }

    public static void drawRoundedHollowSquare(Rect rect, float radius) {
        float glRadius = min(rect.getWidth(), rect.getHeight()) / 2 * radius;
        int roundingPieces = (int) max(Math.ceil(glRadius / 2f), 2);
        GUIRenderBuffer buffer = GLContext.getCurrent().getViewTools().getGuiRenderBuffer();

        buffer.begin();

        drawRoundedCorner(rect.getX(), rect.getY() + glRadius, 3 * PI / 2, glRadius, roundingPieces, buffer);
        drawRoundedCorner(rect.getX() + rect.getWidth() - glRadius, rect.getY(), 0.0, glRadius, roundingPieces, buffer);
        drawRoundedCorner(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight() - glRadius, PI / 2, glRadius, roundingPieces, buffer);
        drawRoundedCorner(rect.getX() + glRadius, rect.getY() + rect.getHeight(), PI, glRadius, roundingPieces, buffer);

        buffer.draw(GL_LINE_LOOP);
    }

    public static void drawRoundedSquare(Rect rect, float glRadius, int roundingPieces, boolean[] corners) {
        GUIRenderBuffer buffer = GLContext.getCurrent().getViewTools().getGuiRenderBuffer();
        buffer.begin();

        if (corners[0]) drawRoundedCorner(rect.getX(), rect.getY() + glRadius, 3 * PI / 2, glRadius, roundingPieces, buffer);
        else buffer.addVertex(rect.getX(), rect.getY());

        if (corners[1])
            drawRoundedCorner(rect.getX() + rect.getWidth() - glRadius, rect.getY(), 0.0, glRadius, roundingPieces, buffer);
        else buffer.addVertex(rect.getX() + rect.getWidth(), rect.getY());

        if (corners[2])
            drawRoundedCorner(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight() - glRadius, PI / 2, glRadius, roundingPieces, buffer);
        else buffer.addVertex(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());

        if (corners[3])
            drawRoundedCorner(rect.getX() + glRadius, rect.getY() + rect.getHeight(), PI, glRadius, roundingPieces, buffer);
        else buffer.addVertex(rect.getX(), rect.getY() + rect.getHeight());

        buffer.draw(GL_POLYGON);
    }

    public static void drawRoundedSquare(Rect rect, float glRadius, int roundingPieces) {
        drawRoundedSquare(rect, glRadius, roundingPieces, new boolean[]{true, true, true, true});
    }

    public static float getOptimalGlRadius(Rect rect, float radius) {
        return min(rect.getWidth(), rect.getHeight()) / 2 * radius;
    }

    public static int getOptimalRoundingPieces(float glRadius) {
        return (int) max(Math.ceil(glRadius * PI), 2);
    }

    public static void drawRoundedSquare(Rect rect, float radius) {
        float glRadius = getOptimalGlRadius(rect, radius);
        int roundingPieces = getOptimalRoundingPieces(glRadius);

        drawRoundedSquare(rect, glRadius, roundingPieces);
    }

    public static void drawRoundedCorner(float x, float y, double sa, float r, int roundingPieces, GUIRenderBuffer buffer) {
        double cent_x = x + r * cos(sa + PI / 2);
        double cent_y = y + r * sin(sa + PI / 2);

        int n = (int) ceil(roundingPieces * 1.5707963267948966 / PI * 2);
        for (int i = 0; i <= n; i++) {
            double ang = sa + 1.5707963267948966 * (double) i / (double) n;

            float next_x = (float)(cent_x + r * sin(ang));
            float next_y = (float)(cent_y - r * cos(ang));

            buffer.addVertex(next_x, next_y);
        }
    }

}
