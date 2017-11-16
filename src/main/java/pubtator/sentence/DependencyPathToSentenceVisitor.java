package pubtator.sentence;

import org.apache.commons.lang3.StringUtils;
import pubtator.DependencyPath;
import pubtator.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DependencyPathToSentenceVisitor implements Visitor<DependencyPath> {
    private final Map<String, Set<String>> pathToSentenceMap;

    DependencyPathToSentenceVisitor(Map<String, Set<String>> pathToSentenceMap) {
        this.pathToSentenceMap = pathToSentenceMap;
    }

    @Override
    public void visit(DependencyPath item) {
        List<String> formattedPath = new ArrayList<>();
        item.getPath().forEach(typedDependency -> formattedPath.add(typedDependency.getGov() + "|" +
                typedDependency.getReln() + "|" + typedDependency.getDep()));
        String feature = StringUtils.join(formattedPath, " ").toLowerCase(Locale.US); // get entire dependency path

        if (!pathToSentenceMap.containsKey(feature)) {
            return;
        }

        pathToSentenceMap.get(feature).add(item.getSentenceString() + "\t" + item.getStartEntity().word() + "\t" +
                item.getEndEntity().word());
    }
}
