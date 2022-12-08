package org.bahmni.module.feedintegration.crater;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPatientFullRepresentation;
import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPatientIdentifier;
import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPersonAddress;
import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPersonAttribute;
import org.bahmni.module.feedintegration.response.ResponseBIllingAddress;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:crater.properties")
public class CraterAPIClient {

	private static String auth;
	@Autowired
    public CraterAPIClient(CraterLogin craterLogin){
		this.auth = craterLogin.getToken();
	}

	@Value("${con.content_type}")
	String content_type;

	@Value("${con.accept}")
	String accept;

	@Value("${con.setDoOutput}")
	boolean do_Output;

	@Value("${con.setDoInput}")
	boolean do_Input;

	@Value("${crater.currency.id}")
	String currencyId;

	@Value("${crater.url}")
	String crater_url;

	@Value("${crater.company}")
	String company;


	private static final Logger logger = LoggerFactory.getLogger(CraterAPIClient.class);

	public boolean login_verification(String auth) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet request = new HttpGet(crater_url + "/api/v1/auth/check");
		request.addHeader("Content-Type", content_type);
		request.addHeader("Accept", accept);
		request.addHeader("company", company);
		request.addHeader("Authorization", "Bearer " + auth);

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);
			httpClient.close();
			response.close();
			return ((result.charAt(0) == '1') ? true : false);
		}
	}
	
	public  void sendRequestToCrater(HttpRequestBase request) throws Exception {
		CloseableHttpResponse response;
		URI uri = new URIBuilder(request.getURI()).build();
		((HttpRequestBase) request).setURI(uri);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		logger.info("URI :{}",uri);
		request.addHeader("Content-Type", content_type);
		request.addHeader("Accept", accept);
		request.addHeader("company", company);
		request.addHeader("Authorization", "Bearer " + auth);
		response = httpClient.execute(request);
		logger.info("Status Code  : {}", response.getStatusLine().getStatusCode());
		response.close();
		httpClient.close();
	}
	
	
	public JSONObject constructCustomerFromOpenmrsPatient(String name,String uuid,OpenMRSPersonAddress address ,List<OpenMRSPersonAttribute> attribute){
		
		JSONObject parameters = new JSONObject();
		ResponseBIllingAddress billing=new ResponseBIllingAddress();
	    parameters.put("name", name);
	    parameters.put("contact_name", uuid);
		for (OpenMRSPersonAttribute openMRSPersonAttribute : attribute) {
			String display=openMRSPersonAttribute.getAttributeType().getDisplay();
			String value = openMRSPersonAttribute.getValue().toString();
			if(display.equals("phoneNumber")) {
			parameters.put("phone",value);
			billing.setPhone(value);
			}
			else
			parameters.put(display,value);
		}
		billing.setName(name);
		billing.setAddress_street_1(address.getAddress1());
		billing.setAddress_street_2(address.getAddress2());
		billing.setCity(address.getCityVillage());
		billing.setCountry_id(address.getCountry());
		billing.setState(address.getStateProvince());
		billing.setZip(address.getPostalCode().toString());
		parameters.put("billing",new JSONObject(billing));
	    return parameters;
				
	}	
	
	public void create_customer(OpenMRSPatientFullRepresentation patientFR, String auth) throws Exception {
		String name = patientFR.getPerson().getPreferredName().getPreferredFullName();
		String uuid = getuuid(patientFR);
		OpenMRSPersonAddress address = patientFR.getPerson().getPreferredAddress();
		List<OpenMRSPersonAttribute> attribute=patientFR.getPerson().getAttributes();
		HttpPost request = new HttpPost(crater_url + "/api/v1/customers");	
		JSONObject parameters=constructCustomerFromOpenmrsPatient(name,uuid,address,attribute);
		parameters.put("currency_id", currencyId);
		StringEntity params = new StringEntity(parameters.toString());
	    request.setEntity(params);
		sendRequestToCrater(request);
	}

	public void update_customer(OpenMRSPatientFullRepresentation patientFR, String auth) throws Exception {
		String name = patientFR.getPerson().getPreferredName().getPreferredFullName();
		String uuid = getuuid(patientFR);
		OpenMRSPersonAddress address = patientFR.getPerson().getPreferredAddress();
		List<OpenMRSPersonAttribute> attribute=patientFR.getPerson().getAttributes();
		JSONObject parameters=constructCustomerFromOpenmrsPatient(name,uuid,address,attribute);
		parameters.put("currency_id",Integer.valueOf(currencyId));
		String id = this.get_list_customers(uuid, auth);
		HttpPut request = new HttpPut(crater_url + "/api/v1/customers/"+id);
	    StringEntity params = new StringEntity(parameters.toString());
	    request.setEntity(params);
		sendRequestToCrater(request);
	}

	public String get_list_customers(String uuid, String auth) throws URISyntaxException, JSONException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpUriRequest list_customers = RequestBuilder.get()
                .setUri(new URI(crater_url+ "/api/v1/customers"))
                .addParameter("contact_name", uuid)
                .build();

		list_customers.addHeader("Authorization", "Bearer " + auth);
		list_customers.addHeader("Accept", "*/*");
		list_customers.addHeader("Content-Type", "*/*");
		list_customers.addHeader("company", company);

		CloseableHttpResponse response = httpClient.execute(list_customers);
		JSONObject myObject = new JSONObject(EntityUtils.toString(response.getEntity()));
		httpClient.close();
		response.close();

		if (myObject.getJSONArray("data").length() == 0) {
			return "Customer not found";

		} else {
			return String.valueOf(myObject.getJSONArray("data").getJSONObject(0).getInt("id"));
		}
	}

	public void create_update(OpenMRSPatientFullRepresentation patientFR) throws Exception {
		if (login_verification(auth)) {
			String uuid = getuuid(patientFR);
			if (get_list_customers(uuid, auth).equals("Customer not found")) {
				create_customer(patientFR, auth);
			} else {
                update_customer(patientFR, auth);
			}
        }
        else{
			logger.error("Not Authenticated");
		}
	}

	public String getuuid(OpenMRSPatientFullRepresentation patientFR) {
		Optional<OpenMRSPatientIdentifier> uuid1 = patientFR.getIdentifiers().stream()
                .filter(OpenMRSPatientIdentifier::isPreferred)
                .findFirst();

		return uuid1.isPresent() ? uuid1.get().getIdentifier() : EMPTY_STRING;
	}
}
