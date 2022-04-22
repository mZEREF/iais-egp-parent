package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class NewAdhocRfiDto implements Serializable {
    private String id;

    private String facilityNo;

    private String submissionType;

    private String email;

    private String title;

    private LocalDate dueDate;

    private LocalDate startDate;

    private String approveNo;

    private String status;

    private Boolean informationRequired;

    private Boolean supportingDocRequired;

    private String titleOfInformationRequired;

    private String titleOfSupportingDocRequired;

    private String suppliedInformation;

    private ApplicationDto applicationDto;

    private AuditTrailDto auditTrailDto;

}
