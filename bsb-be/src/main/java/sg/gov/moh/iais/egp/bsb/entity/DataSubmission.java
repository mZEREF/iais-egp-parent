package sg.gov.moh.iais.egp.bsb.entity;
import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class DataSubmission extends BaseEntity {
	
	private String id;
	
	private Facility facility;

	private List<DataSubmissionBat> submissionBats;

	private List<DataSubmissionDoc> docs;

	private Application application;

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

	@Temporal(TemporalType.TIMESTAMP)
	private Date actualArrivalDate;

	private String actualArrivalTime;

	@Temporal(TemporalType.TIMESTAMP)
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

	private String amendReason;

	private String amendReasonOther;

}
