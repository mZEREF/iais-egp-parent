package sg.gov.moh.iais.egp.bsb.dto.appointment;

import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tangtang
 * @date 2022/5/6 14:10
 */
@Data
public class BsbAppointmentUserDto implements Serializable {
    @JMap
    private String workGrpName;
    @JMap
    private String loginUserId;
    @JMap
    private String appNo;
    @JMap
    private int workHours;
}
