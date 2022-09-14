package org.bahmni.module.feedintegration.atomfeed.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("file:${HOME}/atomfeed.properties")
public class ConnectionDetails {

    private static String AUTH_URI;
    private static String OPENMRS_USER;
    private static String OPENMRS_PASSWORD;
    private static int OPENMRS_WEBCLIENT_CONNECT_TIMEOUT;
    private static int OPENMRS_WEBCLIENT_READ_TIMEOUT;

    @Value("${openmrs.auth.uri}")
    public void setAuthUri(String AUTH_URI){
        ConnectionDetails.AUTH_URI = AUTH_URI;
    }

    @Value("${openmrs.user}")
    public void setOpenmrsUser(String OPENMRS_USER){
        ConnectionDetails.OPENMRS_USER = OPENMRS_USER;
    }

    @Value("${openmrs.password}")
    public void setOpenmrsPassword(String OPENMRS_PASSWORD){
        ConnectionDetails.OPENMRS_PASSWORD = OPENMRS_PASSWORD;
    }

    @Value("${openmrs.connectionTimeoutInMilliseconds}")
    public void setOpenmrsWebclientReadTimeout(int OPENMRS_WEBCLIENT_CONNECT_TIMEOUT){
        ConnectionDetails.OPENMRS_WEBCLIENT_CONNECT_TIMEOUT = OPENMRS_WEBCLIENT_CONNECT_TIMEOUT;
    }

    @Value("${openmrs.replyTimeoutInMilliseconds}")
    public void setOpenmrsWebclientConnectTimeout(int OPENMRS_WEBCLIENT_READ_TIMEOUT){
        ConnectionDetails.OPENMRS_WEBCLIENT_READ_TIMEOUT = OPENMRS_WEBCLIENT_READ_TIMEOUT;
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
