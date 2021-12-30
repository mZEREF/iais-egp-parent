package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;


/**
 * @author YiMing
 * @version 2021/12/28 8:47
 **/
@Data
public class FacilityProfileDto {
    private String facName;
    private String facAddress;
    private String isProtected;
}
