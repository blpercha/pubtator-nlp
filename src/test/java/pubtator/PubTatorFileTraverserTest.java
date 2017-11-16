package pubtator;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PubTatorFileTraverserTest {
    private final String fileText = "26026123|t|Suicidal Ideation Versus Hopelessness/Helplessness in Healthy Individuals and in Patients with Benign Breast Disease and Breast Cancer: A Prospective Case-control Study in Finland.\n" +
            "26026123|a|BACKGROUND/AIM: The relation between suicidal ideation versus hopelessness/helplessness in healthy study subjects (HSS) and in patients with benign breast disease (BBD) and breast cancer (BC) has not been compared to date in a prospective study. We, therefore, investigated suicidal ideation versus hopelessness/helplessness in 115 patients. PATIENTS AND METHODS: In the Kuopio Breast Cancer Study, 115 women with breast symptoms were evaluated for hopelessness and helplessness versus suicidal/pessimistic thoughts before any diagnostic procedures were carried-out. RESULTS: In the self-rating score (SRS), hopelessness and the helplessness versus pessimistic thoughts were significantly correlated in the HSS, BBD and BC groups. In the SRS, the weighted kappa-values for hopelessness versus pessimistic thoughts in the BBD group were also statistically significant. There was also a significant positive correlation in the examiner-rating score (ERS) in the hopelessness versus pessimistic thoughts in the HSS, BBD and BC groups, as well as in the ERS, in the helplessness versus pessimistic thoughts in the HSS and BBD groups. In SRS, the hopelessness and the helplessness versus suicidal thoughts were significantly correlated in the HSS, BBD and BC groups. There was also a significant positive correlation in the ERS in the hopelessness versus suicidal thoughts in the HSS, BBD and BC groups, as well as in the ERS, in the helplessness versus suicidal thoughts in the BBD group. CONCLUSION: A new finding with clinical relevance in the present work is the agreement between hopelessness/helplessness versus suicidal/pessimistic thoughts in the self-rating and examiner-rating. In the breast cancer diagnostic Unit, the identification of suicidal ideation is essential in suicide prevention and it is important to assess and treat depression even though a subject reports little suicidal ideation.\n" +
            "26026123\t0\t17\tSuicidal Ideation\tDisease\tMESH:D001\n" +
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
            "26026123\t1189\t1192\tHSS\n" +
            "26026123\t1291\t1294\tHSS\tDisease\tMESH:D006210\n" +
            "26026123\t1314\t1317\tSRS\tDisease\tMESH:D056730\n" +
            "26026123\t1419\t1422\tHSS\tDisease\tMESH:D006210\n" +
            "26026123\t1556\t1559\tHSS\tDisease\tMESH:D006210\n" +
            "26026123\t1871\t1884\tbreast cancer\tDisease\tMESH:D001943\n" +
            "26026123\t1419\t1422\tHSS\tDisease\tMESH:D006210\n" +
            "26026123\t1556\t1559\tHSS\tDisease\tMESH:D006210\n" +
            "26026123\t1871\t1884\tbreast cancer\tDisease\tMESH:D001943\n" +
            "\n" +
            "26102214|t|Mechanistic Effects of Calcitriol in Cancer Biology.\n" +
            "26102214|a|UNASSIGNED: Besides its classical biological effects on calcium and phosphorus homeostasis, calcitriol, the active vitamin D metabolite, has a broad variety of actions including anticancer effects that are mediated either transcriptionally and/or via non-genomic pathways. In the context of cancer, calcitriol regulates the cell cycle, induces apoptosis, promotes cell differentiation and acts as anti-inflammatory factor within the tumor microenvironment. In this review, we address the different mechanisms of action involved in the antineoplastic effects of calcitriol.\n" +
            "26102214\t109\t116\tcalcium\tChemical\n" +
            "26102214\t121\t131\tphosphorus\tChemical\tMESH:D010758\n" +
            "26102214\t145\t155\tcalcitriol\tChemical\tMESH:D002117\n" +
            "26102214\t168\t177\tvitamin D\tChemical\tMESH:D014807\n" +
            "26102214\t352\t362\tcalcitriol\tChemical\tMESH:D002117\n" +
            "26102214\t23\t33\tCalcitriol\tChemical\tMESH:D002117\n" +
            "26102214\t614\t624\tcalcitriol\tChemical\tMESH:D002117\n" +
            "26102214\t344\t350\tcancer\tDisease\tMESH:D009369\n" +
            "26102214\t486\t491\ttumor\tDisease\tMESH:D009369\n" +
            "\n" +
            "26073013|t|Influence of HEK293 metabolism on the production of viral vectors and vaccine.\n" +
            "26073013|a|UNASSIGNED: Mammalian cell cultures are increasingly used for the production of complex biopharmaceuticals including viral vectors and vaccines. HEK293 is the predominant cell line used for the transient expression of recombinant proteins and a well-established system for the production of viral vectors. Understanding metabolic requirements for high productivity in HEK293 cells remains an important area of investigation. Many authors have presented approaches for increased productivity through optimization of cellular metabolism from two distinct perspectives. One is a non-targeted approach, which is directed to improving feeding strategies by addition of exhausted or critical substrates and eventually removal of toxic metabolites. Alternatively, a targeted approach has attempted to identify specific targets for optimization through better understanding of the cellular metabolism under different operating conditions. This review will present both approaches and their successes with regards to improvement of viral production in HEK293 cells outlining the key relations between HEK293 cell metabolism and viral vector productivity. Also, we will summarize the current knowledge on HEK293 metabolism indicating remaining issues to address and problems to resolve to maximize the productivity of viral vectors in HEK293 cells.\n" +
            "26073013\t91\t100\tMammalian\tSpecies\t9606";

    @Test
    public void testFileTraverser() {
        PubTatorFileTraverser traverser = new PubTatorFileTraverser(new ByteArrayInputStream(fileText.getBytes()));
        RecordCountVisitor visitor = new RecordCountVisitor();
        try {
            traverser.traverseRows(visitor);
        } catch (Exception e) {
            throw new AssertionError("PubTatorFileTraverserTest: Cannot traverse rows.");
        }
        assertEquals(visitor.getCount(), 3);
    }

    @Test
    public void testFileTraverser2() {
        PubTatorFileTraverser traverser = new PubTatorFileTraverser(new ByteArrayInputStream(fileText.getBytes()));
        RecordStoreVisitor visitor = new RecordStoreVisitor();
        try {
            traverser.traverseRows(visitor);
        } catch (Exception e) {
            throw new AssertionError("PubTatorFileTraverserTest: Cannot traverse rows.");
        }
        assertEquals(visitor.getRecords().size(), 3);
        assertEquals(visitor.getRecords().get(0).entityTypes.size(), 36);
        assertEquals(visitor.getRecords().get(1).entityTypes.size(), 9);
        assertEquals(visitor.getRecords().get(2).entityTypes.size(), 1);
        assertEquals(visitor.getRecords().get(0).entityIds.size(), 36);
        assertEquals(visitor.getRecords().get(1).entityIds.size(), 9);
        assertEquals(visitor.getRecords().get(2).entityIds.size(), 1);
    }

    private class RecordStoreVisitor implements Visitor<PubTatorRecord> {
        private final List<PubTatorRecord> records = new ArrayList<>();

        @Override
        public void visit(PubTatorRecord item) {
            records.add(item);
        }

        List<PubTatorRecord> getRecords() {
            return records;
        }
    }

    private class RecordCountVisitor implements Visitor<PubTatorRecord> {
        private int count = 0;

        @Override
        public void visit(PubTatorRecord item) {
            count++;
        }

        int getCount() {
            return count;
        }
    }
}