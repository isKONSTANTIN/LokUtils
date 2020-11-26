package ru.konstanteam.lokutils.tools.property;

import ru.konstanteam.lokutils.tools.Removable;

import java.util.Vector;

public class Property<T> implements SoftValue<T> {
    protected T value;
    protected SoftValue<T> parent;

    protected Vector<PropertyChangeListener<T>> changeListeners = new Vector<>();

    public Property(Property<T> startPropertyParent) {
        set(startPropertyParent);
    }

    public Property(SoftValue<T> startParent) {
        set(startParent);
    }

    public Property(T startValue) {
        set(startValue);
    }

    public Removable addListener(PropertyChangeListener<T> changeListener) {
        changeListeners.add(changeListener);

        return () -> changeListeners.remove(changeListener);
    }

    public void set(SoftValue<T> parent) {
        this.parent = parent;
    }

    public void set(T value) {
        this.parent = null;

        setWithoutParentReset(value);
    }

    protected void setWithoutParentReset(T value) {
        T oldValue = this.value;
        this.value = value;

        for (PropertyChangeListener<T> changeListener : changeListeners)
            changeListener.changed(oldValue, this.value);
    }

    public void checkParentChanges() {
        if (parent == null)
            return;

        T parentValue = parent.get();

        if (!parentValue.equals(value))
            setWithoutParentReset(parentValue);
    }

    @Override
    public T get() {
        checkParentChanges();

        return value;
    }
}
