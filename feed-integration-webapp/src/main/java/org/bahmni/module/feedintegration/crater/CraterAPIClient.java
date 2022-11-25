package org.bahmni.module.feedintegration.crater;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPatientFullRepresentation;
import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPatientIdentifier;
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

	public void create_customer(OpenMRSPatientFullRepresentation patientFR, String auth) throws Exception {
		String name = patientFR.getPerson().getPreferredName().getPreferredFullName();
		String uuid = getuuid(patientFR);
		CloseableHttpResponse response;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost request = new HttpPost(crater_url + "/api/v1/customers");	
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("name", name));
		nameValuePairs.add(new BasicNameValuePair("contact_name", uuid));
		nameValuePairs.add(new BasicNameValuePair("currency_id", currencyId));
		URI uri = new URIBuilder(request.getURI()).setParameters(nameValuePairs).build();
		((HttpRequestBase) request).setURI(uri);
		logger.info("URI :{}",uri);
		request.addHeader("Content-Type", content_type);
		request.addHeader("Accept", accept);
		request.addHeader("company", company);
		request.addHeader("Authorization", "Bearer " + auth);
		response = httpClient.execute(request);
		logger.debug("Status Code  : {}", response.getStatusLine().getStatusCode());
		response.close();
		httpClient.close();

	}

	public void update_customer(OpenMRSPatientFullRepresentation patientFR, String auth) throws Exception {
		String name = patientFR.getPerson().getPreferredName().getPreferredFullName();
		String uuid = getuuid(patientFR);

		String id = this.get_list_customers(uuid, auth);
		
		CloseableHttpResponse response;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPut request = new HttpPut(crater_url + "/api/v1/customers/"+id);	
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("name", name));
		nameValuePairs.add(new BasicNameValuePair("currency_id", currencyId));
		URI uri = new URIBuilder(request.getURI()).setParameters(nameValuePairs).build();
		((HttpRequestBase) request).setURI(uri);
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

        return uuid1.isPresent() ? uuid1.get().getIdentifier() : "";
	}
}