package org.bahmni.module.feedintegration.apicalls;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bahmni.module.CraterAtomfeed;
import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPatientFullRepresentation;

import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPatientIdentifier;
import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPersonName;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.context.annotation.Configuration;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;

import java.util.Optional;
import java.util.Properties;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;
import static java.nio.charset.StandardCharsets.UTF_8;


@Configuration
@Component
public class CraterAPIcalls {

    String auth = CraterAtomfeed.auth;

    public CraterAPIcalls() throws IOException {
    }

    public Properties getprop() throws IOException{
        InputStream input = CraterAPIcalls.class.getClassLoader().getResourceAsStream("application.properties");
        Properties prop = new Properties();
        prop.load(input);
        return prop;
    }

    Properties properties = this.getprop();

    public HttpURLConnection getConnection(String api, String httpMethod) throws IOException {
        URL url = new URL(properties.getProperty("crater.url") + "customers/" + api);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(httpMethod);
        con.setRequestProperty("Authorization", "Bearer " + auth);
        con.setRequestProperty("Content-Type", properties.getProperty("con.content_type"));
        con.setRequestProperty("Accept", properties.getProperty("con.accept"));
        con.setRequestProperty("company", properties.getProperty("crater.company"));
        con.setDoOutput(Boolean.parseBoolean(properties.getProperty("con.setDoOutput")));
        con.setDoInput(Boolean.parseBoolean(properties.getProperty("con.setDoInput")));
        return con;
    }

    public String login() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpUriRequest httppost = RequestBuilder.post()
                .setUri(new URI(properties.getProperty("crater.url") + "auth/login"))
                .addParameter("username", properties.getProperty("crater.username"))
                .addParameter("password", properties.getProperty("crater.password"))
                .addParameter("device_name", "mobile_app")
                .build();

        httppost.addHeader("Content Type", properties.getProperty("con.content_type"));
        httppost.addHeader("Accept", properties.getProperty("con.accept"));
        httppost.addHeader("company", properties.getProperty("crater.company"));

        CloseableHttpResponse response = httpclient.execute(httppost);
        String k = EntityUtils.toString(response.getEntity());
        JSONObject myObject = new JSONObject(k);
        response.close();
        return myObject.getString("token");
    }

    public String login_verification(String auth) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(properties.getProperty("crater.url") + "auth/check");

        request.addHeader("Content-Type", properties.getProperty("con.content_type"));
        request.addHeader("Accept", properties.getProperty("con.accept"));
        request.addHeader("company", properties.getProperty("crater.company"));
        request.addHeader("Authorization", "Bearer " + auth);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            if (result.charAt(0) != '{') {
                return "Authenticated";
            } else {
                return "Not Authenticated";
            }
        }
    }

    public void create_customer(OpenMRSPatientFullRepresentation patientFR) throws Exception {
            String name = getname(patientFR);
            String uuid = getuuid(patientFR);

            HttpURLConnection con = getConnection("customers", "POST");

            JSONObject parameters = new JSONObject();
            parameters.put("name", name);
            parameters.put("contact_name", uuid);
            parameters.put("currency_id", properties.getProperty("crater.currencyid"));
            String jsonInputString = parameters.toString();

            try (OutputStream os = con.getOutputStream()) {
                byte[] inputString = jsonInputString.getBytes(UTF_8);
                os.write(inputString, 0, inputString.length);
            }
            con.getInputStream();
    }

    public void update_customer(OpenMRSPatientFullRepresentation patientFR) throws Exception {
            String name = getname(patientFR);
            String uuid = getuuid(patientFR);

            String id = this.get_list_customers(uuid);
            HttpURLConnection con = getConnection("customers/" + id, "PUT");

            JSONObject parameters = new JSONObject();
            parameters.put("name", name);
            String jsonInputString = parameters.toString();

            try (OutputStream os = con.getOutputStream()) {
                byte[] inputString = jsonInputString.getBytes(UTF_8);
                os.write(inputString, 0, inputString.length);
            }
            con.getInputStream();
    }

    public String get_list_customers(String uuid) throws URISyntaxException, JSONException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpUriRequest list_customers = RequestBuilder.get()
                .setUri(new URI(properties.getProperty("crater.url") + "customers"))
                .addParameter("contact_name", uuid)
                .build();

        list_customers.addHeader("Authorization", "Bearer " + auth);
        list_customers.addHeader("Accept", "*/*");
        list_customers.addHeader("Content-Type", "*/*");
        list_customers.addHeader("company", "1");

        CloseableHttpResponse response = httpclient.execute(list_customers);
        JSONObject myObject = new JSONObject(EntityUtils.toString(response.getEntity()));

        if (myObject.getJSONArray("data").length() == 0) {
            return "Customer not found";

        } else {
            return myObject.getJSONArray("data").getJSONObject(0).getString("id");
        }
    }
    
    public void create_update(OpenMRSPatientFullRepresentation patientFR) throws Exception {
        if (login_verification(auth).equals("Authenticated")) {
            String uuid = getuuid(patientFR);
            if (get_list_customers(uuid).equals("Customer not found")) {
                create_customer(patientFR);
            } else {
                update_customer(patientFR);
            }
        }
        else{
            System.out.println("Not Authenticated");
        }
    }

    public String getuuid(OpenMRSPatientFullRepresentation patientFR) {
        Optional<OpenMRSPatientIdentifier> uuid1 = patientFR.getIdentifiers().stream()
                .filter(OpenMRSPatientIdentifier::isPreferred)
                .findFirst();

        return uuid1.isPresent() ? uuid1.get().getIdentifier() : EMPTY_STRING;
    }

    public String getname(OpenMRSPatientFullRepresentation patientFR) {
        String name;
        OpenMRSPersonName preferredName = patientFR.getPerson().getPreferredName();
        if (preferredName.getMiddleName() == null) {
            name = preferredName.getGivenName()
                    + " " + preferredName.getFamilyName();
        } else {
            name = preferredName.getGivenName()
                    + " " + preferredName.getMiddleName()
                    + " " + preferredName.getFamilyName();
        }
        return name;
    }
}