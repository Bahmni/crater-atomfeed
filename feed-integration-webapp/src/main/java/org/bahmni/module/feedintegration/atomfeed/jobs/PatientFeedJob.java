package org.bahmni.module.feedintegration.atomfeed.jobs;

import org.bahmni.module.feedintegration.atomfeed.client.AtomFeedClientFactory;
import org.bahmni.module.feedintegration.atomfeed.worker.PatientFeedWorker;
import org.ict4h.atomfeed.client.service.FeedClient;
import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@DisallowConcurrentExecution
@Component("openMRSPatientFeedJob")
@ConditionalOnExpression("'${enable.scheduling}'=='true'")
public class PatientFeedJob implements FeedJob {
    private static final String OPENMRS_PATIENT_FEED_NAME = "openmrs.patient.feed.uri";
    private final Logger logger = LoggerFactory.getLogger(PatientFeedJob.class);
    private FeedClient atomFeedClient;
    private PatientFeedWorker patientFeedWorker;
    private AtomFeedClientFactory atomFeedClientFactory;

    @Autowired
    public PatientFeedJob(PatientFeedWorker patientFeedWorker, AtomFeedClientFactory atomFeedClientFactory) {
        this.patientFeedWorker = patientFeedWorker;
        this.atomFeedClientFactory = atomFeedClientFactory;
    }

    public PatientFeedJob() {
    }

    @Override
    public void process() throws InterruptedException {
        if(atomFeedClient == null){
            atomFeedClient = atomFeedClientFactory.get(OPENMRS_PATIENT_FEED_NAME, patientFeedWorker);
        }
        logger.info("Processing feed...");
        atomFeedClient.processEvents();
        logger.info("Completed processing feed...");
    }
}
