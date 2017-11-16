package pubtator.matrix;

import gnu.trove.map.TObjectIntMap;
import org.apache.commons.lang3.tuple.Pair;
import pubtator.DependencyPath;
import pubtator.DependencyPathFileTraverser;
import pubtator.EntityType;
import pubtator.Visitor;
import pubtator.filter.DependencyPathConjRemovalVisitor;
import pubtator.filter.DependencyPathEntityTypeRestrictVisitor;
import pubtator.filter.DependencyPathPathLengthRestrictVisitor;
import pubtator.filter.DependencyPathSameStartEndRemovalVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class MakeDependencyPathMatrices {
    public static void main(String[] args) throws Exception {
        File pubtatorDependenciesFolder = new File(args[0]);
        EntityType entityTypeStart = EntityType.valueOf(args[1]);
        EntityType entityTypeEnd = EntityType.valueOf(args[2]);
        String outputFile = args[3];
        int minEntityPairCount = Integer.parseInt(args[4]);
        int minFeatureCount = Integer.parseInt(args[5]);
        int maxEntityPairCount = Integer.parseInt(args[6]);
        int maxFeatureCount = Integer.parseInt(args[7]);
        int nRowsRandomlySelected = Integer.parseInt(args[8]);

        Map<Pair<String, String>, TObjectIntMap<String>> matrix = new HashMap<>();
        Visitor<DependencyPath> matrixVisitor =
                new DependencyPathMatrixAssemblyVisitor(matrix, false, true, true);
        Visitor<DependencyPath> entityRestrictVisitor =
                new DependencyPathEntityTypeRestrictVisitor(entityTypeStart, entityTypeEnd, matrixVisitor);
        Visitor<DependencyPath> pathLengthRestrictVisitor =
                new DependencyPathPathLengthRestrictVisitor(entityRestrictVisitor, 2);  // todo: path length 2 only?
        Visitor<DependencyPath> conjRemovalVisitor =
                new DependencyPathConjRemovalVisitor(pathLengthRestrictVisitor);
        Visitor<DependencyPath> sameStartEndRemovalVisitor =
                new DependencyPathSameStartEndRemovalVisitor(conjRemovalVisitor, true);

        int fileCount = 0;
        for (File file : pubtatorDependenciesFolder.listFiles((dir, name) -> name.contains("pubtator"))) {
            DependencyPathFileTraverser traverser = new DependencyPathFileTraverser(new FileInputStream(file));
            traverser.traverseRows(sameStartEndRemovalVisitor);
            fileCount++;
            System.out.println("Files done: " + fileCount);
        }

        // reduce matrix to minimum count features
        System.out.println("entity pairs (original): " + matrix.size());
        Set<String> features = new HashSet<>();
        matrix.forEach((ePair, featMap) -> features.addAll(featMap.keySet()));
        System.out.println("features (original): " + features.size());
        matrix = MatrixUtils.reduce(matrix, minEntityPairCount, minFeatureCount, maxEntityPairCount, maxFeatureCount);
        if (nRowsRandomlySelected != -1) {
            matrix = MatrixUtils.rowReduceRandom(matrix, nRowsRandomlySelected); // randomly select rows
        }
        System.out.println("entity pairs (reduced): " + matrix.size());
        Set<String> finalFeatures = new HashSet<>();
        matrix.forEach((ePair, featMap) -> finalFeatures.addAll(featMap.keySet()));
        System.out.println("features (reduced): " + finalFeatures.size());

        PrintWriter printWriter = new PrintWriter(outputFile);
        matrix.forEach((entityPair, featureCountMap) -> featureCountMap.forEachEntry((feature, count) -> {
            printWriter.print(entityPair.getLeft() + "\t");
            printWriter.print(entityPair.getRight() + "\t");
            printWriter.print(feature + "\t" + count + "\t"); // dependency path
            printWriter.println(entityPair.getLeft() + "|" + entityPair.getRight());  // entity pair
            return true;
        }));
        printWriter.flush();
        printWriter.close();
    }
}
