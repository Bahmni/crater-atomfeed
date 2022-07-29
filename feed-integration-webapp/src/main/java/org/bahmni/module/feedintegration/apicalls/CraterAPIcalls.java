package org.bahmni.module.feedintegration.apicalls;

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bahmni.module.BahmniFeedIntegrationExample;
import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPatientFullRepresentation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.context.annotation.Configuration;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;

import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;


@Configuration
@Component
public class CraterAPIcalls {

    String auth = BahmniFeedIntegrationExample.auth;

    public Properties getprop() throws IOException{
        InputStream input = CraterAPIcalls.class.getClassLoader().getResourceAsStream("application.properties");
        Properties prop = new Properties();
        prop.load(input);
        return prop;
    }

    public String login() throws Exception {
        Properties properties =  this.getprop();
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
        Properties properties =  this.getprop();
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
            Properties properties = this.getprop();
            String name = getname(patientFR);
            String uuid = getuuid(patientFR);

            URL url = new URL(properties.getProperty("crater.url") + "customers");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + auth);
            con.setRequestProperty("Content-Type", properties.getProperty("con.content_type"));
            con.setRequestProperty("Accept", properties.getProperty("con.accept"));
            con.setRequestProperty("company", properties.getProperty("crater.company"));
            con.setDoOutput(Boolean.parseBoolean(properties.getProperty("con.setDoOutput")));
            con.setDoInput(Boolean.parseBoolean(properties.getProperty("con.setDoInput")));

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
            Properties properties = this.getprop();
            String name = getname(patientFR);
            String uuid = getuuid(patientFR);

            String id = this.get_list_customers(uuid);

            URL url = new URL(properties.getProperty("crater.url") + "customers/" + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Authorization", "Bearer " + auth);
            con.setRequestProperty("Content-Type", properties.getProperty("con.content_type"));
            con.setRequestProperty("Accept", properties.getProperty("con.accept"));
            con.setRequestProperty("company", properties.getProperty("crater.company"));
            con.setDoOutput(Boolean.parseBoolean(properties.getProperty("con.setDoOutput")));
            con.setDoInput(Boolean.parseBoolean(properties.getProperty("con.setDoInput")));

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
        Properties properties = this.getprop();
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

    public String del_customer(String auth, OpenMRSPatientFullRepresentation patient) throws JSONException, IOException, URISyntaxException {

        String id = this.get_list_customers("asdasd");

        if (id.equals("Customer not found")) {
            return id;
        }

        URL url = new URL("https://demo.craterapp.comapi/v1/customers/delete");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + auth);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        con.setDoInput(true);

        JSONArray ids = new JSONArray();
        ids.put(id);

        System.out.println(ids);
        JSONObject parameters = new JSONObject();
        parameters.put("ids", id);
        String jsonInputString = parameters.toString();

        System.out.println(jsonInputString);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        con.getInputStream();
        return "Customer deleted";
    }

    public void create_update(OpenMRSPatientFullRepresentation patientFR) throws Exception {
        if (login_verification(auth) == "Authenticated") {
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
        String uuid = "";
        for (int i = 0; i <= patientFR.getIdentifiers().size(); i++) {
            if (patientFR.getIdentifiers().get(i).isPreferred()) {
                uuid = patientFR.getIdentifiers().get(i).getIdentifier();
                break;
            }
        }
        return uuid;
    }

    public String getname(OpenMRSPatientFullRepresentation patientFR) {
        String name;
        if (patientFR.getPerson().getPreferredName().getMiddleName() == null) {
            name = patientFR.getPerson().getPreferredName().getGivenName()
                    + " " + patientFR.getPerson().getPreferredName().getFamilyName();
        } else {
            name = patientFR.getPerson().getPreferredName().getGivenName()
                    + " " + patientFR.getPerson().getPreferredName().getMiddleName()
                    + " " + patientFR.getPerson().getPreferredName().getFamilyName();
        }
        return name;
    }
}


