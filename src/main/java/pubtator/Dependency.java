package pubtator;

import java.util.Objects;

public class Dependency {
    private final String gov;
    private final String dep;
    private final String reln;

    public Dependency(String gov, String dep, String reln) {
        this.gov = gov;
        this.dep = dep;
        this.reln = reln;
    }

    public String getGov() {
        return gov;
    }

    public String getDep() {
        return dep;
    }

    public String getReln() {
        return reln;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dependency)) return false;
        Dependency that = (Dependency) o;
        return Objects.equals(gov, that.gov) &&
                Objects.equals(dep, that.dep) &&
                Objects.equals(reln, that.reln);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gov, dep, reln);
    }

    @Override
    public String toString() {
        return gov + "|" + reln + "|" + dep;
    }
}
