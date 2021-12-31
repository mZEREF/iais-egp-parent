package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;

import java.io.Serializable;


/**
 * @author YiMing
 * @version 2021/12/28 8:47
 **/
@Data
public class FacilityProfileDto implements Serializable {
    private String facName;
    private String facAddress;
    private String isProtected;
}
