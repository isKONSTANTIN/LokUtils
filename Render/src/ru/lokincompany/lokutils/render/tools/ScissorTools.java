package ru.lokincompany.lokutils.render.tools;

import org.lwjgl.opengl.GL11;
import ru.lokincompany.lokutils.objects.Point;
import ru.lokincompany.lokutils.objects.Rect;
import ru.lokincompany.lokutils.objects.Size;

import java.util.ArrayList;

public class ScissorTools {
    private static ArrayList<Rect> stackScissor = new ArrayList<>();
    private static ArrayList<Point> stackTranslate = new ArrayList<>();

    public static Rect getCurrentScissor() {
        int id = stackScissor.size() - 1;
        if (id < 0) return null;

        return stackScissor.get(id);
    }

    public static Point getCurrentTranslate(){
        int id = stackTranslate.size() - 1;
        if (id < 0) return null;

        return stackTranslate.get(id);
    }

    public static void pushScissor(Rect scissor){
        if (stackScissor.size() == 0) GL11.glEnable(GL11.GL_SCISSOR_TEST);

        Point translate = getCurrentTranslate();
        Rect globalScissor = translate != null ? scissor.offset(translate) : scissor;

        GL11.glScissor((int)globalScissor.getX(), (int)globalScissor.getY(), (int)globalScissor.getWidth(), (int)globalScissor.getHeight());

        stackScissor.add(scissor);
    }

    public static void pushScissor(Point position, Size size){
        pushScissor(new Rect(position, size));
    }

    public static void popScissor() {
        Rect currentScissor = getCurrentScissor();
        if (currentScissor == null) return;

        stackScissor.remove(stackScissor.size() - 1);

        if (stackScissor.size() == 0) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            return;
        }

        Rect scissor = getCurrentScissor();
        GL11.glScissor((int)scissor.getX(), (int)scissor.getY(), (int)scissor.getWidth(), (int)scissor.getHeight());
    }

    public static void pushTranslate(Point translate){
        GL11.glTranslatef(translate.x, translate.y, 0);
        stackTranslate.add(translate);
    }

    public static void popTranslate() {
        Point currentTranslate = getCurrentTranslate();
        if (currentTranslate == null) return;

        stackTranslate.remove(currentTranslate);

        GL11.glTranslatef(-currentTranslate.x, -currentTranslate.y, 0);
    }

}
