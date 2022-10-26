package sg.gov.moh.iais.egp.bsb.dto.adhocrfi;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.AppAndMiscDto;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class NewAdhocRfiDto implements Serializable {
    private String id;
    private String requestNo;
    private String facilityNo;
    private String facilityName;
    private String facilityStatus;
    private String title;
    private String commentsForApplicant;
    private String email;
    private LocalDate dueDate;
    private String status;
    private Boolean informationRequired;
    private Boolean supportingDocRequired;
    private String titleOfInformationRequired;
    private String titleOfSupportingDocRequired;

    private String submissionType;
    private LocalDate startDate;
    private String suppliedInformation;
    private AppAndMiscDto application;
    private String requestor;
    private String dueDateShow;
}
