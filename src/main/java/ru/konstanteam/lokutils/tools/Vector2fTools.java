package ru.konstanteam.lokutils.tools;

import org.lwjgl.util.vector.Vector2f;

public class Vector2fTools {
    public static Vector2f max(Vector2f vector1, Vector2f vector2) {
        return new Vector2f(Math.max(vector1.x, vector2.x), Math.max(vector1.y, vector2.y));
    }

    public static Vector2f min(Vector2f vector1, Vector2f vector2) {
        return new Vector2f(Math.min(vector1.x, vector2.x), Math.min(vector1.y, vector2.y));
    }
}
