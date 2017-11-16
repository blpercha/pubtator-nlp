package pubtator.counting;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.apache.commons.lang3.tuple.Triple;
import pubtator.EntityType;
import pubtator.PubTatorRecord;
import pubtator.Visitor;

import java.util.Set;

public class FoundPairsVisitor implements Visitor<PubTatorRecord> {
    private final TObjectIntMap<Triple<String, String, EntityType>> idMap;
    private final Set<EntityType> acceptableEntityTypes;
    private final boolean lowerCase;
    private final TIntObjectMap<TIntObjectMap<TIntSet>> pairCooccurMap;

    FoundPairsVisitor(TIntObjectMap<TIntObjectMap<TIntSet>> pairCooccurMap,
                      TObjectIntMap<Triple<String, String, EntityType>> idMap,
                      Set<EntityType> acceptableEntityTypes, boolean lowerCase) {
        this.pairCooccurMap = pairCooccurMap;
        this.idMap = idMap;
        this.acceptableEntityTypes = acceptableEntityTypes;
        this.lowerCase = lowerCase;
    }

    @Override
    public void visit(PubTatorRecord item) {
        // get all found entities
        final TIntSet foundAcceptableEntities = CountingUtils.getAcceptableEntities(
                item, acceptableEntityTypes, lowerCase, idMap);

        // count all pairs
        foundAcceptableEntities.forEach(i -> {
            foundAcceptableEntities.forEach(j -> {
                if (i >= j) { // <- only record count in one direction and don't record if two entities same
                    return true;
                }
                pairCooccurMap.putIfAbsent(i, new TIntObjectHashMap<>());
                pairCooccurMap.get(i).putIfAbsent(j, new TIntHashSet());
                pairCooccurMap.get(i).get(j).add(item.getPmid());
                return true;
            });
            return true;
        });
    }
}
