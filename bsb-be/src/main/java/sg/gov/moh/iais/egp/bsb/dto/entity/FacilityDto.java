package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap
public class FacilityDto extends BaseEntityDto {
    private String id;

    private List<FacilityDocDto> docs;

    private FacilityOperatorDto operator;

    private FacilityOfficerDto officer;

    private List<FacilityAdminDto> admins;

    private List<FacilityActivityDto> facilityActivities;

    private List<FacilityAuthoriserDto> authorizers;

    private List<FacilityBiosafetyCommitteeDto> bioCommittees;

    private ApplicationDto application;

    private String facilityName;

    private String facilityClassification;

    private String postalCode;

    private String blkNo;

    private String floorNo;

    private String unitNo;

    private String streetName;

    private String isProtected;
}
