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
public class FacilityCertifierAdminDto extends BaseEntityDto {
    private String id;

    @JMap(value = "${certifierReg.id}")
    private String certifierRegId;

    private String adminName;

    private String adminType;

    private String idType;

    private String idNumber;

    private String designation;

    private String contactNo;

    private String emailAddr;

    private Date employmentStartDt;

    private String nationality;
}
