package org.bahmni.module.feedintegration.crater;

import org.bahmni.module.feedintegration.atomfeed.client.AtomFeedProperties;

import java.io.InputStream;
import java.util.Properties;

public class CraterProperties {
    private static final String content_type = "con.content_type";
    private static final String accept = "con.accept";
    private static final String setDoOutput = "con.setDoOutput";
    private static final String setDoInput = "con.setDoInput";
    public static final String currencyid = "crater.currencyid";
    public static final String url = "crater.url";
    public static final String company = "crater.company";
    public static final String deviceName = "crater.device_name";
    public static final String username = "crater.username";
    public static final String password = "crater.password";
    public static final String property_file = "/crater.properties";
    private Properties properties;
    private static CraterProperties craterProperties;

    private CraterProperties() {
        InputStream propertyStream = null;
        try {
            propertyStream = this.getClass().getResourceAsStream(property_file);
            properties = new Properties();
            properties.load(propertyStream);

        } catch (Exception e) {
        } finally {
            if (null != propertyStream) {
                try {
                    propertyStream.close();
                    propertyStream = null;
                } catch (Exception e) {
                }
            }

        }
    }

    public static CraterProperties getInstance() {
        if (craterProperties == null) {
            synchronized (AtomFeedProperties.class) {
                if (craterProperties == null) {
                    craterProperties = new CraterProperties();
                }
            }
        }
        return craterProperties;
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public String getContent_type() {
        return getProperty(content_type);
    }

    public String getAccept() {
        return getProperty(accept);
    }

    public String getSetDoOutput() {
        return getProperty(setDoOutput);
    }

    public String getSetDoInput() {
        return getProperty(setDoInput);
    }

    public String getCurrencyid() {
        return getProperty(currencyid);
    }

    public String getUrl() {
        return getProperty(url);
    }

    public String getCompany() {
        return getProperty(company);
    }

    public String getUsername() {
        return getProperty(username);
    }

    public String getPassword() {
        return getProperty(password);
    }

    public String getDeviceName() {
        return getProperty(deviceName);
    }
}