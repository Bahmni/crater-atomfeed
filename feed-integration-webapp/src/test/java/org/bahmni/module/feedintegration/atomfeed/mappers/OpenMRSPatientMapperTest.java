package org.bahmni.module.feedintegration.atomfeed.mappers;

import junit.framework.Assert;
import org.bahmni.module.feedintegration.atomfeed.OpenMRSMapperBaseTest;
import org.bahmni.module.feedintegration.atomfeed.contract.patient.OpenMRSPatient;
import org.junit.Test;

public class OpenMRSPatientMapperTest extends OpenMRSMapperBaseTest {

    @Test
    public void testMap() throws Exception {
        String json = deserialize("/samplePatient.json");
        OpenMRSPatient patient = new OpenMRSPatientMapper().map(json);

        Assert.assertEquals("GAN200053", patient.getPatientId());
        Assert.assertEquals(" F l o r a ", patient.getGivenName());
        Assert.assertEquals("S t o n e", patient.getFamilyName());

    }
}