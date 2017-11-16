package pubtator.sentence;

import pubtator.DependencyPath;
import pubtator.filter.DependencyPathEntityTypeRestrictVisitor;
import pubtator.DependencyPathFileTraverser;
import pubtator.EntityType;
import pubtator.Visitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class FindSentencesMatchingPath {
    public static void main(String[] args) throws Exception {
        File pubtatorDependenciesFolder = new File(args[0]);
        String pathListFile = args[1];
        String outputFile = args[2];
        EntityType entityTypeStart = EntityType.fromString(args[3]);
        EntityType entityTypeEnd = EntityType.fromString(args[4]);

        // get list of paths to find
        BufferedReader pathListReader = new BufferedReader(new FileReader(pathListFile));
        String line;
        Map<String, Set<String>> pathStrings = new HashMap<>();
        while ((line = pathListReader.readLine()) != null) {
            pathStrings.put(line.trim(), new HashSet<>());
        }

        // collect sentences corresponding to paths
        int fileCount = 0;
        DependencyPathToSentenceVisitor visitor = new DependencyPathToSentenceVisitor(pathStrings);
        Visitor<DependencyPath> entityRestrictVisitor = new DependencyPathEntityTypeRestrictVisitor(
                entityTypeStart, entityTypeEnd, visitor);
        for (File file : pubtatorDependenciesFolder.listFiles((dir, name) -> name.contains("pubtator"))) {
            DependencyPathFileTraverser traverser = new DependencyPathFileTraverser(new FileInputStream(file));
            traverser.traverseRows(entityRestrictVisitor);
            fileCount++;
            System.out.println("Files done: " + fileCount);
        }

        // print sentences to file
        PrintWriter printWriter = new PrintWriter(new FileWriter(outputFile));
        pathStrings.forEach((pathString, sentenceStrings) -> sentenceStrings.forEach(sentenceString -> {
            printWriter.println(pathString + "\t" + sentenceString);
        }));
        printWriter.flush();
        printWriter.close();
    }
}
