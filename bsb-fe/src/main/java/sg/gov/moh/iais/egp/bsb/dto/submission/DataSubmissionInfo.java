package sg.gov.moh.iais.egp.bsb.dto.submission;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.util.Date;
import java.util.List;

/**
 * @author tangtang
 **/
@Data
public class DataSubmissionInfo {
    private String id;
    private String applicationId;
    private String facilityId;
    private String facilityName;
    private String facilityAddress;
    private String facilityIsProtected;
    private List<DataSubmissionBatInfo> submissionBats;
    private List<DocRecordInfo> docs;
    private String submissionNo;
    private String ddComplete;
    private String type;
    private String facilityReceiving;
    private String receivingCountry;
    private Date exportationDate;
    private String providerName;
    private String flightNo;
    private String procurementMode;
    private String sourceFacilityName;
    private String sourceFacilityAddr;
    private String sourceFacilityContactPerson;
    private String sourceFacilityContactPersonEmail;
    private String sourceFacilityContactPersonTel;
    private String serviceProviderName;
    private Date actualArrivalDate;
    private String actualArrivalTime;
    private Date expectedTransferDate;
    private String receivingFacilityExpectedArrivalTime;
    private String courierServiceProviderName;
    private String ensureStipulatedAB;
    private String ensurePackagingBata;
    private Date receiptDate;
    private String receiptTime;
    private String remarks;
    private String status;
}