package su.knst.lokutils.tools.property;

public interface InvalidationListener<T> {
    void invalidated(Value<T> value);
}
