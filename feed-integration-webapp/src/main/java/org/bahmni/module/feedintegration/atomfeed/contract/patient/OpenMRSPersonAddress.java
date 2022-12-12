package org.bahmni.module.feedintegration.atomfeed.contract.patient;

import java.io.IOException;

import org.bahmni.webclients.ObjectMapperRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSPersonAddress {
	private String uuid;
	private String address1;
	private String address2;
	private String address3;
	private String cityVillage;
	private String countyDistrict;
	private String stateProvince;
	private String country;
	private Integer postalCode;
	public Integer getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
	}

	private final Logger logger = LoggerFactory.getLogger(OpenMRSPersonAddress.class);

	public OpenMRSPersonAddress() {
	}

	public OpenMRSPersonAddress(String address1, String address2, String address3, String cityVillage,
			String countyDistrict, String stateProvince, String country) {
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.cityVillage = cityVillage;
		this.countyDistrict = countyDistrict;
		this.stateProvince = stateProvince;
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getCityVillage() {
		return cityVillage;
	}

	public void setCityVillage(String cityVillage) {
		this.cityVillage = cityVillage;
	}

	public String getCountyDistrict() {
		return countyDistrict;
	}

	public void setCountyDistrict(String countyDistrict) {
		this.countyDistrict = countyDistrict;
	}

	public String getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	

	@Override
	public String toString() {
		return "OpenMRSPersonAddress [uuid=" + uuid + ", address1=" + address1 + ", address2=" + address2
				+ ", address3=" + address3 + ", cityVillage=" + cityVillage + ", countyDistrict=" + countyDistrict
				+ ", stateProvince=" + stateProvince + ", country=" + country + ", postalCode=" + postalCode + "]";
	}

	public String toJsonString() {
		try {
			return ObjectMapperRepository.objectMapper.writeValueAsString(this);
		} catch (IOException e) {
			logger.error("Unable to convert personAddress hash to json string. {}", e.getMessage());
		}
		return null;
	}

}