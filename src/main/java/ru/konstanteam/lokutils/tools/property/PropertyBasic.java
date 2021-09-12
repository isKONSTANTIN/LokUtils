package ru.konstanteam.lokutils.tools.property;

import ru.konstanteam.lokutils.tools.Removable;

import java.util.Vector;
import java.util.function.Supplier;

public class PropertyBasic<T> extends AbstractProperty<T> {
    public PropertyBasic(Value<T> startPropertyBasicParent) {
        track(startPropertyBasicParent);
    }

    public PropertyBasic(Supplier<T> value) {
        track(value);
    }

    public PropertyBasic(Supplier<T> value, Value... sources) {
        track(value, sources);
    }

    public PropertyBasic(T startValue) {
        set(startValue);
    }
}
