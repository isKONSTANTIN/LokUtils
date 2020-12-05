package ru.konstanteam.lokutils.render.tools;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;
import ru.konstanteam.lokutils.gui.core.GUIShader;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.Window;

import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

public class ViewTools {
    private static final int SMOOTHING_DEVIATION = 1;

    private Vector4f orthoView = new Vector4f();
    private ArrayList<Rect> stackScissor = new ArrayList<>();
    private ArrayList<TranslateState> stackTranslate = new ArrayList<>();
    private Window window;
    private GUIRenderBuffer guiRenderBuffer;

    public ViewTools(Window window) throws Exception {
        this.window = window;

        this.guiRenderBuffer = new GUIRenderBuffer(new GUIShader());
    }

    public void update(){
        guiRenderBuffer.getDefaultShader().update(new Size(window.getResolution()));
    }

    public GUIRenderBuffer getGuiRenderBuffer() {
        return guiRenderBuffer;
    }

    public Rect getCurrentScissor() {
        int id = stackScissor.size() - 1;
        if (id < 0) return null;
        TranslateState translate = getCurrentTranslate();

        return translate != null ? stackScissor.get(id).relativeTo(translate.global) : stackScissor.get(id);
    }

    public TranslateState getCurrentTranslate() {
        int id = stackTranslate.size() - 1;
        if (id < 0) return TranslateState.ZERO;

        return stackTranslate.get(id);
    }

    public void pushScissor(Rect scissor, int smoothingDeviation) {
        if (stackScissor.size() == 0) GL11.glEnable(GL11.GL_SCISSOR_TEST);
        Size size = scissor.size;

        if (size.width == Float.MAX_VALUE || size.height == Float.MAX_VALUE) {
            stackScissor.add(getCurrentScissor());
            return;
        }

        TranslateState translate = getCurrentTranslate();
        Rect absoluteScissor = scissor.offset(translate.global);

        Rect lastScissor = getCurrentScissor();
        if (lastScissor != null){
            lastScissor = lastScissor.offset(translate.global);
            absoluteScissor = lastScissor.intersect(absoluteScissor);
        }


        Rect globalScissor = absoluteScissor.setPosition(new Point(absoluteScissor.getX(), window.getResolution().getY() - absoluteScissor.getY() - absoluteScissor.getHeight()));

        globalScissor = globalScissor.setPosition(new Point(
                (int) Math.ceil(globalScissor.getX()) - smoothingDeviation,
                (int) Math.ceil(globalScissor.getY()) - smoothingDeviation
        )).setSize(new Size(
                (int) Math.ceil(globalScissor.getWidth()) + smoothingDeviation * 2,
                (int) Math.ceil(globalScissor.getHeight()) + smoothingDeviation * 2
        ));

        GL11.glScissor(
                (int) globalScissor.position.x,
                (int) globalScissor.position.y,
                (int) globalScissor.size.width,
                (int) globalScissor.size.height
        );

        stackScissor.add(absoluteScissor);
    }

    public void pushScissor(Rect scissor) {
        pushScissor(scissor, SMOOTHING_DEVIATION);
    }

    public void pushScissor(Point position, Size size) {
        pushScissor(new Rect(position, size));
    }

    public void pushScissor(Point position, Size size, int smoothingDeviation) {
        pushScissor(new Rect(position, size), smoothingDeviation);
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
                (int) scissor.getHeight()
        );
    }

    public void pushTranslate(Point translate) {
        TranslateState currentTranslate = getCurrentTranslate();

        GL11.glTranslatef(translate.x, translate.y, 0);

        stackTranslate.add(new TranslateState(translate, currentTranslate.global.offset(translate)));
    }

    public void popTranslate() {
        TranslateState currentTranslate = getCurrentTranslate();
        if (currentTranslate == TranslateState.ZERO) return;

        stackTranslate.remove(currentTranslate);

        GL11.glTranslatef(-currentTranslate.local.x, -currentTranslate.local.y, 0);
    }

    public void pushLook(Rect rect, int smoothingDeviation) {
        pushTranslate(rect.position);
        pushScissor(Point.ZERO, rect.size, smoothingDeviation);
    }

    public void pushLook(Rect rect) {
        pushTranslate(rect.position);
        pushScissor(Point.ZERO, rect.size);
    }

    public void popLook() {
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

