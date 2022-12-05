package org.bahmni.module.feedintegration.atomfeed.jobs;

import org.bahmni.module.feedintegration.atomfeed.client.AtomFeedClientFactory;
import org.bahmni.module.feedintegration.atomfeed.worker.PatientFeedWorker;
import org.ict4h.atomfeed.client.service.FeedClient;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@DisallowConcurrentExecution
@Component("openMRSPatientFailedFeedJob")
@ConditionalOnExpression("'${enable.scheduling}'=='true'")
@PropertySource("classpath:atomfeed.properties")
public class PatientFailedFeedJob implements FeedJob {

    @Value("${openmrs.patient.feed.uri}")
    private String PATIENT_FEED_URI ;
    private final Logger logger = LoggerFactory.getLogger(PatientFailedFeedJob.class);
    private FeedClient atomFeedClient;
    private PatientFeedWorker patientFeedWorker;
    private AtomFeedClientFactory atomFeedClientFactory;

    @Autowired
    public PatientFailedFeedJob(PatientFeedWorker patientFeedWorker, AtomFeedClientFactory atomFeedClientFactory) {
        this.patientFeedWorker = patientFeedWorker;
        this.atomFeedClientFactory = atomFeedClientFactory;
    }

    public PatientFailedFeedJob() {
    }

    @Override
    public void process() {
        if(atomFeedClient == null){
            atomFeedClient = atomFeedClientFactory.get(PATIENT_FEED_URI, patientFeedWorker);
        }
        atomFeedClient.processFailedEvents();
    }
}
