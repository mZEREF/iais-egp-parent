package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap(excluded = {"facilityId"})
public class DataSubmissionDto extends BaseEntityDto {
    private String id;

    @JMap(value = "${facility.id}")
    private String facilityId;

    private List<DataSubmissionBatDto> submissionBats;

    private List<DataSubmissionDocDto> docs;

    private ApplicationDto application;

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

    private String transferCode;
}
