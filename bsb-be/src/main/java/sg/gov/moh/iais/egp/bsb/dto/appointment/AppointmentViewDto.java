package sg.gov.moh.iais.egp.bsb.dto.appointment;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AppointmentViewDto implements Serializable {

    private String id;

    private String appId;

    private Date inspectionDateAndTime;

    private String facilityName;

    private String facilityClassification;

    private List<String> inspectors;
}
