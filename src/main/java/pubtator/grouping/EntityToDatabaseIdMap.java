package pubtator.grouping;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.apache.commons.lang3.tuple.Triple;
import pubtator.EntityType;
import pubtator.PubTatorFileTraverser;
import pubtator.PubTatorRecord;
import pubtator.Visitor;
import pubtator.counting.FoundTermsVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;

class EntityToDatabaseIdMap {
    public static void main(String[] args) throws Exception {
        File pubtatorBioconceptsFolder = new File(args[0]);
        EntityType entityType = EntityType.fromString(args[1]);
        String outputFile = args[2];

        // set up visitor to collect all terms with ids
        TIntIntMap entityCounts = new TIntIntHashMap();
        TObjectIntMap<Triple<String, String, EntityType>> entityIdMap = new TObjectIntHashMap<>();
        Visitor<PubTatorRecord> visitor = new FoundTermsVisitor(entityCounts, entityIdMap,
                new HashSet<>(Collections.singletonList(entityType)), false);

        int fileCount = 0;
        for (File file : pubtatorBioconceptsFolder.listFiles((dir, name) -> name.contains("pubtator"))) {
            PubTatorFileTraverser traverser = new PubTatorFileTraverser(new FileInputStream(file));
            traverser.traverseRows(visitor);
            fileCount++;
            System.out.println("Files done: " + fileCount);
        }

        // print results to output file
        PrintWriter printWriter = new PrintWriter(outputFile);
        entityIdMap.forEachEntry((triple, i) -> {
            printWriter.println(triple.getLeft() + "\t" + triple.getMiddle() + "\t" +
                    triple.getRight() + "\t" + entityCounts.get(i));
            return true;
        });
        printWriter.flush();
        printWriter.close();
    }
}
