package org.bahmni.module.feedintegration.atomfeed.contract.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPatientFullRepresentation {
    private String uuid;
    private List<OpenMRSPatientIdentifier> identifiers;
    private OpenMRSPerson person;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<OpenMRSPatientIdentifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<OpenMRSPatientIdentifier> identifiers) {
        this.identifiers = identifiers;
    }

    public OpenMRSPerson getPerson() {
        return person;
    }

    public void setPerson(OpenMRSPerson person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "OpenMRSPatientFullRepresentation{" +
                "uuid='" + uuid + '\'' +
                ", identifiers=" + identifiers +
                ", person=" + person +
                '}';
    }
}
