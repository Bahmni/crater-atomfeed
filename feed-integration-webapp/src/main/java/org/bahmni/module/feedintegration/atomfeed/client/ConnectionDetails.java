package org.bahmni.module.feedintegration.atomfeed.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
@PropertySource("file:${HOME}/atomfeed.properties")
public class ConnectionDetails {
    private static String AUTH_URI;
    private static String OPENMRS_USER;
    private static String OPENMRS_PASSWORD;
    private static int OPENMRS_WEBCLIENT_CONNECT_TIMEOUT;
    private static int OPENMRS_WEBCLIENT_READ_TIMEOUT;


    @Value("${openmrs.auth.uri}")
    private String authURINonstatic;

    @Value("${openmrs.user}")
    private String openmrsUserNonstatic;

    @Value("${openmrs.password}")
    private String openmrsPasswordNonstatic;

    @Value("${openmrs.connectionTimeoutInMilliseconds}")
    private int connectionTimeNonstatic;

    @Value("${openmrs.replyTimeoutInMilliseconds}")
    private int replyTimeNonstatic;

    @Value("${openmrs.auth.uri}")
    public void setAuthUri(String authURINonstatic){
        ConnectionDetails.AUTH_URI = authURINonstatic;
    }

    @Value("${openmrs.user}")
    public void setOpenmrsUser(String openmrsUserNonstatic){
        ConnectionDetails.OPENMRS_USER = openmrsUserNonstatic;
    }

    @Value("${openmrs.password}")
    public void setOpenmrsPassword(String openmrsPasswordNonstatic){
        ConnectionDetails.OPENMRS_PASSWORD = openmrsPasswordNonstatic;
    }

    @Value("${openmrs.connectionTimeoutInMilliseconds}")
    public void setOpenmrsWebclientReadTimeout(int connectionTimeNonstatic){
        ConnectionDetails.OPENMRS_WEBCLIENT_CONNECT_TIMEOUT = connectionTimeNonstatic;
    }

    @Value("${openmrs.replyTimeoutInMilliseconds}")
    public void setOpenmrsWebclientConnectTimeout(int replyTimeNonstatic){
        ConnectionDetails.OPENMRS_WEBCLIENT_READ_TIMEOUT = replyTimeNonstatic;
    }

    public static org.bahmni.webclients.ConnectionDetails get() {
        return new org.bahmni.webclients.ConnectionDetails(
                AUTH_URI,
                OPENMRS_USER,
                OPENMRS_PASSWORD,
                OPENMRS_WEBCLIENT_CONNECT_TIMEOUT,
                OPENMRS_WEBCLIENT_READ_TIMEOUT);
    }
}
