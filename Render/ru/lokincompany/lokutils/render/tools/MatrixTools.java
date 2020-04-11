package ru.lokincompany.lokutils.render.tools;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

public class MatrixTools {
    public static Matrix4f createPerspectiveMatrix(float fov, float aspect, float near, float far) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();

        float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspect);
        float x_scale = y_scale / aspect;
        float frustum_length = far - near;

        matrix.m00 = x_scale;
        matrix.m11 = y_scale;
        matrix.m22 = -((far + near) / frustum_length);
        matrix.m23 = -1;
        matrix.m32 = -((2 * near * far) / frustum_length);
        matrix.m33 = 0;

        return matrix;
    }

    public static Matrix4f createOrthographicMatrix(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();

        float x_orth = 2 / (right - left);
        float y_orth = 2 / (top - bottom);
        float z_orth = -2 / (far - near);

        float tx = -(right + left) / (right - left);
        float ty = -(top + bottom) / (top - bottom);
        float tz = -(far + near) / (far - near);

        matrix.m00 = x_orth;
        matrix.m10 = 0;
        matrix.m20 = 0;
        matrix.m30 = 0;
        matrix.m01 = 0;
        matrix.m11 = y_orth;
        matrix.m21 = 0;
        matrix.m31 = 0;
        matrix.m02 = 0;
        matrix.m12 = 0;
        matrix.m22 = z_orth;
        matrix.m32 = 0;
        matrix.m03 = tx;
        matrix.m13 = ty;
        matrix.m23 = tz;
        matrix.m33 = 1;

        return matrix;
    }

    public static FloatBuffer insertInBuffer(Matrix4f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.store(buffer);
        buffer.flip();

        return buffer;
    }

    public static void bindMatrix(Matrix4f matrix4f, int GLMatrixMode) {
        glMatrixMode(GLMatrixMode);
        glLoadIdentity();

        glMultMatrixf(insertInBuffer(matrix4f));
    }

    public static Matrix4f getBindedMatrix(int GLMatrixType) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        glGetFloatv(GLMatrixType, buffer);

        return (Matrix4f) new Matrix4f().load(buffer);
    }

    public static void bindPerspectiveMatrix(float fov, float aspect, float near, float far) {
        bindMatrix(MatrixTools.createPerspectiveMatrix(fov, aspect, near, far), GL_PROJECTION);
        glMatrixMode(GL_MODELVIEW);
    }

    public static void bindOrthographicMatrix(float left, float right, float bottom, float top, float near, float far) {
        bindMatrix(MatrixTools.createOrthographicMatrix(left, right, bottom, top, near, far), GL_PROJECTION);
        glMatrixMode(GL_MODELVIEW);
    }

    public static void bindOrthographicMatrix(float width, float height) {
        bindOrthographicMatrix(-width / 2, width / 2, -height / 2, height / 2, -1.0f, 1000.0f);
    }
}
