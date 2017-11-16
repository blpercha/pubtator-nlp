package pubtator;

public enum EntityType {
    Disease, Species, Gene, Chemical, ProteinMutation, DNAMutation, FamilyName, DomainMotif, SNP, OtherNull;

    public static EntityType fromString(String inputString) {
        switch (inputString) {
            case "Disease":
                return Disease;
            case "Species":
                return Species;
            case "Gene":
                return Gene;
            case "Chemical":
                return Chemical;
            case "ProteinMutation":
                return ProteinMutation;
            case "DNAMutation":
                return DNAMutation;
            case "FamilyName":
                return FamilyName;
            case "DomainMotif":
                return DomainMotif;
            case "SNP":
                return SNP;
            case "OtherNull":
                return OtherNull;
        }
        return null;
    }
}
