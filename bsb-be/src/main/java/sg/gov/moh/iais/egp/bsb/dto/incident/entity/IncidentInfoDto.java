package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YiMing
 * @version 2021/12/27 17:52
 **/
@Data
public class IncidentInfoDto implements Serializable {
    private String referenceNo;
    private String incidentReporting;
    private String incidentType;
}
