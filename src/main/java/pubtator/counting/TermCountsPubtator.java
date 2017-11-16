package pubtator.counting;

// database ids:
// atopic dermatitis = MESH:D003876
// psoriasis = MESH:D011565
// lyme disease = MESH:D008193

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.TIntSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import pubtator.EntityType;
import pubtator.ListVisitor;
import pubtator.PubTatorFileTraverser;
import pubtator.PubTatorRecord;
import pubtator.Visitor;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

class TermCountsPubtator {
    public static void main(String[] args) throws Exception {
        String pubtatorGzipFile = args[0];
        String outputPairCountsFile = args[1];
        String outputEntityIdMapFile = args[2];

        // create set of acceptable entity types: chemicals, genes, diseases
        Set<EntityType> acceptableEntityTypes = new HashSet<>(Arrays.asList(EntityType.Chemical,
                EntityType.Disease, EntityType.Gene));

        // collect joint and individual counts
        TObjectIntMap<Triple<String, String, EntityType>> entityIdMap = new TObjectIntHashMap<>();
        TIntIntMap entityCountMap = new TIntIntHashMap();
        TIntObjectMap<TIntObjectMap<TIntSet>> pairCountMap = new TIntObjectHashMap<>();

        Visitor<PubTatorRecord> entityCountVisitor = new FoundTermsVisitor(entityCountMap,
                entityIdMap, acceptableEntityTypes, true);
        Visitor<PubTatorRecord> pairCountVisitor = new FoundPairsVisitor(pairCountMap,
                entityIdMap, acceptableEntityTypes, true);

        Visitor<PubTatorRecord> listVisitor = new ListVisitor<>(Arrays.asList(entityCountVisitor,
                pairCountVisitor));

        PubTatorFileTraverser traverser = new PubTatorFileTraverser(new GZIPInputStream(
                new FileInputStream(pubtatorGzipFile)));
        traverser.traverseRows(listVisitor);

        // print (a) entity-to-id map, (b) pair counts (plus individual entity counts)
        PrintWriter printWriter = new PrintWriter(outputPairCountsFile);
        pairCountMap.forEachEntry((i, innerMap) -> {
            innerMap.forEachEntry((j, pmids) -> {
                List<String> pmidStrings = new ArrayList<>();
                pmids.forEach(pmid -> {
                    pmidStrings.add(String.format("%d", pmid));
                    return true;
                });
                printWriter.println(i + "\t" + j + "\t" + pmids.size() + "\t" +
                        entityCountMap.get(i) + "\t" + entityCountMap.get(j) + "\t" +
                        StringUtils.join(pmidStrings, ","));
                return true;
            });
            return true;
        });
        printWriter.flush();
        printWriter.close();

        PrintWriter printWriter2 = new PrintWriter(outputEntityIdMapFile);
        entityIdMap.forEachEntry((entityWithId, numericId) -> {
            printWriter2.println(numericId + "\t" + entityWithId.getLeft() + "\t" + entityWithId.getMiddle() + "\t" +
                    entityWithId.getRight() + "\t" + entityCountMap.get(numericId));
            printWriter2.flush();
            return true;
        });
        printWriter2.flush();
        printWriter2.close();
    }
}
