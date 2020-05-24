package ru.lokincompany.lokutils.ui.positioning;

import ru.lokincompany.lokutils.ui.UIObject;

public interface PositioningLink<T> extends PositioningAlgorithm<T> {

    T calculate();

    @Override
    default T calculate(T oldValue, UIObject object){
        return calculate();
    }
}
