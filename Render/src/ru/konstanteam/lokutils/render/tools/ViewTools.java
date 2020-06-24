package ru.konstanteam.lokutils.render.tools;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.Window;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

public class ViewTools {

    private Vector4f orthoView = new Vector4f();
    private ArrayList<Rect> stackScissor = new ArrayList<>();
    private ArrayList<TranslateState> stackTranslate = new ArrayList<>();
    private Window window;

    public ViewTools(Window window){
        this.window = window;
    }

    public Rect getCurrentScissor() {
        int id = stackScissor.size() - 1;
        if (id < 0) return null;

        return stackScissor.get(id);
    }

    public TranslateState getCurrentTranslate(){
        int id = stackTranslate.size() - 1;
        if (id < 0) return null;

        return stackTranslate.get(id);
    }

    public void pushScissor(Rect scissor){
        if (stackScissor.size() == 0) GL11.glEnable(GL11.GL_SCISSOR_TEST);

        TranslateState translate = getCurrentTranslate();
        Rect globalScissor = translate != null ? scissor.offset(translate.global) : scissor;

        globalScissor = globalScissor.setPosition(new Point(globalScissor.getX(), window.getResolution().getY() - globalScissor.getY() - globalScissor.getHeight()));
        //globalScissor = globalScissor.setSize(globalScissor.getSize().offset(globalScissor.getX(), scissor.getY()));
        Rect currentScissor = getCurrentScissor();
        if (currentScissor != null)
            globalScissor = currentScissor.intersect(globalScissor);

        GL11.glScissor(
                (int) globalScissor.getX(),
                (int) globalScissor.getY(),
                (int) globalScissor.getWidth(),
                (int) globalScissor.getHeight());

        stackScissor.add(globalScissor);
    }

    public void pushScissor(Point position, Size size){
        pushScissor(new Rect(position, size));
    }

    public void popScissor() {
        Rect currentScissor = getCurrentScissor();
        if (currentScissor == null) return;

        stackScissor.remove(stackScissor.size() - 1);

        if (stackScissor.size() == 0) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            return;
        }

        Rect scissor = getCurrentScissor();
        GL11.glScissor(
                (int) scissor.getX(),
                (int) scissor.getY(),
                (int) scissor.getWidth(),
                (int) scissor.getHeight());
    }

    public void pushTranslate(Point translate){
        TranslateState currentTranslate = getCurrentTranslate();

        GL11.glTranslatef(translate.x, translate.y, 0);

        stackTranslate.add(new TranslateState(translate, currentTranslate != null ? currentTranslate.global.offset(translate) : translate));
    }

    public void popTranslate() {
        TranslateState currentTranslate = getCurrentTranslate();
        if (currentTranslate == null) return;

        stackTranslate.remove(currentTranslate);

        GL11.glTranslatef(-currentTranslate.local.x, -currentTranslate.local.y, 0);
    }

    public void pushLook(Rect rect){
        pushTranslate(rect.position);
        pushScissor(Point.ZERO, rect.size);
    }

    public void popLook(){
        popScissor();
        popTranslate();
    }

    public Vector4f getOrthoView() {
        return orthoView;
    }

    public void setOrtho2DView(Vector4f orthoView) {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        gluOrtho2D(orthoView.x, orthoView.y, orthoView.z, orthoView.w);

        glMatrixMode(GL_MODELVIEW);

        this.orthoView.x = orthoView.x;
        this.orthoView.y = orthoView.y;
        this.orthoView.z = orthoView.z;
        this.orthoView.w = orthoView.w;
    }

    public void moveOrtho2DView(float x, float y) {
        setOrtho2DView(new Vector4f(orthoView.x - x, orthoView.y - x, orthoView.z - y, orthoView.w - y));
    }

}

class TranslateState {
    public final Point local;
    public final Point global;

    public TranslateState(Point local, Point global) {
        this.local = local;
        this.global = global;
    }
}
