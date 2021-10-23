package su.knst.lokutils.render.shader;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.util.vector.*;
import su.knst.lokutils.objects.Vector2i;
import su.knst.lokutils.objects.Vector3i;
import su.knst.lokutils.objects.Vector4i;
import su.knst.lokutils.render.GLObject;
import su.knst.lokutils.render.VBO;
import su.knst.lokutils.render.context.GLContext;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;

public class Shader extends GLObject {
    protected String vertPath;
    protected String fragPath;
    protected HashMap<String, Integer> uniformsNames = new HashMap<>();
    protected HashMap<String, Integer> attributesNames = new HashMap<>();
    protected ArrayList<Integer> enabledVertexAttribs = new ArrayList<>();

    protected Shader(String vertPath, String fragPath) throws IOException {
        this.vertPath = vertPath;
        this.fragPath = fragPath;
        generate();
    }

    @Override
    public void generate() throws IOException {
        GLcontext = GLContext.getCurrent();
        if (GLcontext == null) throw new RuntimeException("Shader cannot be created without OpenGL context!");

        int vertShader = loadShaderObject(vertPath, ARBVertexShader.GL_VERTEX_SHADER_ARB);
        int fragShader = loadShaderObject(fragPath, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        int program = ARBShaderObjects.glCreateProgramObjectARB();

        ARBShaderObjects.glAttachObjectARB(program, vertShader);
        ARBShaderObjects.glAttachObjectARB(program, fragShader);

        ARBShaderObjects.glLinkProgramARB(program);
        ARBShaderObjects.glValidateProgramARB(program);

        this.id = program;
    }

    @Override
    public void delete() {
        if (!GLContext.check(GLcontext))
            throw new RuntimeException("Shader cannot be deleted without or another OpenGL context!");

        ARBShaderObjects.glDeleteObjectARB(id);

        id = 0;
        vertPath = null;
        fragPath = null;
    }

    @Override
    public void bind() {
        if (!GLContext.check(GLcontext))
            throw new RuntimeException("Shader cannot be binded without or another OpenGL context!");

        ARBShaderObjects.glUseProgramObjectARB(id);
    }

    @Override
    public void unbind() {
        if (!GLContext.check(GLcontext))
            throw new RuntimeException("Shader cannot be unbinded without or another OpenGL context!");

        for (int aid : enabledVertexAttribs){
            glDisableVertexAttribArray(aid);
        }

        enabledVertexAttribs.clear();

        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    protected int getProgram() {
        return id;
    }

    protected String getVertPath() {
        return vertPath;
    }

    protected String getFragPath() {
        return fragPath;
    }

    public boolean equals(Object obj) {
        Shader objs = (Shader) obj;
        return objs.id == id;
    }

    protected int getUniformLocationID(String name) {
        if (uniformsNames.containsKey(name))
            return uniformsNames.get(name);

        int uid = glGetUniformLocation(id, name);
        uniformsNames.put(name, uid);
        return uid;
    }

    protected int getAttributeLocationID(String name) {
        if (attributesNames.containsKey(name))
            return attributesNames.get(name);

        int uid = glGetAttribLocation(id, name);
        attributesNames.put(name, uid);
        return uid;
    }

    protected void setAttributeData(String attributeName, VBO data, int size){
        int attributeID = getAttributeLocationID(attributeName);

        enabledVertexAttribs.add(attributeID);

        data.bind();
        GL20C.glEnableVertexAttribArray(attributeID);
        GL20C.glVertexAttribPointer(attributeID, size, GL_FLOAT, false, 0, 0);
        data.unbind();
    }

    protected void setUniformData(String uniformName, int data) {
        glUniform1i(getUniformLocationID(uniformName), data);
    }

    protected void setUniformData(String uniformName, boolean data) {
        glUniform1i(getUniformLocationID(uniformName), data ? 1 : 0);
    }

    protected void setUniformData(String uniformName, Vector2i data) {
        glUniform2i(getUniformLocationID(uniformName), data.getX(), data.getY());
    }

    protected void setUniformData(String uniformName, Vector3i data) {
        glUniform3i(getUniformLocationID(uniformName), data.getX(), data.getY(), data.getZ());
    }

    protected void setUniformData(String uniformName, Vector4i data) {
        glUniform4i(getUniformLocationID(uniformName), data.getX(), data.getY(), data.getZ(), data.getW());
    }

    protected void setUniformData(String uniformName, float data) {
        glUniform1f(getUniformLocationID(uniformName), data);
    }

    protected void setUniformData(String uniformName, Vector2f data) {
        glUniform2f(getUniformLocationID(uniformName), data.x, data.y);
    }

    protected void setUniformData(String uniformName, Vector3f data) {
        glUniform3f(getUniformLocationID(uniformName), data.x, data.y, data.z);
    }

    protected void setUniformData(String uniformName, Vector4f data) {
        glUniform4f(getUniformLocationID(uniformName), data.x, data.y, data.z, data.w);
    }

    protected void setUniformData(String uniformName, Matrix2f data) {
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(8);
        data.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix2fv(getUniformLocationID(uniformName), false, matrixBuffer);
    }

    protected void setUniformData(String uniformName, Matrix3f data) {
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(12);
        data.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix3fv(getUniformLocationID(uniformName), false, matrixBuffer);
    }

    protected void setUniformData(String uniformName, Matrix4f data) {
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        data.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4fv(getUniformLocationID(uniformName), false, matrixBuffer);
    }

    protected void setBindedTexturePosition(String uniformName, int position){
        setUniformData(uniformName, position);
    }

    protected String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    protected String readFileAsString(String filename) throws IOException {
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

    protected int loadShaderObject(String filename, int shaderType) throws IOException, RuntimeException {
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
