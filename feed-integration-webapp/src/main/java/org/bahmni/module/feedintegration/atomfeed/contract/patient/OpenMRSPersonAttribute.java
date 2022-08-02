package org.bahmni.module.feedintegration.atomfeed.contract.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPersonAttribute {
    private String uuid;
    private boolean voided;
    private Object value;
    private OpenMRSPersonAttributeType attributeType;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isVoided() {
        return voided;
    }

    public void setVoided(boolean voided) {
        this.voided = voided;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public OpenMRSPersonAttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(OpenMRSPersonAttributeType attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public String toString() {
        return "OpenMRSPersonAttribute{" +
                "uuid='" + uuid + '\'' +
                ", voided=" + voided +
                ", value=" + value +
                ", attributeType=" + attributeType +
                '}';
    }
}
