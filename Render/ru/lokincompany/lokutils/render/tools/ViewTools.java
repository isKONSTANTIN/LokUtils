package ru.lokincompany.lokutils.render.tools;

import org.lwjgl.util.vector.Vector4f;
import ru.lokincompany.lokutils.objects.Vector2i;
import ru.lokincompany.lokutils.objects.Vector4i;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

public class ViewTools {

    private static Vector4f orthoView = new Vector4f();

    public static Vector4f getOrthoView(){
        return orthoView;
    }

    public static void setOrtho2DView(Vector4f orthoView){
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        gluOrtho2D(orthoView.x, orthoView.y, orthoView.z, orthoView.w);

        glMatrixMode(GL_MODELVIEW);

        ViewTools.orthoView.x = orthoView.x;
        ViewTools.orthoView.y = orthoView.y;
        ViewTools.orthoView.z = orthoView.z;
        ViewTools.orthoView.w = orthoView.w;
    }

    public static void moveOrtho2DView(float x, float y){
        setOrtho2DView(new Vector4f(orthoView.x - x, orthoView.y - x, orthoView.z - y, orthoView.w - y));
    }

}
