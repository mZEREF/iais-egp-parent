package sg.gov.moh.iais.egp.bsb.dto.approvalapplication;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author Li Ran
 * @Date 2021/7/30 17:09
 **/
@Getter
@Setter
public class BsbFacilityQueryDto implements Serializable {

    private String id;

    private String facilityName;

}
