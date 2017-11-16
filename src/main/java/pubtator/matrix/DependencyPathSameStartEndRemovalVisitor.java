package pubtator.matrix;

import pubtator.DependencyPath;
import pubtator.Visitor;

import java.util.Locale;

public class DependencyPathSameStartEndRemovalVisitor implements Visitor<DependencyPath> {
    private final Visitor<DependencyPath> visitor;
    private final boolean lowercase;

    DependencyPathSameStartEndRemovalVisitor(Visitor<DependencyPath> visitor,
                                             boolean lowercase) {
        this.visitor = visitor;
        this.lowercase = lowercase;
    }

    @Override
    public void visit(DependencyPath item) {
        if (lowercase) {
            if (item.getStartEntity().word().toLowerCase(Locale.US).equals(
                    item.getEndEntity().word().toLowerCase(Locale.US))) {
                return;
            }
        } else {
            if (item.getStartEntity().word().equals(item.getEndEntity().word())) {
                return;
            }
        }
        // can't map to same entity
        if (item.getEntityIdStartEntity() != null && item.getEntityIdEndEntity() != null &&
                item.getEntityIdStartEntity().equals(item.getEntityIdEndEntity())) {
            return;
        }
        visitor.visit(item);
    }
}
