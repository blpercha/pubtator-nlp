package pubtator;

import java.util.List;

public class ListVisitor<T> implements Visitor<T> {
    private final List<Visitor<T>> visitors;

    public ListVisitor(List<Visitor<T>> visitors) {
        this.visitors = visitors;
    }

    @Override
    public void visit(T s) {
        visitors.forEach(visitor -> visitor.visit(s));
    }
}