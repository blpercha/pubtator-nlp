package pubtator.parsing;

import pubtator.ObjectPrintVisitor;
import pubtator.PubTatorFileTraverser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

class PrintDependencyPaths {
    public static void main(String[] args) throws Exception {
        String pubtatorFile = args[0];
        String outputFile = args[1];
        int minSentenceLength = Integer.parseInt(args[2]);
        int maxSentenceLength = Integer.parseInt(args[3]);

        // parse the file and write output
        PubTatorFileTraverser traverser = new PubTatorFileTraverser(new FileInputStream(pubtatorFile));
        OutputStream outputStream = new FileOutputStream(outputFile);
        ToDependencyPathsVisitor visitor = new ToDependencyPathsVisitor(minSentenceLength, maxSentenceLength,
                new ObjectPrintVisitor<>(outputStream));
        traverser.traverseRows(visitor);
        outputStream.flush();
        outputStream.close();
    }
}
