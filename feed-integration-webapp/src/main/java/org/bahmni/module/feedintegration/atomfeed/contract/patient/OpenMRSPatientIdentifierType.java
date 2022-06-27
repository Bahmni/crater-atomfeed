package org.bahmni.module.feedintegration.atomfeed.contract.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPatientIdentifierType {
    private String uuid;
    private String display;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return "OpenMRSPatientIdentifierType{" +
                "uuid='" + uuid + '\'' +
                ", display='" + display + '\'' +
                '}';
    }
}
