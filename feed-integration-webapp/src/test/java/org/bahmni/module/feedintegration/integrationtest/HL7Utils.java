package org.bahmni.module.feedintegration.integrationtest;

import ca.uhn.hl7v2.*;
import ca.uhn.hl7v2.model.*;
import ca.uhn.hl7v2.model.v25.message.*;
import ca.uhn.hl7v2.model.v25.segment.*;

import java.text.*;
import java.util.*;

public class HL7Utils {

    public static DateFormat getHl7DateFormat() {
        return new SimpleDateFormat("yyyyMMddHHmmss");
    }

    public static MSH populateMessageHeader(MSH msh, Date dateTime, String messageType, String triggerEvent, String sendingFacility) throws DataTypeException {
        msh.getFieldSeparator().setValue("|");
        msh.getEncodingCharacters().setValue("^~\\&");
        msh.getSendingFacility().getHd1_NamespaceID().setValue(sendingFacility);
        msh.getSendingFacility().getUniversalID().setValue(sendingFacility);
        msh.getSendingFacility().getNamespaceID().setValue(sendingFacility);
        msh.getDateTimeOfMessage().getTs1_Time().setValue(getHl7DateFormat().format(dateTime));
        msh.getMessageType().getMessageCode().setValue(messageType);
        msh.getMessageType().getTriggerEvent().setValue(triggerEvent);
        msh.getProcessingID().getProcessingID().setValue("P");  // stands for production (?)
        msh.getVersionID().getVersionID().setValue("2.5");

        return msh;
    }

    public static ORR_O02 generateORRwithAccept(String messageControlId, String sendingFacility) throws DataTypeException {
        ORR_O02 ack = new ORR_O02();

        populateMessageHeader(ack.getMSH(), new Date(), "ORR", "O02", sendingFacility);

        ack.getMSA().getAcknowledgmentCode().setValue(AcknowledgmentCode.AA.toString());
        ack.getMSA().getMessageControlID().setValue(messageControlId);

        return ack;
    }

    public static ORR_O02 generateORRWithError(String messageControlId, String sendingFacility, String errorMessage) throws DataTypeException {
        ORR_O02 ack = new ORR_O02();
        populateMessageHeader(ack.getMSH(), new Date(), "ORR", "002", sendingFacility);

        ack.getMSA().getAcknowledgmentCode().setValue(AcknowledgmentCode.AE.toString());
        ack.getMSA().getMessageControlID().setValue(messageControlId);
        ack.getMSA().getTextMessage().setValue(errorMessage);

        return ack;
    }

}
