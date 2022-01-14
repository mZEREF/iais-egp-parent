package sg.gov.moh.iais.egp.bsb.dto.followup;

import lombok.Data;

import java.io.Serializable;


/**
 * @author YiMing
 * @version 2022/1/10 13:46
 **/
@Data
public class IncidentInfoA implements Serializable{
    private String incidentCause;

    private String explainCause;

    private String measure;

    private String implementEntityDate;
}
