package sg.gov.moh.iais.egp.bsb.dto.appointment;

import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author tangtang
 * @date 2022/5/6 14:09
 */
@Data
public class BsbAppointmentDto implements Serializable {
    @JMap
    private List<BsbAppointmentUserDto> users;
    @JMap
    private List<String> appNoList;
    @JMap
    private String sysClientKey;
    @JMap
    private String startDate;
    @JMap
    private String endDate;
    @JMap
    private Date submitDt;
    @JMap
    private int resultNum;
    @JMap
    private String specificApptRefNo;
    @JMap
    private HashMap<String, Date> userSpecMap;
    @JMap
    private boolean confirmAtOnce;
}
