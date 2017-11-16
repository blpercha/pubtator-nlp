package pubtator.matrix;

import pubtator.DependencyPath;
import pubtator.EntityType;
import pubtator.Visitor;

public class DependencyPathEntityTypeRestrictVisitor implements Visitor<DependencyPath> {
    private final EntityType entityType1;
    private final EntityType entityType2;
    private final Visitor<DependencyPath> visitor;

    DependencyPathEntityTypeRestrictVisitor(EntityType entityType1, EntityType entityType2,
                                            Visitor<DependencyPath> visitor) {
        this.entityType1 = entityType1;
        this.entityType2 = entityType2;
        this.visitor = visitor;
    }

    @Override
    public void visit(DependencyPath item) {
        if (item.getEntityTypeStartEntity() == null || item.getEntityTypeEndEntity() == null) {
            return;
        }
        if (!item.getEntityTypeStartEntity().equals(entityType1)) {
            return;
        }
        if (!item.getEntityTypeEndEntity().equals(entityType2)) {
            return;
        }
        visitor.visit(item);
    }
}
