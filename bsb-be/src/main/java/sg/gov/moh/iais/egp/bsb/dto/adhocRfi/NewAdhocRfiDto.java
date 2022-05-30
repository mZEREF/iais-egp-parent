package sg.gov.moh.iais.egp.bsb.dto.adhocrfi;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApplicationDto;

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

    private String approvalNo;

    private String status;

    private Boolean informationRequired;

    private Boolean supportingDocRequired;

    private String titleOfInformationRequired;

    private String titleOfSupportingDocRequired;

    private String suppliedInformation;

    private ApplicationDto application;

    private String dueDateShow;
}
