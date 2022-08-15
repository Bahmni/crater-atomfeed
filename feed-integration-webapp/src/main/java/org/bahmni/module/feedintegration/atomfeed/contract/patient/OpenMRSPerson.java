package org.bahmni.module.feedintegration.atomfeed.contract.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPerson {
    private String uuid;
    private String display;

    private OpenMRSPersonName preferredName;
    private List<OpenMRSPersonName> names;

    private List<OpenMRSPersonAttribute> attributes;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplay() {return display;}

    public void setDisplay(String display) {this.display = display;}

    public OpenMRSPersonName getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(OpenMRSPersonName preferredName) {
        this.preferredName = preferredName;
    }

    public List<OpenMRSPersonName> getNames() {
        return names;
    }

    public void setNames(List<OpenMRSPersonName> names) {
        this.names = names;
    }

    public List<OpenMRSPersonAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<OpenMRSPersonAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "OpenMRSPerson{" +
                "uuid='" + uuid + '\'' +
                ", display='" + display + '\'' +
                ", preferredName=" + preferredName +
                ", names=" + names +
                ", attributes=" + attributes +
                '}';
    }
}
