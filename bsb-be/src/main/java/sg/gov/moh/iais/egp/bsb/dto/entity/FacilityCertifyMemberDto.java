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
@JGlobalMap(excluded = {"certifierRegId"})
public class FacilityCertifyMemberDto extends BaseEntityDto {
    private String id;

    @JMap(value = "${certifierReg.id}")
    private String certifierRegId;

    private String name;

    private String idType;

    private String idNumber;

    private Date dob;

    private String sex;

    private String nationality;

    private String telNo;

    private String jobDesignation;

    private String leadCertifier;

    private String areaOfExpertise;

    private String expInCertification;

    private String expInCommission;

    private String expInOthers;

    private String educationBackground;

    private String positionActivity;

    private String relevatnCertificate;

    private String facilityResearches;
}
