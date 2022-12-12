package org.bahmni.module.feedintegration.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBIllingAddress {
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		private String address_street_1;
		private String address_street_2;
		private String city;
		private String state;
		private String country_id;
		private String zip;
		private String phone;
		public String getAddress_street_1() {
			return address_street_1;
		}
		public void setAddress_street_1(String address_street_1) {
			this.address_street_1 = address_street_1;
		}
		public String getAddress_street_2() {
			return address_street_2;
		}
		public void setAddress_street_2(String address_street_2) {
			this.address_street_2 = address_street_2;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getCountry_id() {
			return country_id;
		}
		public void setCountry_id(String country_id) {
			this.country_id = country_id;
		}
		public String getZip() {
			return zip;
		}
		public void setZip(String zip) {
			this.zip = zip;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		@Override
		public String toString() {
			return "ResponseBIllingAddress [name=" + name + ", address_street_1=" + address_street_1
					+ ", address_street_2=" + address_street_2 + ", city=" + city + ", state=" + state + ", country_id="
					+ country_id + ", zip=" + zip + ", phone=" + phone + "]";
		}
		
}
