package org.bahmni.module.feedintegration.crater;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
@Scope("singleton")
public class CraterLogin {

    private final String token;

    public CraterLogin() {
        try {
            CraterProperties properties = CraterProperties.getInstance();
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpUriRequest httppost = RequestBuilder.post()
                    .setUri(new URI(properties.getUrl() + "/api/v1/auth/login"))
                    .addParameter("username", properties.getUsername())
                    .addParameter("password", properties.getPassword())
                    .addParameter("device_name", properties.getDeviceName())
                    .build();

            httppost.addHeader("Content Type", "*/*");
            httppost.addHeader("Accept", "*/*");
            httppost.addHeader("company", "*/*");

            CloseableHttpResponse response = httpclient.execute(httppost);
            String authString = EntityUtils.toString(response.getEntity());
            JSONObject authJSON = new JSONObject(authString);
            this.token = authJSON.getString("token");
            response.close();
        }  catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getToken() {
        return token;
    }
}