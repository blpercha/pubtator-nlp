package pubtator;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PubTatorRecord {
    private int pmid;
    private String title;
    private String abstractText;
    private String text;
    private final List<Pair<Integer, Integer>> positions = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();
    final List<EntityType> entityTypes = new ArrayList<>();
    final List<String> entityIds = new ArrayList<>();
    private final Map<Pair<Integer, Integer>, String> positionToStringMap = new HashMap<>();
    private final Map<Pair<Integer, Integer>, EntityType> positionToEntityType = new HashMap<>();
    private final Map<Pair<Integer, Integer>, String> positionToEntityId = new HashMap<>();

    @SuppressWarnings("unused")
    public int getPmid() {
        return pmid;
    }

    void setPmid(int pmid) {
        this.pmid = pmid;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getAbstractText() {
        return abstractText;
    }

    void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    @SuppressWarnings("unused")
    public String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }

    void addPosition(int start, int end) {
        positions.add(Pair.of(start, end));
    }

    void addString(String string) {
        strings.add(string);
    }

    void addEntityType(EntityType entityType) {
        entityTypes.add(entityType);
    }

    void addEntityId(String entityId) {
        entityIds.add(entityId);
    }

    @SuppressWarnings("unused")
    List<String> getEntityIds() {
        return entityIds;
    }

    @SuppressWarnings("unused")
    public List<String> getStrings() {
        return strings;
    }

    public List<Pair<Integer, Integer>> getPositions() {
        return positions;
    }

    @SuppressWarnings("unused")
    List<Pair<Integer, Integer>> getSortedPositions() {
        List<Pair<Integer, Integer>> sortedPositions = new ArrayList<>(positions);
        Collections.sort(sortedPositions);
        return sortedPositions;
    }

    public String getStringForPosition(int start, int end) {
        Pair<Integer, Integer> pair = Pair.of(start, end);
        if (positionToStringMap.containsKey(pair)) {
            return positionToStringMap.get(pair);
        }
        return null;
    }

    public EntityType getEntityTypeForPosition(int start, int end) {
        Pair<Integer, Integer> pair = Pair.of(start, end);
        if (positionToEntityType.containsKey(pair)) {
            return positionToEntityType.get(pair);
        }
        return EntityType.OtherNull;
    }

    public String getEntityIdForPosition(int start, int end) {
        Pair<Integer, Integer> pair = Pair.of(start, end);
        if (positionToEntityId.containsKey(pair)) {
            return positionToEntityId.get(pair);
        }
        return null;
    }

    void setStringForPosition(int start, int end, String string) {
        positionToStringMap.put(Pair.of(start, end), string);
    }

    void setEntityTypeForPosition(int start, int end, EntityType type) {
        positionToEntityType.put(Pair.of(start, end), type);
    }

    void setEntityIdForPosition(int start, int end, String id) {
        positionToEntityId.put(Pair.of(start, end), id);
    }

    @Override
    public String toString() {
        return "PubTatorRecord{" +
                "pmid='" + pmid + '\'' +
                ", title='" + title + '\'' +
                ", abstractText='" + abstractText + '\'' +
                ", text='" + text + '\'' +
                ", positions=" + positions +
                ", strings=" + strings +
                ", entityTypes=" + entityTypes +
                ", entityIds=" + entityIds +
                '}';
    }

}