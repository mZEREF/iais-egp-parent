package sg.gov.moh.iais.egp.bsb.dto.inventory;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.DataSubmissionBat;
import java.util.List;
import java.util.Map;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/
@Data
public class InventoryAgentResultDto {

    private PageInfo pageInfo;

    private List<DataSubmissionBat> bsbAgent;

    private Map<String, Biological> bioIdMap;

}
