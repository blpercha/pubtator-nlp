package pubtator.parsing;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import org.junit.Test;
import pubtator.DependencyPath;
import pubtator.DependencyPathFileTraverser;
import pubtator.PubTatorFileTraverser;
import pubtator.Visitor;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PubTatorToDependencyPathsVisitorTest {

    @Test
    public void testModelPaths() throws Exception {
        assertEquals(LexicalizedParser.DEFAULT_PARSER_LOC, "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
    }

    @Test
    public void testSerializeDeserialize() throws Exception {
        String fileText = "26026123|t|Suicidal Ideation Versus Hopelessness/Helplessness in Healthy Individuals " +
                "and in Patients with Benign Breast Disease and Breast Cancer: A Prospective Case-control Study " +
                "in Finland.\n" +
                "26026123|a|BACKGROUND/AIM: The relation between suicidal ideation versus hopelessness/" +
                "helplessness in healthy study subjects (HSS) and in patients with benign breast disease (BBD) " +
                "and breast cancer (BC) has not been compared to date in a prospective study. We, therefore, " +
                "investigated suicidal ideation versus hopelessness/helplessness in 115 patients. PATIENTS AND " +
                "METHODS: In the Kuopio Breast Cancer Study, 115 women with breast symptoms were evaluated for " +
                "hopelessness and helplessness versus suicidal/pessimistic thoughts before any diagnostic " +
                "procedures were carried-out. RESULTS: In the self-rating score (SRS), hopelessness and the " +
                "helplessness versus pessimistic thoughts were significantly correlated in the HSS, BBD and " +
                "BC groups. In the SRS, the weighted kappa-values for hopelessness versus pessimistic thoughts " +
                "in the BBD group were also statistically significant. There was also a significant positive " +
                "correlation in the examiner-rating score (ERS) in the hopelessness versus pessimistic thoughts " +
                "in the HSS, BBD and BC groups, as well as in the ERS, in the helplessness versus pessimistic " +
                "thoughts in the HSS and BBD groups. In SRS, the hopelessness and the helplessness versus suicidal " +
                "thoughts were significantly correlated in the HSS, BBD and BC groups. There was also a significant " +
                "positive correlation in the ERS in the hopelessness versus suicidal thoughts in the HSS, BBD and " +
                "BC groups, as well as in the ERS, in the helplessness versus suicidal thoughts in the BBD group. " +
                "CONCLUSION: A new finding with clinical relevance in the present work is the agreement between " +
                "hopelessness/helplessness versus suicidal/pessimistic thoughts in the self-rating and examiner-" +
                "rating. In the breast cancer diagnostic Unit, the identification of suicidal ideation is essential " +
                "in suicide prevention and it is important to assess and treat depression even though a subject " +
                "reports little suicidal ideation.\n" +
                "26026123\t95\t116\tBenign Breast Disease\tDisease\tMESH:D001941\n" +
                "26026123\t121\t134\tBreast Cancer\tDisease\tMESH:D001943\n" +
                "26026123\t272\t294\thealthy study subjects\tSpecies\tMESH:D014717\n" +
                "26026123\t296\t299\tHSS\tDisease\tMESH:D006210\n" +
                "26026123\t322\t343\tbenign breast disease\tDisease\tMESH:D001941\n" +
                "26026123\t354\t367\tbreast cancer\tDisease\tMESH:D001943\n" +
                "26026123\t559\t572\tBreast Cancer\tDisease\tMESH:D001943\n" +
                "26026123\t764\t781\tself-rating score\tDisease\tMESH:D012652\n" +
                "26026123\t783\t786\tSRS\tDisease\tMESH:D056730\n" +
                "26026123\t888\t891\tHSS\tDisease\tMESH:D006210\n" +
                "26026123\t919\t922\tSRS\tDisease\tMESH:D056730\n" +
                "26026123\t95\t116\tBenign Breast Disease\tDisease\tMESH:D001941\n" +
                "26026123\t121\t134\tBreast Cancer\tDisease\tMESH:D001943\n" +
                "26026123\t272\t294\thealthy study subjects\tDisease\tMESH:D014717\n" +
                "26026123\t296\t299\tHSS\tDisease\tMESH:D006210\n" +
                "26026123\t322\t343\tbenign breast disease\tDisease\tMESH:D001941\n" +
                "26026123\t354\t367\tbreast cancer\tDisease\tMESH:D001943\n" +
                "26026123\t559\t572\tBreast Cancer\tDisease\tMESH:D001943\n" +
                "26026123\t764\t781\tself-rating score\tDisease\tMESH:D012652\n" +
                "26026123\t783\t786\tSRS\tDisease\tMESH:D056730\n" +
                "26026123\t888\t891\tHSS\tDisease\tMESH:D006210\n" +
                "26026123\t919\t922\tSRS\tDisease\tMESH:D056730\n" +
                "26026123\t1106\t1127\texaminer-rating score\tDisease\tMESH:C536766\n" +
                "26026123\t1189\t1192\tHSS\tDisease\tMESH:D006210\n" +
                "26026123\t1291\t1294\tHSS\tDisease\tMESH:D006210\n" +
                "26026123\t1314\t1317\tSRS\tDisease\tMESH:D056730\n" +
                "26026123\t1106\t1127\texaminer-rating score\tDisease\tMESH:C536766\n" +
                "26026123\t1189\t1192\tHSS\tDisease\tMESH:D006210\n" +
                "26026123\t1291\t1294\tHSS\tDisease\tMESH:D006210\n" +
                "26026123\t1314\t1317\tSRS\tDisease\tMESH:D056730\n" +
                "26026123\t1419\t1422\tHSS\tDisease\tMESH:D006210\n" +
                "26026123\t1556\t1559\tHSS\tDisease\tMESH:D006210\n" +
                "26026123\t1871\t1884\tbreast cancer\tDisease\tMESH:D001943\n" +
                "26026123\t1419\t1422\tHSS\tDisease\tMESH:D006210\n" +
                "26026123\t1556\t1559\tHSS\tDisease\tMESH:D006210\n" +
                "26026123\t1871\t1884\tbreast cancer\tDisease\tMESH:D001943\n";
        PubTatorFileTraverser traverser = new PubTatorFileTraverser(new ByteArrayInputStream(fileText.getBytes()));
        ObjectCollectVisitor<DependencyPath> pathCollectVisitor = new ObjectCollectVisitor<>();
        ToDependencyPathsVisitor visitor = new ToDependencyPathsVisitor(1, 50, pathCollectVisitor);
        traverser.traverseRows(visitor);

        // see what dependency paths were collected
        Set<DependencyPath> dependencyPathsFound = pathCollectVisitor.getCollectedObjects();
        assertEquals(dependencyPathsFound.size(), 24);

        for (DependencyPath dependencyPath : dependencyPathsFound) {
            String pathAsString = dependencyPath.toString();

            ObjectCollectVisitor<DependencyPath> readInVisitor = new ObjectCollectVisitor<>();

            DependencyPathFileTraverser traverser1 = new DependencyPathFileTraverser(
                    new ByteArrayInputStream(pathAsString.getBytes()));
            traverser1.traverseRows(readInVisitor);

            if (readInVisitor.getCollectedObjects().size() == 0) {
                System.out.println("no path: " + dependencyPath);
                continue;
            }

            assertEquals(readInVisitor.getCollectedObjects().size(), 1);

            DependencyPath foundPath = new ArrayList<>(readInVisitor.getCollectedObjects()).get(0);

            assertEquals(dependencyPath.getStartEntity(), foundPath.getStartEntity());
            assertEquals(dependencyPath.getEndEntity(), foundPath.getEndEntity());
            assertEquals(dependencyPath.getEntityIdStartEntity(), foundPath.getEntityIdStartEntity());
            assertEquals(dependencyPath.getEntityIdEndEntity(), foundPath.getEntityIdEndEntity());
            assertEquals(dependencyPath.getPath(), foundPath.getPath());
            assertEquals(dependencyPath.getSentenceString(), foundPath.getSentenceString());
            assertEquals(dependencyPath.getFoundStringStartEntity(), foundPath.getFoundStringStartEntity());
            assertEquals(dependencyPath.getFoundStringEndEntity(), foundPath.getFoundStringEndEntity());
            assertEquals(dependencyPath.getSentenceNumber(), foundPath.getSentenceNumber());
            assertEquals(dependencyPath.getPmid(), foundPath.getPmid());

            assertEquals(dependencyPath, foundPath);
        }
    }

    private class ObjectCollectVisitor<T> implements Visitor<T> {
        private final Set<T> collectedObjects = new HashSet<>();

        @Override
        public void visit(T item) {
            collectedObjects.add(item);
        }

        Set<T> getCollectedObjects() {
            return collectedObjects;
        }
    }
}