package pubtator.matrix;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import pubtator.DependencyPath;
import pubtator.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DependencyPathMatrixAssemblyVisitor implements Visitor<DependencyPath> {
    private final Map<Pair<String, String>, TObjectIntMap<String>> matrix;
    private final boolean useEntityId;
    private final boolean requireId;
    private final boolean lowercase;

    DependencyPathMatrixAssemblyVisitor(Map<Pair<String, String>, TObjectIntMap<String>> matrix,
                                        boolean useEntityId, boolean requireId, boolean lowercase) {
        this.matrix = matrix;
        this.useEntityId = useEntityId;
        this.requireId = requireId;
        this.lowercase = lowercase;
    }

    @Override
    public void visit(DependencyPath item) {
        Pair<String, String> entityPair;

        if (requireId && (item.getEntityIdStartEntity() == null || item.getEntityIdEndEntity() == null)) {
            return;
        }
        if (useEntityId) {
            entityPair = Pair.of(item.getEntityIdStartEntity(), item.getEntityIdEndEntity());
        } else {
            if (lowercase) {
                entityPair = Pair.of(item.getStartEntity().word().toLowerCase(Locale.US),
                        item.getEndEntity().word().toLowerCase(Locale.US));
            } else {
                entityPair = Pair.of(item.getStartEntity().word(), item.getEndEntity().word());
            }
            if (entityPair.getLeft() == null || entityPair.getRight() == null) {
                return; // this can happen due to quirks with PubTator
            }
        }
        if (!matrix.containsKey(entityPair)) {
            matrix.put(entityPair, new TObjectIntHashMap<>());
        }
        List<String> formattedPath = new ArrayList<>();
        item.getPath().forEach(typedDependency -> formattedPath.add(typedDependency.getGov() + "|" +
                typedDependency.getReln() + "|" + typedDependency.getDep()));
        String feature = StringUtils.join(formattedPath, " "); // get entire dependency path
        if (lowercase) {
            feature = feature.toLowerCase(Locale.US);
        }
        matrix.get(entityPair).adjustOrPutValue(feature, 1, 1);
    }
}
