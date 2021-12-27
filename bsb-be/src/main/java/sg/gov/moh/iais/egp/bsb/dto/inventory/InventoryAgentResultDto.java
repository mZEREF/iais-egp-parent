package sg.gov.moh.iais.egp.bsb.dto.inventory;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/

@Data
public class InventoryAgentResultDto{
    private PageInfo pageInfo;

    private List<BatResultDto> bsbAgent;

    public Map<String,BatResultDto> retrieveMap(){
        return this.bsbAgent.stream().collect(Collectors.toMap(BatResultDto::getDataSubBatId, Function.identity()));
    }
}
