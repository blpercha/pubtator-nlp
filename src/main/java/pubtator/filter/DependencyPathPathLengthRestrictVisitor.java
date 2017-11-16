package pubtator.filter;

import pubtator.DependencyPath;
import pubtator.Visitor;

public class DependencyPathPathLengthRestrictVisitor implements Visitor<DependencyPath> {
    private final Visitor<DependencyPath> visitor;
    private final int minLength;

    public DependencyPathPathLengthRestrictVisitor(Visitor<DependencyPath> visitor, int minLength) {
        this.visitor = visitor;
        this.minLength = minLength;
    }

    @Override
    public void visit(DependencyPath item) {
        if (item.getPath().size() < minLength) {
            return;
        }
        visitor.visit(item);
    }
}
