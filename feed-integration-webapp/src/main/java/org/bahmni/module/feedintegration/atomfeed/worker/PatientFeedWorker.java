package org.bahmni.module.feedintegration.atomfeed.worker;

import org.bahmni.module.feedintegration.apicalls.CraterAPIcalls;
import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPatientFullRepresentation;
import org.bahmni.module.feedintegration.services.OpenMRSService;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientFeedWorker implements EventWorker {
    private static final Logger logger = LoggerFactory.getLogger(EncounterFeedWorker.class);

    @Autowired
    private OpenMRSService openMRSService;

    public PatientFeedWorker() {
    }

    @Override
    public void process(Event event) {
        System.out.println("***************** ");
        System.out.println("Process patient event:" + event.getContent());
        System.out.println("***************** ");
        try {
            logger.info("Getting patient details ...");
            String patientUri = event.getContent();
            OpenMRSPatientFullRepresentation patientFR = openMRSService.getPatientFR(patientUri);
            CraterAPIcalls crater;
            crater = new CraterAPIcalls();
            String auth = crater.login();
            crater.create_update(patientFR);
        } catch (Exception e) {
            logger.error("Failed to fetch patient details", e);
            throw new RuntimeException("Failed to fetch patient details", e);
        }
    }

    @Override
    public void cleanUp(Event event) {

    }
}
