package sg.gov.moh.iais.egp.bsb.dto.appointment;

import lombok.Data;

import java.io.Serializable;

@Data
public class InspectionDateDto implements Serializable {
    private String appId;
    private String specifyStartDt;
    private String specifyEndDt;
    private String module;
}
