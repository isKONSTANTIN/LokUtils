package su.knst.lokutils.gui.objects;

import su.knst.lokutils.gui.GUIObject;
import su.knst.lokutils.gui.eventsystem.events.CharTypedEvent;
import su.knst.lokutils.gui.eventsystem.events.KeyTypedEvent;
import su.knst.lokutils.gui.layout.GUIAbstractLayout;
import su.knst.lokutils.input.KeyAction;
import su.knst.lokutils.objects.Point;
import su.knst.lokutils.objects.Rect;
import su.knst.lokutils.objects.Size;
import su.knst.lokutils.objects.Squircle;
import su.knst.lokutils.render.context.GLContext;
import su.knst.lokutils.render.text.AbstractFont;
import su.knst.lokutils.render.text.TextRenderHelper;
import su.knst.lokutils.render.tools.GLFastTools;
import su.knst.lokutils.render.tools.GUIRenderBuffer;
import su.knst.lokutils.tools.Action;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;

public class GUITextField extends GUIObject {
    protected GUIText text;
    protected int pointer;
    protected float pointerPos;
    protected float translate;
    protected Action enterAction;

    public GUITextField() {
        size().set(new Size(100, 25));

        setText(new GUIText(""));

        customersContainer.setCustomer(CharTypedEvent.class, (event) -> {
            if (event.key.action != KeyAction.RELEASE || !getOwner().isFocused(this))
                return;

            addText(String.valueOf(event.key.aChar));
        });

        customersContainer.setCustomer(KeyTypedEvent.class, (event) -> {
            if (event.key.action == KeyAction.RELEASE || !getOwner().isFocused(this))
                return;

            String stext = text.string().get();

            if (event.key.buttonID == GLFW_KEY_BACKSPACE && stext.length() > 0 && pointer > 0) {
                text.string().set(stext.substring(0, pointer - 1).concat(stext.substring(pointer)));
                movePointer(-1);
            }

            if (event.key.buttonID == GLFW_KEY_RIGHT)
                movePointer(1);

            if (event.key.buttonID == GLFW_KEY_LEFT)
                movePointer(-1);

            if (event.key.buttonID == GLFW_KEY_V && event.key.mods == GLFW_MOD_CONTROL) {
                try {
                    addText((String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
                } catch (UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                }
            }

            if (event.key.buttonID == GLFW_KEY_ENTER && enterAction != null)
                enterAction.call();

        });
    }

    public Action getEnterAction() {
        return enterAction;
    }

    public GUITextField setEnterAction(Action action) {
        this.enterAction = action;

        return this;
    }

    public GUITextField addText(String newText) {
        String originalText = text.string().get();

        text.string().set(originalText.substring(0, pointer).concat(newText).concat(originalText.substring(pointer)));
        movePointer(newText.length());

        return this;
    }

    public GUITextField setPointerPos(int position) {
        String text = this.text.string().get();
        AbstractFont font = this.text.getFont();

        pointer = Math.max(Math.min(position, text.length()), 0);
        if (font != null)
            pointerPos = TextRenderHelper.getSize(font, text.substring(0, pointer), null).width;

        return this;
    }

    public GUITextField movePointer(int position) {
        setPointerPos(pointer + position);

        return this;
    }

    public int getPointerPosition() {
        return pointer;
    }

    public String getText() {
        return text.string().get();
    }

    public GUITextField setText(GUIText text) {
        if (this.text != null)
            text.string().track(this.text.string());

        this.text = text;

        movePointer(0);
        return this;
    }

    public GUITextField setText(String text) {
        this.text.string().set(text);

        movePointer(0);
        return this;
    }

    @Override
    public void init(GUIAbstractLayout owner) {
        super.init(owner);

        text.init(owner);
        this.text.overrideColor = asset.color("text");
    }

    @Override
    public void render() {
        su.knst.lokutils.objects.Color color = asset.color("background");
        su.knst.lokutils.objects.Color cursorColor = text.getColor();

        Size size = size().get();
        Size textSize = text.size().get();

        glColor4f(color.red, color.green, color.blue, color.alpha);
        GLFastTools.drawSquircle(new Squircle(size, (float) asset.object("rounded")));

        if (translate + size.width < pointerPos)
            translate = pointerPos - size.width;
        else if (translate > pointerPos)
            translate = pointerPos - size.width;

        translate = Math.max(0, translate);

        GLContext.getCurrent().getViewTools().pushScissor(new Rect(size));
        GLContext.getCurrent().getViewTools().pushTranslate(new Point(-translate + 4, size.height / 2f - textSize.height / 2f));

        text.render();

        if (getOwner().isFocused(this)) {
            glColor4f(cursorColor.red, cursorColor.green, cursorColor.blue, cursorColor.alpha);

            GUIRenderBuffer buffer = GLContext.getCurrent().getViewTools().getGuiRenderBuffer();

            glDisable(GL_MULTISAMPLE);
            buffer.begin();

            float pointerPos = this.pointerPos;

            if (pointerPos - translate <= 0)
                pointerPos += Math.abs(pointerPos - translate) + 1;

            buffer.addVertex(Math.max(pointerPos, 1), 0);
            buffer.addVertex(Math.max(pointerPos, 1), textSize.height);


            buffer.draw(GL_LINES);
            glEnable(GL_MULTISAMPLE);
        }

        GLContext.getCurrent().getViewTools().popTranslate();
        GLContext.getCurrent().getViewTools().popScissor();
    }
}
