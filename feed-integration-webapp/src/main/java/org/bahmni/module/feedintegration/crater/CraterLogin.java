package org.bahmni.module.feedintegration.crater;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
@PropertySource("classpath:crater.properties")
public class CraterLogin {

    private String token;

    @Value("${crater.url}")
    String crater_url;

    @Value("${crater.device_name}")
    String device_name;

    @Value("${crater.username}")
    String username;

    @Value("${crater.password}")
    String password;

    @Value("${crater.company}")
    String company;

    @PostConstruct
    public void innit() throws IOException {
        CloseableHttpClient httpclient = null;
        try {
            CloseableHttpResponse response;
            httpclient = HttpClients.createDefault();
            
            List nameValuePairs = new ArrayList();
    	    nameValuePairs.add(new BasicNameValuePair("username", username));
    	    nameValuePairs.add(new BasicNameValuePair("password", password));
    	    nameValuePairs.add(new BasicNameValuePair("device_name", device_name));
     	   
    	    HttpPost httpPost = new HttpPost(crater_url + "/api/v1/auth/login");
    	    URI uri = new URIBuilder(httpPost.getURI())
    	      .setParameters(nameValuePairs)
    	      .build();
    	   ((HttpRequestBase) httpPost).setURI(uri);
    	   httpPost.addHeader("Content-Type", "*/*");
           httpPost.addHeader("Accept", "*/*");
           httpPost.addHeader("company", company);
           response = httpclient.execute(httpPost);
            String authString = EntityUtils.toString(response.getEntity());
            JSONObject authJSON = new JSONObject(authString);
            this.token = authJSON.getString("token");
            response.close();
            httpclient.close();
        }		

        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } 
        catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpclient.close();
        }
    }

    public String getToken() {
        return token;
    }
}
