package sg.gov.moh.iais.egp.bsb.dto.inspection;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ChecklistInfoDto implements Serializable {
    private Date inspectionDate;
    private String inspectorLead;
    private String inspector;
    private String ncNum;
}
