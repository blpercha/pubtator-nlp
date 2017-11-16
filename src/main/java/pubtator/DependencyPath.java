package pubtator;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DependencyPath {
    // 3513695	1	tumours	175,182	CEA	162,165	tumours	CEA	MESH:D015275	1048	Disease	Gene	END_ENTITY|nmod|START_ENTITY
    // 3513695	1	tumours	175,182	human	169,174	tumours	human	MESH:D015275	9606	Disease	Species	START_ENTITY|amod|END_ENTITY

    private int pmid;
    private int sentenceNumber;
    private IndexedToken startEntity;
    private IndexedToken endEntity;
    private String foundStringStartEntity;
    private String foundStringEndEntity;
    private String entityIdStartEntity;
    private String entityIdEndEntity;
    private EntityType entityTypeStartEntity;
    private EntityType entityTypeEndEntity;
    private List<Dependency> path;
    private String sentenceString;

    public DependencyPath(int pmid, int sentenceNumber, IndexedToken startEntity,
                          IndexedToken endEntity, String foundStringStartEntity,
                          String foundStringEndEntity, String entityIdStartEntity, String entityIdEndEntity,
                          EntityType entityTypeStartEntity, EntityType entityTypeEndEntity,
                          List<Dependency> path, String sentenceString) {
        this.pmid = pmid;
        this.sentenceNumber = sentenceNumber;
        this.startEntity = startEntity;
        this.endEntity = endEntity;
        this.foundStringStartEntity = foundStringStartEntity;
        this.foundStringEndEntity = foundStringEndEntity;
        this.entityIdStartEntity = entityIdStartEntity;
        this.entityIdEndEntity = entityIdEndEntity;
        this.entityTypeStartEntity = entityTypeStartEntity;
        this.entityTypeEndEntity = entityTypeEndEntity;
        this.path = path;
        this.sentenceString = sentenceString;
    }

    private DependencyPath() {

    }

    private void setSentenceString(String sentenceString) {
        this.sentenceString = sentenceString;
    }

    private void setPmid(int pmid) {
        this.pmid = pmid;
    }

    private void setSentenceNumber(int sentenceNumber) {
        this.sentenceNumber = sentenceNumber;
    }

    private void setFoundStringStartEntity(String foundStringStartEntity) {
        this.foundStringStartEntity = foundStringStartEntity;
    }

    private void setFoundStringEndEntity(String foundStringEndEntity) {
        this.foundStringEndEntity = foundStringEndEntity;
    }

    private void setEntityIdStartEntity(String entityIdStartEntity) {
        this.entityIdStartEntity = entityIdStartEntity;
    }

    private void setEntityIdEndEntity(String entityIdEndEntity) {
        this.entityIdEndEntity = entityIdEndEntity;
    }

    private void setEntityTypeStartEntity(EntityType entityTypeStartEntity) {
        this.entityTypeStartEntity = entityTypeStartEntity;
    }

    private void setEntityTypeEndEntity(EntityType entityTypeEndEntity) {
        this.entityTypeEndEntity = entityTypeEndEntity;
    }

    private void setPath(List<Dependency> path) {
        this.path = path;
    }

    public int getPmid() {
        return pmid;
    }

    public int getSentenceNumber() {
        return sentenceNumber;
    }

    public String getFoundStringStartEntity() {
        return foundStringStartEntity;
    }

    public String getFoundStringEndEntity() {
        return foundStringEndEntity;
    }

    public String getEntityIdStartEntity() {
        return entityIdStartEntity;
    }

    public String getEntityIdEndEntity() {
        return entityIdEndEntity;
    }

    public EntityType getEntityTypeStartEntity() {
        return entityTypeStartEntity;
    }

    public EntityType getEntityTypeEndEntity() {
        return entityTypeEndEntity;
    }

    public List<Dependency> getPath() {
        return path;
    }

    public IndexedToken getStartEntity() {
        return startEntity;
    }

    private void setStartEntity(IndexedToken startEntity) {
        this.startEntity = startEntity;
    }

    public IndexedToken getEndEntity() {
        return endEntity;
    }

    private void setEndEntity(IndexedToken endEntity) {
        this.endEntity = endEntity;
    }

    public String getSentenceString() {
        return sentenceString;
    }

    @Override
    public String toString() {
        List<String> pathStrings = new ArrayList<>();
        for (Dependency dependency : path) {
            pathStrings.add(dependency.getGov() + "|" + dependency.getReln() + "|" + dependency.getDep());
        }
        String pathAsString = StringUtils.join(pathStrings, " ");
        return pmid + "\t" + sentenceNumber + "\t" + startEntity.word() + "\t" +
                startEntity.startPosition() + "," + startEntity.endPosition() + "\t" +
                endEntity.word() + "\t" +
                endEntity.startPosition() + "," + endEntity.endPosition() + "\t" +
                foundStringStartEntity + "\t" + foundStringEndEntity + "\t" +
                entityIdStartEntity + "\t" + entityIdEndEntity + "\t" +
                entityTypeStartEntity + "\t" + entityTypeEndEntity + "\t" +
                pathAsString + "\t" + sentenceString;
    }

    static DependencyPath fromString(String dependencyPathOutputLine) {
        String[] splitLine = dependencyPathOutputLine.trim().split("\t");

        try {
            DependencyPath newPath = new DependencyPath();
            newPath.setPmid(Integer.parseInt(splitLine[0]));
            newPath.setSentenceNumber(Integer.parseInt(splitLine[1]));

            String[] splitStartEntityPosition = splitLine[3].split(",");
            IndexedToken startEntity = new IndexedToken(splitLine[2],
                    Integer.parseInt(splitStartEntityPosition[0]),
                    Integer.parseInt(splitStartEntityPosition[1]));
            newPath.setStartEntity(startEntity);

            String[] splitEndEntityPosition = splitLine[5].split(",");
            IndexedToken endEntity = new IndexedToken(splitLine[4],
                    Integer.parseInt(splitEndEntityPosition[0]),
                    Integer.parseInt(splitEndEntityPosition[1]));
            newPath.setEndEntity(endEntity);

            newPath.setFoundStringStartEntity(splitLine[6]);
            newPath.setFoundStringEndEntity(splitLine[7]);
            if (splitLine[8].equals("null")) {
                newPath.setEntityIdStartEntity(null);
            } else {
                newPath.setEntityIdStartEntity(splitLine[8]);
            }
            if (splitLine[9].equals("null")) {
                newPath.setEntityIdEndEntity(null);
            } else {
                newPath.setEntityIdEndEntity(splitLine[9]);
            }
            newPath.setEntityTypeStartEntity(EntityType.fromString(splitLine[10]));
            newPath.setEntityTypeEndEntity(EntityType.fromString(splitLine[11]));

            String[] formattedPath = splitLine[12].split(" ");
            List<Dependency> path = new ArrayList<>();
            for (String pathElement : formattedPath) {
                String[] splitElement = pathElement.split("\\|");
                path.add(new Dependency(splitElement[0], splitElement[2], splitElement[1]));
            }
            newPath.setPath(path);

            String sentenceString = splitLine[13];
            newPath.setSentenceString(sentenceString);

            return newPath;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DependencyPath that = (DependencyPath) o;

        if (pmid != that.pmid) return false;
        if (sentenceNumber != that.sentenceNumber) return false;
        if (startEntity != null ? !startEntity.equals(that.startEntity) : that.startEntity != null) return false;
        if (endEntity != null ? !endEntity.equals(that.endEntity) : that.endEntity != null) return false;
        if (foundStringStartEntity != null ? !foundStringStartEntity.equals(that.foundStringStartEntity) : that.foundStringStartEntity != null)
            return false;
        if (foundStringEndEntity != null ? !foundStringEndEntity.equals(that.foundStringEndEntity) : that.foundStringEndEntity != null)
            return false;
        if (entityIdStartEntity != null ? !entityIdStartEntity.equals(that.entityIdStartEntity) : that.entityIdStartEntity != null)
            return false;
        if (entityIdEndEntity != null ? !entityIdEndEntity.equals(that.entityIdEndEntity) : that.entityIdEndEntity != null)
            return false;
        if (entityTypeStartEntity != that.entityTypeStartEntity) return false;
        if (entityTypeEndEntity != that.entityTypeEndEntity) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        return sentenceString != null ? sentenceString.equals(that.sentenceString) : that.sentenceString == null;
    }

    @Override
    public int hashCode() {
        int result = pmid;
        result = 31 * result + sentenceNumber;
        result = 31 * result + (startEntity != null ? startEntity.hashCode() : 0);
        result = 31 * result + (endEntity != null ? endEntity.hashCode() : 0);
        result = 31 * result + (foundStringStartEntity != null ? foundStringStartEntity.hashCode() : 0);
        result = 31 * result + (foundStringEndEntity != null ? foundStringEndEntity.hashCode() : 0);
        result = 31 * result + (entityIdStartEntity != null ? entityIdStartEntity.hashCode() : 0);
        result = 31 * result + (entityIdEndEntity != null ? entityIdEndEntity.hashCode() : 0);
        result = 31 * result + (entityTypeStartEntity != null ? entityTypeStartEntity.hashCode() : 0);
        result = 31 * result + (entityTypeEndEntity != null ? entityTypeEndEntity.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (sentenceString != null ? sentenceString.hashCode() : 0);
        return result;
    }
}
