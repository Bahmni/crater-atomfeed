package org.bahmni.module.feedintegration.atomfeed.mappers;

import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPatient;
import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPatientFullRepresentation;
import org.bahmni.webclients.ObjectMapperRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.ParseException;

public class OpenMRSPatientMapper {
    private ObjectMapper objectMapper;

    public OpenMRSPatientMapper() {
        this.objectMapper = ObjectMapperRepository.objectMapper;
    }

    public OpenMRSPatient map(String patientJSON) throws IOException, ParseException {
        OpenMRSPatient patient = new OpenMRSPatient();
        JsonNode jsonNode = objectMapper.readTree(patientJSON);

        patient.setPatientId(jsonNode.path("identifiers").get(0).path("identifier").asText());
        patient.setGivenName(jsonNode.path("person").path("preferredName").path("givenName").asText().replaceAll("[\\W&&[^-]]", " "));
        patient.setFamilyName(jsonNode.path("person").path("preferredName").path("familyName").asText().replaceAll("[\\W&&[^-]]", " "));
        patient.setMiddleName(jsonNode.path("person").path("preferredName").path("middleName").asText().replaceAll("[\\W&&[^-]]", " "));

        return patient;
    }

    public OpenMRSPatientFullRepresentation mapFullRepresentation(String patientJSON) throws IOException {
        return objectMapper.readValue(patientJSON, OpenMRSPatientFullRepresentation.class);
    }
}
