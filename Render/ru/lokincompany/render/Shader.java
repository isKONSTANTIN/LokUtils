package ru.lokincompany.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.util.vector.*;
import ru.lokincompany.objects.Vector2i;
import ru.lokincompany.objects.Vector3i;
import ru.lokincompany.objects.Vector4i;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Shader {

    protected int program;
    protected String vertPath;
    protected String fragPath;

    protected static Shader bindedShader;

    HashMap<String, Integer> uniformsName = new HashMap<>();

    public Shader(String vertPath, String fragPath) throws IOException {
        int vertShader = loadShaderObject(vertPath, ARBVertexShader.GL_VERTEX_SHADER_ARB);
        int fragShader = loadShaderObject(fragPath, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        int program = ARBShaderObjects.glCreateProgramObjectARB();

        ARBShaderObjects.glAttachObjectARB(program, vertShader);
        ARBShaderObjects.glAttachObjectARB(program, fragShader);

        ARBShaderObjects.glLinkProgramARB(program);
        ARBShaderObjects.glValidateProgramARB(program);

        this.program = program;
        this.vertPath = vertPath;
        this.fragPath = fragPath;
    }

    public void bind(){
        ARBShaderObjects.glUseProgramObjectARB(program);
        bindedShader = this;
    }

    public void unbind(){
        ARBShaderObjects.glUseProgramObjectARB(0);
        bindedShader = null;
    }

    public Shader getBindedShader(){
        return bindedShader;
    }

    public int getProgram() {
        return program;
    }

    public String getVertPath() {
        return vertPath;
    }

    public String getFragPath() {
        return fragPath;
    }

    public boolean equals(Object obj) {
        Shader objs = (Shader) obj;
        return objs.program == program;
    }

    public int getUniformLocationID(String name) {
        if (!uniformsName.containsKey(name)) {
            int id = glGetUniformLocation(program, name);
            uniformsName.put(name, id);
            return id;
        } else {
            return uniformsName.get(name);
        }
    }

    public void setUniformData(String uniformName, int data) {
        glUniform1i(getUniformLocationID(uniformName), data);
    }

    public void setUniformData(String uniformName, boolean data) {
        glUniform1i(getUniformLocationID(uniformName), data ? 1 : 0);
    }

    public void setUniformData(String uniformName, Vector2i data) {
        glUniform2i(getUniformLocationID(uniformName), data.getX(), data.getY());
    }

    public void setUniformData(String uniformName, Vector3i data) {
        glUniform3i(getUniformLocationID(uniformName), data.getX(), data.getY(), data.getZ());
    }

    public void setUniformData(String uniformName, Vector4i data) {
        glUniform4i(getUniformLocationID(uniformName), data.getX(), data.getY(), data.getZ(), data.getW());
    }

    public void setUniformData(String uniformName, float data) {
        glUniform1f(getUniformLocationID(uniformName), data);
    }

    public void setUniformData(String uniformName, Vector2f data) {
        glUniform2f(getUniformLocationID(uniformName), data.x, data.y);
    }

    public void setUniformData(String uniformName, Vector3f data) {
        glUniform3f(getUniformLocationID(uniformName), data.x, data.y, data.z);
    }

    public void setUniformData(String uniformName, Vector4f data) {
        glUniform4f(getUniformLocationID(uniformName), data.x, data.y, data.z, data.w);
    }

    public void setUniformData(String uniformName, Matrix2f data) {
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(8);
        data.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix2fv(getUniformLocationID(uniformName), false, matrixBuffer);
    }

    public void setUniformData(String uniformName, Matrix3f data) {
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(12);
        data.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix3fv(getUniformLocationID(uniformName), false, matrixBuffer);
    }

    public void setUniformData(String uniformName, Matrix4f data) {
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        data.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4fv(getUniformLocationID(uniformName), false, matrixBuffer);
    }

    private String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    private String readFileAsString(String filename) throws IOException {
        StringBuilder source = new StringBuilder();
        InputStream in;

        if (filename.charAt(0) == '#') {
            in = Shader.class.getResourceAsStream(filename.substring(1));
        } else {
            in = new FileInputStream(filename);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        String line;
        while ((line = reader.readLine()) != null)
            source.append(line).append('\n');

        reader.close();
        in.close();

        return source.toString();
    }

    private int loadShaderObject(String filename, int shaderType) throws IOException, RuntimeException {
        int shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

        if (shader == 0)
            return 0;

        ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
        ARBShaderObjects.glCompileShaderARB(shader);

        if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE)
            throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

        return shader;
    }
}
