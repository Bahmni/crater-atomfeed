package org.bahmni.module.feedintegration.atomfeed.client;

import org.bahmni.webclients.ClientCookies;
import org.bahmni.webclients.HttpClient;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.jdbc.AllFailedEventsJdbcImpl;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.ict4h.atomfeed.client.service.FeedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class AtomFeedClientFactory {

    @Autowired
    private AtomFeedHibernateTransactionManager transactionManager;

    public FeedClient get(String feedName, EventWorker encounterFeedWorker) {
        HttpClient authenticatedWebClient = WebClientFactory.getClient();
        org.bahmni.webclients.ConnectionDetails connectionDetails = ConnectionDetails.get();
        String authUri = connectionDetails.getAuthUrl();
//        System.out.println(authUri);
        ClientCookies cookies = getCookies(authenticatedWebClient, authUri);
//        System.out.println("ykvjkno");
        return getFeedClient(AtomFeedProperties.getInstance(),
                feedName, encounterFeedWorker, cookies);
    }

    private FeedClient getFeedClient(AtomFeedProperties atomFeedProperties, String feedName,
                                        EventWorker eventWorker, ClientCookies cookies) {
        String uri = atomFeedProperties.getProperty(feedName);
        try {
            org.ict4h.atomfeed.client.AtomFeedProperties atomFeedClientProperties = createAtomFeedClientProperties(atomFeedProperties);
            
            AllFeeds allFeeds = new AllFeeds(atomFeedClientProperties, cookies);
            AllMarkersJdbcImpl allMarkers = new AllMarkersJdbcImpl(transactionManager);
            AllFailedEventsJdbcImpl allFailedEvents = new AllFailedEventsJdbcImpl(transactionManager);

            return new AtomFeedClient(allFeeds, allMarkers, allFailedEvents,
                    atomFeedClientProperties, transactionManager, new URI(uri), eventWorker);
            
        } catch (URISyntaxException e) {
            throw new RuntimeException(String.format("Is not a valid URI - %s", uri));
        }
    }

    private org.ict4h.atomfeed.client.AtomFeedProperties createAtomFeedClientProperties(AtomFeedProperties atomFeedProperties) {
        org.ict4h.atomfeed.client.AtomFeedProperties feedProperties = new org.ict4h.atomfeed.client.AtomFeedProperties();
        feedProperties.setConnectTimeout(Integer.parseInt(atomFeedProperties.getFeedConnectionTimeout()));
        feedProperties.setReadTimeout(Integer.parseInt(atomFeedProperties.getFeedReplyTimeout()));
        feedProperties.setMaxFailedEvents(Integer.parseInt(atomFeedProperties.getMaxFailedEvents()));
        feedProperties.setFailedEventMaxRetry(Integer.parseInt(atomFeedProperties.getFailedEventMaxRetry()));
        feedProperties.setControlsEventProcessing(true);
        return feedProperties;
    }

    private ClientCookies getCookies(HttpClient authenticatedWebClient, String urlString) {
        try {
            ClientCookies k = authenticatedWebClient.getCookies(new URI(urlString));
            System.out.println(k.toString());
            return authenticatedWebClient.getCookies(new URI(urlString));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Is not a valid URI - " + urlString);
        }
    }
}
