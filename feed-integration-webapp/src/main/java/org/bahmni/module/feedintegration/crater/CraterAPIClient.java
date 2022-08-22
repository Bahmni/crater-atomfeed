package org.bahmni.module.feedintegration.crater;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPatientFullRepresentation;
import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPatientIdentifier;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;
import static java.nio.charset.StandardCharsets.UTF_8;


@Component
public class CraterAPIClient {

    private final CraterProperties properties = CraterProperties.getInstance();

    private static String auth;
    @Autowired
    public CraterAPIClient(CraterLogin craterLogin){
        this.auth = craterLogin.getToken();
    }

    private static final Logger logger = LoggerFactory.getLogger(CraterAPIClient.class);

    public HttpURLConnection getConnection(String api, String httpMethod, String auth) throws IOException {
        URL url = new URL(properties.getUrl() + "/api/v1/" + api);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(httpMethod);
        con.setRequestProperty("Authorization", "Bearer " + auth);
        con.setRequestProperty("Content-Type", properties.getContent_type());
        con.setRequestProperty("Accept", properties.getAccept());
        con.setRequestProperty("company", properties.getCompany());
        con.setDoOutput(Boolean.parseBoolean(properties.getSetDoOutput()));
        con.setDoInput(Boolean.parseBoolean(properties.getSetDoInput()));
        return con;
    }

    public boolean login_verification(String auth) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(properties.getUrl() + "/api/v1/auth/check");

        request.addHeader("Content-Type", properties.getContent_type());
        request.addHeader("Accept", properties.getAccept());
        request.addHeader("company", properties.getCompany());
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

            HttpURLConnection con = getConnection("customers", "POST", auth);

            JSONObject parameters = new JSONObject();
            parameters.put("name", name);
            parameters.put("contact_name", uuid);
            parameters.put("currency_id", properties.getCurrencyid());
            String jsonInputString = parameters.toString();

            try (OutputStream os = con.getOutputStream()) {
                byte[] inputString = jsonInputString.getBytes(UTF_8);
                os.write(inputString, 0, inputString.length);
            }
            con.getInputStream();
    }

    public void update_customer(OpenMRSPatientFullRepresentation patientFR, String auth) throws Exception {
            String name = patientFR.getPerson().getPreferredName().getPreferredFullName();
            String uuid = getuuid(patientFR);

            String id = this.get_list_customers(uuid, auth);
            HttpURLConnection con = getConnection("customers/" + id, "PUT", auth);

            JSONObject parameters = new JSONObject();
            parameters.put("name", name);
            String jsonInputString = parameters.toString();

            try (OutputStream os = con.getOutputStream()) {
                byte[] inputString = jsonInputString.getBytes(UTF_8);
                os.write(inputString, 0, inputString.length);
            }
            con.getInputStream();
    }

    public String get_list_customers(String uuid, String auth) throws URISyntaxException, JSONException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpUriRequest list_customers = RequestBuilder.get()
                .setUri(new URI(properties.getUrl()+ "/api/v1/customers"))
                .addParameter("contact_name", uuid)
                .build();

        list_customers.addHeader("Authorization", "Bearer " + auth);
        list_customers.addHeader("Accept", "*/*");
        list_customers.addHeader("Content-Type", "*/*");
        list_customers.addHeader("company", "1");

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