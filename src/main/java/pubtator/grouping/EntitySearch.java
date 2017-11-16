package pubtator.grouping;

import pubtator.DependencyPath;
import pubtator.DependencyPathFileTraverser;
import pubtator.EntityType;
import pubtator.ListVisitor;
import pubtator.Visitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EntitySearch {
    public static void main(String[] args) throws Exception {
        File pubtatorDependenciesFolder = new File(args[0]);
        File entityListFile = new File(args[1]);
        EntityType entityListType = EntityType.fromString(args[2]);
        EntityType otherEntityType = EntityType.fromString(args[3]);
        boolean listEntityAtEnd = Boolean.parseBoolean(args[4]); // if true, look for paths where list entities are at end
        String outputFile = args[5];

        // get entity list and list of visitors
        List<Visitor<DependencyPath>> visitors = new ArrayList<>();
        PrintWriter printWriter = new PrintWriter(outputFile);
        BufferedReader entityNameReader = new BufferedReader(new FileReader(entityListFile));
        String name;
        while ((name = entityNameReader.readLine()) != null) {
            visitors.add(new EntityTypePrintVisitor(name.trim(), entityListType, otherEntityType,
                    printWriter, listEntityAtEnd));
        }
        Visitor<DependencyPath> entityFindListVisitor = new ListVisitor<>(visitors);

        int fileCount = 0;
        for (File file : pubtatorDependenciesFolder.listFiles((dir, fileName) -> fileName.contains("pubtator"))) {
            DependencyPathFileTraverser traverser = new DependencyPathFileTraverser(new FileInputStream(file));
            traverser.traverseRows(entityFindListVisitor);
            fileCount++;
            System.out.println("Files done: " + fileCount);
        }
        printWriter.flush();
        printWriter.close();
    }

    private static class EntityTypePrintVisitor implements Visitor<DependencyPath> {
        private final String entity;
        private final EntityType entityType;
        private final EntityType otherEntityType;
        private final PrintWriter printWriter;
        private final boolean entityAtEnd;

        EntityTypePrintVisitor(String entity, EntityType entityType,
                               EntityType otherEntityType, PrintWriter printWriter, boolean entityAtEnd) {
            this.entity = entity;
            this.entityType = entityType;
            this.otherEntityType = otherEntityType;
            this.printWriter = printWriter;
            this.entityAtEnd = entityAtEnd;
        }

        @Override
        public void visit(DependencyPath dependencyPath) {
            if ((!entityAtEnd && entityType.equals(dependencyPath.getEntityTypeStartEntity()) &&
                    entity.equalsIgnoreCase(dependencyPath.getFoundStringStartEntity()) &&
                    otherEntityType.equals(dependencyPath.getEntityTypeEndEntity())) ||
                    (entityAtEnd && entityType.equals(dependencyPath.getEntityTypeEndEntity()) &&
                            entity.equalsIgnoreCase(dependencyPath.getFoundStringEndEntity()) &&
                            otherEntityType.equals(dependencyPath.getEntityTypeStartEntity()))) {
                printWriter.println(dependencyPath.toString());
                printWriter.flush();
            }
        }
    }
}
