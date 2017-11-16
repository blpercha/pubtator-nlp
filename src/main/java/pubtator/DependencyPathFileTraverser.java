package pubtator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DependencyPathFileTraverser {
    private final InputStream inputStream;

    public DependencyPathFileTraverser(InputStream dependencyFormattedInputStream) {
        this.inputStream = dependencyFormattedInputStream;
    }

    public void traverseRows(Visitor<DependencyPath> visitor) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            DependencyPath dependencyPath = DependencyPath.fromString(line);
            if (dependencyPath == null) {
                System.err.println("Invalid dependency path: " + line);
                continue;
            }
            visitor.visit(dependencyPath);
        }
    }
}