package ru.konstanteam.lokutils.render.tools;

import ru.konstanteam.lokutils.objects.Point;

public class TranslateState {
    public final Point local;
    public final Point global;

    public TranslateState(Point local, Point global) {
        this.local = local;
        this.global = global;
    }
}
