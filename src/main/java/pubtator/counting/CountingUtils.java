package pubtator.counting;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import pubtator.EntityType;
import pubtator.PubTatorRecord;

import java.util.Locale;
import java.util.Set;

class CountingUtils {
    static TIntSet getAcceptableEntities(PubTatorRecord item, Set<EntityType> acceptableEntityTypes,
                                         boolean lowerCase,
                                         TObjectIntMap<Triple<String, String, EntityType>> idMap) {
        final TIntSet foundAcceptableEntities = new TIntHashSet();
        for (Pair<Integer, Integer> position : item.getPositions()) {
            String string = item.getStringForPosition(position.getLeft(), position.getRight());
            String entityId = item.getEntityIdForPosition(position.getLeft(), position.getRight());
            if (entityId == null || entityId.equals("")) {
                continue;
            }
            EntityType entityType = item.getEntityTypeForPosition(position.getLeft(), position.getRight());
            if (!acceptableEntityTypes.contains(entityType)) {
                continue;
            }
            Triple<String, String, EntityType> entityWithId;
            if (lowerCase) {
                entityWithId = Triple.of(string.toLowerCase(Locale.US), entityId, entityType);
            } else {
                entityWithId = Triple.of(string, entityId, entityType);
            }
            if (!idMap.containsKey(entityWithId)) {
                idMap.put(entityWithId, idMap.size()); // make new id for this entity
            }
            foundAcceptableEntities.add(idMap.get(entityWithId));
        }
        return foundAcceptableEntities;
    }
}
