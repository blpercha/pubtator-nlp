package pubtator;

public interface Visitor<T> {

    void visit(T item);

}