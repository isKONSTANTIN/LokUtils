package su.knst.lokutils.render.tools;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;
import su.knst.lokutils.gui.core.shaders.BlurShader;
import su.knst.lokutils.gui.core.shaders.GUIShader;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.objects.Vector2i;
import su.knst.lokutils.render.Window;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

public class ViewTools {
    private Vector4f orthoView = new Vector4f();
    private ArrayList<Rect> stackScissor = new ArrayList<>();
    private ArrayList<TranslateState> stackTranslate = new ArrayList<>();
    private Window window;
    private GUIRenderBuffer guiRenderBuffer;

    public ViewTools(Window window) throws Exception{
        this.window = window;

        this.guiRenderBuffer = new GUIRenderBuffer();
    }

    public void update(){
        guiRenderBuffer.update(window.getResolution());
    }

    public GUIRenderBuffer getGuiRenderBuffer() {
        return guiRenderBuffer;
    }

    public Rect getCurrentScissor() {
        Rect abs = getCurrentAbsoluteScissor();
        TranslateState translation = getCurrentTranslate();
        return abs != null ? abs.relativeTo(translation.global) : null;
    }

    public Rect getCurrentAbsoluteScissor() {
        int id = stackScissor.size() - 1;
        if (id < 0) return null;

        return stackScissor.get(id);
    }

    public TranslateState getCurrentTranslate() {
        int id = stackTranslate.size() - 1;
        if (id < 0) return TranslateState.ZERO;

        return stackTranslate.get(id);
    }

    public void pushScissor(Rect scissor) {
        if (stackScissor.size() == 0) GL11.glEnable(GL11.GL_SCISSOR_TEST);
        Size size = scissor.size;

        if (size.width == Float.MAX_VALUE || size.height == Float.MAX_VALUE) {
            stackScissor.add(getCurrentScissor());
            return;
        }

        TranslateState translate = getCurrentTranslate();
        Rect absoluteScissor = scissor.offset(translate.global);

        Rect lastScissor = getCurrentAbsoluteScissor();
        if (lastScissor != null){
            absoluteScissor = lastScissor.intersect(absoluteScissor);
        }

        Rect globalScissor = clientToScreen(absoluteScissor);

        GL11.glScissor(
                (int) globalScissor.position.x,
                (int) globalScissor.position.y,
                (int) globalScissor.size.width,
                (int) globalScissor.size.height
        );

        stackScissor.add(absoluteScissor);
    }

    private Rect clientToScreen(Rect clientRect) {
        Size resolution = window.getResolution();

        float x = clientRect.getX();
        float y = resolution.height - clientRect.getY() - clientRect.getHeight();
        float w = clientRect.getWidth();
        float h = clientRect.getHeight();

        return new Rect(x, y, w, h);
    }


    public void pushScissor(Point position, Size size) {
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

        Rect scissor = getCurrentAbsoluteScissor();
        Rect globalScissor = clientToScreen(scissor);

        GL11.glScissor(
                (int) globalScissor.getX(),
                (int) globalScissor.getY(),
                (int) globalScissor.getWidth(),
                (int) globalScissor.getHeight()
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

