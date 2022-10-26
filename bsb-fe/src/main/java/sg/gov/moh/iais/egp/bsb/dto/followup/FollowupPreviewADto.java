package sg.gov.moh.iais.egp.bsb.dto.followup;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author YiMing
 * @version 2022/1/10 13:44
 **/
@Data
public class FollowupPreviewADto implements Serializable {
    private String incidentId;
    private String incidentInvestId;
    private String referenceNo;
    private List<IncidentInfoA> incidentInfoAList;
}
