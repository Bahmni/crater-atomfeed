package org.bahmni.module.feedintegration.atomfeed.contract.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPerson {
    private String uuid;
    private String display;
    private String gender;
    private int age;
    private Date birthdate;
    private String birthtime;
    private boolean birthdateEstimated;
    private boolean dead;
    private Date deathDate;
    private boolean deathdateEstimated;
    private OpenMRSPersonName preferredName;
    private List<OpenMRSPersonName> names;
    private OpenMRSPersonAddress preferredAddress;
    private List<OpenMRSPersonAddress> addresses;
    private List<OpenMRSPersonAttribute> attributes;


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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getBirthtime() {
        return birthtime;
    }

    public void setBirthtime(String birthtime) {
        this.birthtime = birthtime;
    }

    public boolean isBirthdateEstimated() {
        return birthdateEstimated;
    }

    public void setBirthdateEstimated(boolean birthdateEstimated) {
        this.birthdateEstimated = birthdateEstimated;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Date deathDate) {
        this.deathDate = deathDate;
    }

    public boolean isDeathdateEstimated() {
        return deathdateEstimated;
    }

    public void setDeathdateEstimated(boolean deathdateEstimated) {
        this.deathdateEstimated = deathdateEstimated;
    }

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

    public OpenMRSPersonAddress getPreferredAddress() {
        return preferredAddress;
    }

    public void setPreferredAddress(OpenMRSPersonAddress preferredAddress) {
        this.preferredAddress = preferredAddress;
    }

    public List<OpenMRSPersonAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<OpenMRSPersonAddress> addresses) {
        this.addresses = addresses;
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
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", birthdate=" + birthdate +
                ", birthtime='" + birthtime + '\'' +
                ", birthdateEstimated=" + birthdateEstimated +
                ", dead=" + dead +
                ", deathDate=" + deathDate +
                ", deathdateEstimated=" + deathdateEstimated +
                ", preferredName=" + preferredName +
                ", names=" + names +
                ", preferredAddress=" + preferredAddress +
                ", addresses=" + addresses +
                ", attributes=" + attributes +
                '}';
    }
}
