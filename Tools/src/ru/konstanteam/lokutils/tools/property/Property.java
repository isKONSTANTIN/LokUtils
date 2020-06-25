package ru.konstanteam.lokutils.tools.property;

import ru.konstanteam.lokutils.tools.Removable;

import java.util.Vector;

public class Property<T> implements SoftValue<T> {
    protected T value;
    protected SoftValue<T> parent;
    protected Vector<PropertyChangeListener<T>> changeListeners = new Vector<>();

    public Property() {

    }

    public Property(SoftValue<T> startParent) {
        this.parent = startParent;
    }

    public Property(T startValue) {
        this.value = startValue;
    }

    public Removable addListener(PropertyChangeListener<T> changeListener) {
        changeListeners.add(changeListener);

        return () -> changeListeners.remove(changeListener);
    }

    public void set(SoftValue<T> parent) {
        this.parent = parent;
    }

    public void set(T value) {
        T oldValue = this.value;
        this.value = value;

        for (PropertyChangeListener<T> changeListener : changeListeners)
            changeListener.changed(oldValue, this.value);
    }

    public void checkParentChanges() {
        T parentValue = parent.get();

        if (!parentValue.equals(value))
            set(parentValue);
    }

    @Override
    public T get() {
        if (parent != null)
            checkParentChanges();

        return value;
    }

    public T lazyGet() {
        return value;
    }
}
