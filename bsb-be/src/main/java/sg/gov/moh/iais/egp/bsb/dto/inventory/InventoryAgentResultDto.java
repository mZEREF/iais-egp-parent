package sg.gov.moh.iais.egp.bsb.dto.inventory;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.FacilityBiologicalAgent;
import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/8/31 14:21
 * DESCRIPTION: TODO
 **/
@Data
public class InventoryAgentResultDto {

    private PageInfo pageInfo;


    private List<FacilityBiologicalAgent> bsbAgent;

}
