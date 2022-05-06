package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap(excluded = {"facilityId"})
public class FacilityBiosafetyCommitteeDto extends BaseEntityDto {
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

    private String emailAddr;

    private Date employmentStartDate;

    private String workArea;

    private String role;

    private String employeeOfComp;

    private String externalCompName;
}
