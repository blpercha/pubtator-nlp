package pubtator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class PubTatorFileTraverser {
    private final InputStream inputStream;

    private final Pattern TITLE_SPLIT_PATTERN = Pattern.compile("\\|");
    private final Pattern ABSTRACT_LINE_SPLIT_PATTERN = Pattern.compile("\\|");
    private final Pattern ANNOTATION_LINE_SPLIT_PATTERN = Pattern.compile("\t");

    public PubTatorFileTraverser(InputStream pubTatorFormattedInputStream) {
        this.inputStream = pubTatorFormattedInputStream;
    }

    public void traverseRows(Visitor<PubTatorRecord> visitor) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        PubTatorRecord record;
        while (true) {
            record = new PubTatorRecord();
            String titleLine = bufferedReader.readLine();
            if (titleLine == null) {
                break;
            }
            String[] titleData = TITLE_SPLIT_PATTERN.split(titleLine);
            record.setPmid(Integer.parseInt(titleData[0]));
            if (titleData.length == 3) {
                record.setTitle(titleData[2]);
            } else {
                record.setTitle("");
            }
            String[] abstractLine = ABSTRACT_LINE_SPLIT_PATTERN.split(bufferedReader.readLine());
            if (abstractLine.length == 3) {
                record.setAbstractText(abstractLine[2]);
                record.setText(record.getTitle() + "\n" + record.getAbstractText());
            } else {
                record.setAbstractText("");
                record.setText(record.getTitle());
            }
            String[] annotationLine = ANNOTATION_LINE_SPLIT_PATTERN.split(bufferedReader.readLine());
            while (annotationLine.length > 1) {
                int start = Integer.parseInt(annotationLine[1]);
                int end = Integer.parseInt(annotationLine[2]);
                record.addPosition(start, end);
                record.addString(annotationLine[3]);
                record.setStringForPosition(start, end, annotationLine[3]);
                if (annotationLine.length >= 5) {
                    record.addEntityType(EntityType.fromString(annotationLine[4]));
                    record.setEntityTypeForPosition(start, end, EntityType.fromString(annotationLine[4]));
                } else {
                    record.addEntityType(null);
                }
                if (annotationLine.length == 6) {
                    record.setEntityIdForPosition(start, end, annotationLine[5]);
                    record.addEntityId(annotationLine[5]);
                } else {
                    record.addEntityId(null);
                }
                try {
                    annotationLine = ANNOTATION_LINE_SPLIT_PATTERN.split(bufferedReader.readLine());
                } catch (NullPointerException e) {
                    break;
                }
            }
            visitor.visit(record);
        }
        bufferedReader.close();
    }
}
