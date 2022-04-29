package sg.gov.moh.iais.egp.bsb.dto.adhocRfi;


import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApplicationDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.entity.Application;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class ViewAdhocRfiDto implements Serializable {
    private String id;

    private String facilityNo;

    private String submissionType;

    private String email;

    private String title;

    private LocalDate dueDate;

    private LocalDate startDate;

    private String approvalNo;

    private String status;

    private Boolean informationRequired;

    private Boolean supportingDocRequired;

    private String titleOfInformationRequired;

    private String titleOfSupportingDocRequired;

    private String suppliedInformation;

    private ApplicationDto application;

    private List<DocRecordInfo> applicationDocDtos;

}
