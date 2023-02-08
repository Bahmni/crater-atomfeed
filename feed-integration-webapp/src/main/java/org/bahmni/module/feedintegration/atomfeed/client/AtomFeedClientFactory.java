package org.bahmni.module.feedintegration.atomfeed.client;

import org.bahmni.webclients.ClientCookies;
import org.bahmni.webclients.HttpClient;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.jdbc.AllFailedEventsJdbcImpl;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.ict4h.atomfeed.client.service.FeedClient;
import org.ict4h.atomfeed.jdbc.AtomFeedJdbcTransactionManager;
import org.ict4h.atomfeed.server.transaction.AtomFeedSpringTransactionSupport;
import org.openmrs.module.atomfeed.transaction.support.AtomFeedSpringTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Component
@PropertySource("classpath:atomfeed.properties")
public class AtomFeedClientFactory {

    @Autowired
    private AtomFeedSpringTransactionSupport atomFeedSpringTransactionSupport;

    @Value("${feed.connectionTimeoutInMilliseconds}")
    private String FEED_CONNECT_TIMEOUT;

    @Value("${feed.replyTimeoutInMilliseconds}")
    private String FEED_REPLY_TIMEOUT;

    @Value("${feed.maxFailedEvents}")
    private String FEED_MAX_FAILED_EVENTS;

    @Value("${feed.failedEventMaxRetry}")
    private String FAILED_EVENT_MAX_RETRY;

    public FeedClient get(String feedName, EventWorker encounterFeedWorker) {
        HttpClient authenticatedWebClient = WebClientFactory.getClient();
        org.bahmni.webclients.ConnectionDetails connectionDetails = ConnectionDetails.get();
        String authUri = connectionDetails.getAuthUrl();
        ClientCookies cookies = getCookies(authenticatedWebClient, authUri);
        return getFeedClient(AtomFeedProperties.getInstance(),
                feedName, encounterFeedWorker, cookies);
    }

    private FeedClient getFeedClient(AtomFeedProperties atomFeedProperties, String feedName,
                                     EventWorker eventWorker, ClientCookies cookies) {
        try {
            org.ict4h.atomfeed.client.AtomFeedProperties atomFeedClientProperties = createAtomFeedClientProperties(atomFeedProperties);

            AllFeeds allFeeds = new AllFeeds(atomFeedClientProperties, cookies);
            AllMarkersJdbcImpl allMarkers = new AllMarkersJdbcImpl(atomFeedSpringTransactionSupport);
            AllFailedEventsJdbcImpl allFailedEvents = new AllFailedEventsJdbcImpl(atomFeedSpringTransactionSupport);

            return new AtomFeedClient(allFeeds, allMarkers, allFailedEvents,
                    atomFeedClientProperties, atomFeedSpringTransactionSupport, new URI(feedName), eventWorker);

        } catch (URISyntaxException e) {
            throw new RuntimeException(String.format("Is not a valid URI - %s", feedName));
        }
    }

    private org.ict4h.atomfeed.client.AtomFeedProperties createAtomFeedClientProperties(AtomFeedProperties atomFeedProperties) {
        org.ict4h.atomfeed.client.AtomFeedProperties feedProperties = new org.ict4h.atomfeed.client.AtomFeedProperties();
        feedProperties.setConnectTimeout(Integer.parseInt(FEED_CONNECT_TIMEOUT));
        feedProperties.setReadTimeout(Integer.parseInt(FEED_REPLY_TIMEOUT));
        feedProperties.setMaxFailedEvents(Integer.parseInt(FEED_MAX_FAILED_EVENTS));
        feedProperties.setFailedEventMaxRetry(Integer.parseInt(FAILED_EVENT_MAX_RETRY));
        feedProperties.setControlsEventProcessing(true);
        return feedProperties;
    }

    private ClientCookies getCookies(HttpClient authenticatedWebClient, String urlString) {
        try {
            return authenticatedWebClient.getCookies(new URI(urlString));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Is not a valid URI - " + urlString);
        }
    }
}
