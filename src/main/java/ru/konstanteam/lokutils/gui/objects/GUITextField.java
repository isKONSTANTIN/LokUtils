package ru.konstanteam.lokutils.gui.objects;

import ru.konstanteam.lokutils.gui.GUIObject;
import ru.konstanteam.lokutils.gui.eventsystem.events.CharTypedEvent;
import ru.konstanteam.lokutils.gui.eventsystem.events.KeyTypedEvent;
import ru.konstanteam.lokutils.input.KeyAction;
import ru.konstanteam.lokutils.objects.Color;
import ru.konstanteam.lokutils.objects.Point;
import ru.konstanteam.lokutils.objects.Rect;
import ru.konstanteam.lokutils.objects.Size;
import ru.konstanteam.lokutils.render.Font;
import ru.konstanteam.lokutils.render.context.GLContext;
import ru.konstanteam.lokutils.render.tools.GLFastTools;
import ru.konstanteam.lokutils.tools.Action;

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
        size().set(new Size(100, 20));

        setText(new GUIText());

        customersContainer.setCustomer(CharTypedEvent.class, (event) -> {
            if (event.key.action != KeyAction.RELEASE || !getOwner().isFocused(this))
                return;

            addText(String.valueOf(event.key.aChar));
        });

        customersContainer.setCustomer(KeyTypedEvent.class, (event) -> {
            if (event.key.action == KeyAction.RELEASE || !getOwner().isFocused(this))
                return;

            String stext = text.getText();

            if (event.key.buttonID == GLFW_KEY_BACKSPACE && stext.length() > 0 && pointer > 0) {
                String originalText = text.getText();
                text.setText(originalText.substring(0, pointer - 1).concat(originalText.substring(pointer)));
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
        String originalText = text.getText();

        text.setText(originalText.substring(0, pointer).concat(newText).concat(originalText.substring(pointer)));
        movePointer(newText.length());

        return this;
    }

    public GUITextField setPointerPos(int position) {
        String text = this.text.getText();
        Font font = this.text.getFont();

        pointer = Math.max(Math.min(position, text.length()), 0);
        if (font != null)
            pointerPos = font.getSize(text.substring(0, pointer), null).width;

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
        return text.getText();
    }

    public GUITextField setText(GUIText text) {
        if (this.text != null)
            text.setText(this.text.getText());

        this.text = text;

        movePointer(0);
        return this;
    }

    public GUITextField setText(String text) {
        this.text.setText(text);

        movePointer(0);
        return this;
    }

    @Override
    public void render() {
        Color color = getStyle().getColor("textFieldBackground");
        Color cursorColor = text.getColor();

        Size size = size().get();
        Size textSize = text.size().get();

        glColor4f(color.red, color.green, color.blue, color.alpha);
        GLFastTools.drawRoundedSquare(new Rect(size), (float) getStyle().getObject("textFieldRounded"));

        if (translate + size.width < pointerPos)
            translate = pointerPos - size.width;
        else if (translate > pointerPos)
            translate = pointerPos - size.width;

        translate = Math.max(0, translate);

        GLContext.getCurrent().getViewTools().pushScissor(new Rect(size), 0);
        GLContext.getCurrent().getViewTools().pushTranslate(new Point(-translate, size.height / 2f - textSize.height / 2f));

        text.render();

        if (getOwner().isFocused(this)) {
            glColor4f(cursorColor.red, cursorColor.green, cursorColor.blue, cursorColor.alpha);

            glDisable(GL_MULTISAMPLE);
            glBegin(GL_LINES);

            float pointerPos = this.pointerPos;

            if (pointerPos - translate <= 0)
                pointerPos += Math.abs(pointerPos - translate) + 1;

            glVertex2d(Math.max(pointerPos, 1), 0);
            glVertex2d(Math.max(pointerPos, 1), textSize.height);

            glEnd();
            glEnable(GL_MULTISAMPLE);
        }

        GLContext.getCurrent().getViewTools().popScissor();
        GLContext.getCurrent().getViewTools().popTranslate();
    }
}
