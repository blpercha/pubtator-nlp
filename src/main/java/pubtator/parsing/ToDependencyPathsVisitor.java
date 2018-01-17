package pubtator.parsing;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.DirectedGraph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import pubtator.Dependency;
import pubtator.DependencyPath;
import pubtator.IndexedToken;
import pubtator.PubTatorRecord;
import pubtator.Visitor;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ToDependencyPathsVisitor implements Visitor<PubTatorRecord> {
    private final LexicalizedParser lexicalizedParser =
            LexicalizedParser.getParserFromSerializedFile(LexicalizedParser.DEFAULT_PARSER_LOC);
    private final GrammaticalStructureFactory grammaticalStructureFactory =
            (new PennTreebankLanguagePack()).grammaticalStructureFactory();

    private final int minSentenceLength;
    private final int maxSentenceLength;
    private final Visitor<DependencyPath> dependencyPathVisitor;

    ToDependencyPathsVisitor(int minSentenceLength, int maxSentenceLength,
                             Visitor<DependencyPath> dependencyPathVisitor) {
        this.minSentenceLength = minSentenceLength;
        this.maxSentenceLength = maxSentenceLength;
        this.dependencyPathVisitor = dependencyPathVisitor;
    }

    @Override
    public void visit(PubTatorRecord item) {
        String recordText = item.getText();
        Set<String> modifiedTargetStrings = new HashSet<>();

        // turn found entities into single tokens so they're parsed correctly
        for (String targetString : new HashSet<>(item.getStrings())) {
            String modifiedTargetString = targetString.replace(' ', '_');
            recordText = recordText.replace(targetString, modifiedTargetString);
            modifiedTargetStrings.add(modifiedTargetString);
        }

        // break document into sentences, parse each one, find dependency paths between found entity pairs
        DocumentPreprocessor dp = new DocumentPreprocessor(new StringReader(recordText));
        int sentenceNumber = 0;
        for (List<HasWord> sentence : dp) {
            if (sentence.size() < minSentenceLength || sentence.size() > maxSentenceLength) {
                System.err.println("Sentence length too long: " + sentence.size());
                continue;
            }
            Tree tree = lexicalizedParser.parse(sentence);
            GrammaticalStructure grammaticalStructure =
                    grammaticalStructureFactory.newGrammaticalStructure(tree);
            Collection<TypedDependency> dependencies =
                    grammaticalStructure.typedDependencies(GrammaticalStructure.Extras.MAXIMAL);
            DirectedGraph<IndexedToken, Dependency> graph = getGraph(dependencies);
            if (graph == null) {
                System.err.println("Sentence causing graph error: " + item.getPmid() + ":" + sentenceNumber);
                continue;  // this basically means skipping graphs with cycles
            }
            for (IndexedToken t1 : graph.vertexSet()) {
                if (!modifiedTargetStrings.contains(t1.word())) {
                    continue;
                }
                for (IndexedToken t2 : graph.vertexSet()) {
                    if (t1.equals(t2)) {
                        continue;
                    }
                    if (!modifiedTargetStrings.contains(t2.word())) {
                        continue;
                    }
                    List<Dependency> path = getPath(graph, t1, t2);
                    if (path == null) {
                        continue;
                    }
                    List<String> formattedSentenceString = new ArrayList<>();
                    for (HasWord hasWord : sentence) {
                        formattedSentenceString.add(hasWord.word());
                    }
                    DependencyPath dependencyPath = new DependencyPath(item.getPmid(), sentenceNumber,
                            t1, t2,
                            item.getStringForPosition(t1.startPosition(), t1.endPosition()),
                            item.getStringForPosition(t2.startPosition(), t2.endPosition()),
                            item.getEntityIdForPosition(t1.startPosition(), t1.endPosition()),
                            item.getEntityIdForPosition(t2.startPosition(), t2.endPosition()),
                            item.getEntityTypeForPosition(t1.startPosition(), t1.endPosition()),
                            item.getEntityTypeForPosition(t2.startPosition(), t2.endPosition()),
                            formatPath(path, t1, t2), StringUtils.join(formattedSentenceString, ' '));
                    dependencyPathVisitor.visit(dependencyPath);
                }
            }
            sentenceNumber++;
        }
    }

    private DirectedGraph<IndexedToken, Dependency> getGraph(Collection<TypedDependency> typedDependencies) {
        DirectedGraph<IndexedToken, Dependency> graph =
                new DirectedAcyclicGraph<>(new ClassBasedEdgeFactory<>(Dependency.class));
        for (TypedDependency typedDependency : typedDependencies) {
            IndexedToken govToken = new IndexedToken(typedDependency.gov().word(),
                    typedDependency.gov().beginPosition(), typedDependency.gov().endPosition());
            IndexedToken depToken = new IndexedToken(typedDependency.dep().word(),
                    typedDependency.dep().beginPosition(), typedDependency.dep().endPosition());
            graph.addVertex(govToken);
            graph.addVertex(depToken);
            try {
                graph.addEdge(govToken, depToken, new Dependency(govToken.word(),
                        depToken.word(), typedDependency.reln().getShortName()));
            } catch (IllegalArgumentException e) {
                System.err.println("Cycle found: " + graph.edgeSet() + "\t" + typedDependency);
                return null;
            }
        }
        return graph;
    }

    private List<Dependency> getPath(DirectedGraph<IndexedToken, Dependency> graph,
                                     IndexedToken startVertex,
                                     IndexedToken endVertex) {
        UndirectedGraph<IndexedToken, Dependency> undirectedGraph = new AsUndirectedGraph<>(graph);
        ConnectivityInspector<IndexedToken, Dependency> connectivityInspector = new ConnectivityInspector<>(undirectedGraph);
        if (!connectivityInspector.pathExists(startVertex, endVertex)) {
            System.err.println("no path exists between: " + startVertex + "\t" + endVertex);
            return null;
        }

        return DijkstraShortestPath.findPathBetween(undirectedGraph, startVertex, endVertex).getEdgeList();
    }

    private List<Dependency> formatPath(List<Dependency> inputPath, IndexedToken startToken,
                                        IndexedToken endToken) {
        int pathSize = inputPath.size();
        List<Dependency> formattedPath = new ArrayList<>();

        // reformat starting edge
        Dependency startEdge = inputPath.get(0);
        String startGov = startEdge.getGov();
        String startDep = startEdge.getDep();
        if (startGov.equals(startToken.word())) {
            startGov = "START_ENTITY";
        } else if (startDep.equals(startToken.word())) {
            startDep = "START_ENTITY";
        }
        if (pathSize == 1) {
            if (startGov.equals(endToken.word())) {
                startGov = "END_ENTITY";
            } else if (startDep.equals(endToken.word())) {
                startDep = "END_ENTITY";
            }
        }
        formattedPath.add(new Dependency(startGov, startDep, startEdge.getReln()));

        if (pathSize > 1) {
            // add middle edges
            formattedPath.addAll(inputPath.subList(1, pathSize - 1));

            // reformat ending edge
            Dependency endEdge = inputPath.get(pathSize - 1);
            String endGov = endEdge.getGov();
            String endDep = endEdge.getDep();
            if (endGov.equals(endToken.word())) {
                endGov = "END_ENTITY";
            } else if (endDep.equals(endToken.word())) {
                endDep = "END_ENTITY";
            }
            formattedPath.add(new Dependency(endGov, endDep, endEdge.getReln()));
        }

        return formattedPath;
    }
}
