package pubtator.grouping;

import pubtator.DependencyPath;
import pubtator.DependencyPathFileTraverser;
import pubtator.EntityType;
import pubtator.ObjectPrintVisitor;
import pubtator.Visitor;
import pubtator.filter.DependencyPathEntityTypeRestrictVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

class GroupDependencyPathsByEntities {
    public static void main(String[] args) throws Exception {
        File pubtatorDependenciesFolder = new File(args[0]);
        EntityType entityTypeStart = EntityType.valueOf(args[1]);
        EntityType entityTypeEnd = EntityType.valueOf(args[2]);
        String outputFile = args[3];

        ObjectPrintVisitor<DependencyPath> objectPrintVisitor = new ObjectPrintVisitor<>(new FileOutputStream(outputFile));
        int fileCount = 0;
        for (File file : pubtatorDependenciesFolder.listFiles((dir, name) -> name.contains(".txt"))) {
            Visitor<DependencyPath> entityRestrictVisitor =
                    new DependencyPathEntityTypeRestrictVisitor(entityTypeStart, entityTypeEnd,
                            objectPrintVisitor);

            DependencyPathFileTraverser traverser = new DependencyPathFileTraverser(new FileInputStream(file));
            traverser.traverseRows(entityRestrictVisitor);
            fileCount++;
            System.out.println("Files done: " + fileCount);
        }
    }
}
