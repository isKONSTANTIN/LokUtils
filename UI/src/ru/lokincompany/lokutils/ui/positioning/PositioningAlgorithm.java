package ru.lokincompany.lokutils.ui.positioning;

import ru.lokincompany.lokutils.ui.UIObject;

public interface PositioningAlgorithm<T> {
    T calculate(T oldValue, UIObject object);
}
