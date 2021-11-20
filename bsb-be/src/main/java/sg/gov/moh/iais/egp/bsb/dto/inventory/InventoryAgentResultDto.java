package sg.gov.moh.iais.egp.bsb.dto.inventory;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.FacilityBiologicalAgent;
import java.util.List;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/
@Data
public class InventoryAgentResultDto {

    private PageInfo pageInfo;


    private List<FacilityBiologicalAgent> bsbAgent;

}
