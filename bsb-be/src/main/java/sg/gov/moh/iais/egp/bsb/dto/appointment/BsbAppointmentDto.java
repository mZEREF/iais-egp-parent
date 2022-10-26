package sg.gov.moh.iais.egp.bsb.dto.appointment;

import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author tangtang
 */
@Data
public class BsbAppointmentDto implements Serializable {
    @JMap
    private List<BsbAppointmentUserDto> users;
    @JMap
    private List<String> appNoList;
    @JMap
    private String sysClientKey;
    @JMap("startDate")
    private String startDt;
    @JMap("endDate")
    private String endDt;
    @JMap("submitDt")
    private Date submitDate;
    @JMap
    private int resultNum;
    @JMap
    private String specificApptRefNo;
    @JMap
    private HashMap<String, Date> userSpecMap;
    @JMap
    private boolean confirmAtOnce;
}
