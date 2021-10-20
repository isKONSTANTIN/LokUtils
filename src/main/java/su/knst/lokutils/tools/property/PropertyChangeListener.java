package su.knst.lokutils.tools.property;

public interface PropertyChangeListener<T> {
    void changed(T oldValue, T newValue);
}
