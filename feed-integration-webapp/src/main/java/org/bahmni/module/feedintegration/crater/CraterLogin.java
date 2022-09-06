package org.bahmni.module.feedintegration.crater;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
@Scope("singleton")
@PropertySource("/crater.properties")
public class CraterLogin {

    private String token;

    @Value("${crater.url}")
    String url;
    @Value("${crater.device_name}")
    String device_name;

    @Value("${crater.username}")
    String username;

    @Value("${crater.password}")
    String password;

    public CraterLogin() {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpUriRequest httppost = RequestBuilder.post()
                    .setUri(new URI(url + "/api/v1/auth/login"))
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .addParameter("device_name", device_name)
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
