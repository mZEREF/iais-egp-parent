package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap(excluded = {"facilityId"})
public class FacilityAuthoriserDto extends BaseEntityDto {
    private String id;

    @JMap(value = "${facility.id}")
    private String facilityId;

    private String salutation;

    private String name;

    private String idType;

    private String idNumber;

    private String nationality;

    private String designation;

    private String contactNo;

    private String email;

    private LocalDate employmentStartDate;

    private String workArea;

    private LocalDate securityClearanceDate;

    private String employeeOfComp;

    private String externalCompName;
}
