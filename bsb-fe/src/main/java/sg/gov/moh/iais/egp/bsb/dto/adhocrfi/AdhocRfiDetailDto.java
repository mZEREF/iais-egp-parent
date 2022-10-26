package sg.gov.moh.iais.egp.bsb.dto.adhocrfi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdhocRfiDetailDto implements Serializable {
    private String id;
    private String title;
    //rfiAppNo
    private String requestNo;
    private String facilityName;
    private String facilityNo;
    private LocalDate dueDate;
    private String commentsForApplicant;
    private Boolean informationRequired;
    private Boolean supportingDocRequired;
    private String titleOfInformationRequired;
    private String titleOfSupportingDocRequired;
    private String suppliedInformation;
    private String status;
}
