package su.knst.lokutils.tools.property;

public interface Value<T> {
    T get();

    void addChangeListener(ChangeListener<T> listener);

    void removeChangeListener(ChangeListener<T> listener);

    void addInvalidationListener(InvalidationListener<T> listener);

    void removeInvalidationListener(InvalidationListener<T> listener);

}
