package sg.gov.moh.iais.egp.bsb.dto.appointment;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AppointmentViewDto implements Serializable {
    //display field
    private String id;
    private String appId;
    private String applicationNo;
    private String applicationType;
    private String processType;
    private String applicationStatus;
    private Date inspectionDateAndTime;
    private List<String> inspectors;
}
