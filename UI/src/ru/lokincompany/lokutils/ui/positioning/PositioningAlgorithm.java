package ru.lokincompany.lokutils.ui.positioning;

import ru.lokincompany.lokutils.ui.UIObject;

public interface PositioningAlgorithm<T> {
    T calculate(UIObject object);
}
