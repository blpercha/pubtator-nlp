package pubtator.matrix;

import pubtator.Dependency;
import pubtator.DependencyPath;
import pubtator.Visitor;

public class DependencyPathConjRemovalVisitor implements Visitor<DependencyPath> {
    private final Visitor<DependencyPath> visitor;

    DependencyPathConjRemovalVisitor(Visitor<DependencyPath> visitor) {
        this.visitor = visitor;
    }

    @Override
    public void visit(DependencyPath item) {
        for (Dependency dependency : item.getPath()) {
            if (dependency.getReln().equals("conj"))
                return;
        }
        visitor.visit(item);
    }
}
