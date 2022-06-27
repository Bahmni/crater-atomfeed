package org.bahmni.module.feedintegration.atomfeed.contract.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPatientIdentifier {
    private String uuid;
    private String identifier;
    private boolean voided;
    private boolean preferred;
    private OpenMRSPatientIdentifierType identifierType;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isVoided() {
        return voided;
    }

    public void setVoided(boolean voided) {
        this.voided = voided;
    }

    public boolean isPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

    public OpenMRSPatientIdentifierType getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(OpenMRSPatientIdentifierType identifierType) {
        this.identifierType = identifierType;
    }

    @Override
    public String toString() {
        return "OpenMRSPatientIdentifier{" +
                "uuid='" + uuid + '\'' +
                ", identifier='" + identifier + '\'' +
                ", voided=" + voided +
                ", preferred=" + preferred +
                ", identifierType=" + identifierType +
                '}';
    }
}
