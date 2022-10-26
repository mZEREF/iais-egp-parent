package sg.gov.moh.iais.egp.bsb.dto.appointment.doreschedule;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ApptAppInfoDto implements Serializable {
    private String appNo;
    private String appStatus;
    private List<String> officers;
    private List<String> userIdList;
    private List<String> apptRefNo;
    private Date inspDate;
    private Date inspEndDate;
}
