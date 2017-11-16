package pubtator.counting;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.TIntSet;
import org.apache.commons.lang3.tuple.Triple;
import pubtator.EntityType;
import pubtator.PubTatorRecord;
import pubtator.Visitor;

import java.util.Set;

public class FoundTermsVisitor implements Visitor<PubTatorRecord> {
    private final TObjectIntMap<Triple<String, String, EntityType>> idMap;
    private final TIntIntMap entityCounts;
    private final Set<EntityType> acceptableEntityTypes;
    private final boolean lowerCase;

    FoundTermsVisitor(TIntIntMap entityCounts, TObjectIntMap<Triple<String, String, EntityType>> idMap,
                      Set<EntityType> acceptableEntityTypes, boolean lowerCase) {
        this.entityCounts = entityCounts;
        this.idMap = idMap;
        this.acceptableEntityTypes = acceptableEntityTypes;
        this.lowerCase = lowerCase;
    }

    @Override
    public void visit(PubTatorRecord item) {
        // get all found entities
        final TIntSet foundAcceptableEntities = CountingUtils.getAcceptableEntities(
                item, acceptableEntityTypes, lowerCase, idMap);

        foundAcceptableEntities.forEach(i -> {
            entityCounts.adjustOrPutValue(i, 1, 1);
            return true;
        });
    }
}
