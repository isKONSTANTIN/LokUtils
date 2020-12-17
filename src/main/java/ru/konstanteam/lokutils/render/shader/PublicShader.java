package ru.konstanteam.lokutils.render.shader;

import org.lwjgl.util.vector.*;
import ru.konstanteam.lokutils.objects.Vector2i;
import ru.konstanteam.lokutils.objects.Vector3i;
import ru.konstanteam.lokutils.objects.Vector4i;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class PublicShader extends Shader {
    public PublicShader(String vertPath, String fragPath) throws IOException {
        super(vertPath, fragPath);
    }

    public int getProgram() {
        return id;
    }

    public String getVertPath() {
        return vertPath;
    }

    public String getFragPath() {
        return fragPath;
    }

    public int getUniformLocationID(String name) {
        if (uniformsNames.containsKey(name))
            return uniformsNames.get(name);

        int uid = glGetUniformLocation(id, name);
        uniformsNames.put(name, uid);
        return uid;
    }

    public void setUniformData(String uniformName, int data) {
        super.setUniformData(uniformName, data);
    }

    public void setUniformData(String uniformName, boolean data) {
        super.setUniformData(uniformName, data);
    }

    public void setUniformData(String uniformName, Vector2i data) {
        super.setUniformData(uniformName, data);
    }

    public void setUniformData(String uniformName, Vector3i data) {
        super.setUniformData(uniformName, data);
    }

    public void setUniformData(String uniformName, Vector4i data) {
        super.setUniformData(uniformName, data);
    }

    public void setUniformData(String uniformName, float data) {
        super.setUniformData(uniformName, data);
    }

    public void setUniformData(String uniformName, Vector2f data) {
        super.setUniformData(uniformName, data);
    }

    public void setUniformData(String uniformName, Vector3f data) {
        super.setUniformData(uniformName, data);
    }

    public void setUniformData(String uniformName, Vector4f data) {
        super.setUniformData(uniformName, data);
    }

    public void setUniformData(String uniformName, Matrix2f data) {
        super.setUniformData(uniformName, data);
    }

    public void setUniformData(String uniformName, Matrix3f data) {
        super.setUniformData(uniformName, data);
    }

    public void setUniformData(String uniformName, Matrix4f data) {
        super.setUniformData(uniformName, data);
    }
}
