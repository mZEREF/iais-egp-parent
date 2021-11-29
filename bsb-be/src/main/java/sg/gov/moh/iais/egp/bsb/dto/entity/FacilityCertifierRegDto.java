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
public class FacilityCertifierRegDto extends BaseEntityDto {
    private String id;

    private List<FacilityCertifierAdminDto> certifierAdmins;

    private List<FacilityCertifierDocDto> certDocs;

    private List<FacilityCertifyMemberDto> certifyMembers;

    private ApplicationDto application;

    private ApprovalDto approval;

    private String orgName;

    private String addressType;

    private String floorNo;

    private String unitNo;

    private String building;

    private String streetName;

    private String address1;

    private String address2;

    private String address3;

    private String postalCode;

    private String yearEstablished;

    private String emailAddr;

    private String contactNo;

    private String contactPerson;

    private String city;

    private String state;

    private String country;

    private String remark;

    private String cloned;

    private String useStatus;
}
