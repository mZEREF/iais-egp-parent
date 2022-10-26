package sg.gov.moh.iais.egp.bsb.dto.adhocrfi;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.AppAndMiscDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class ViewAdhocRfiDto implements Serializable {
    private String id;

    private String requestNo;
    private String facilityNo;
    private String facilityName;

    private String email;

    private String title;

    private LocalDate dueDate;

    private LocalDate startDate;

    private String status;

    private Boolean informationRequired;

    private Boolean supportingDocRequired;

    private String titleOfInformationRequired;

    private String titleOfSupportingDocRequired;

    private String suppliedInformation;

    private AppAndMiscDto application;

    private List<DocDisplayDto> applicationDocDtos;

    private String dueDateShow;
}
