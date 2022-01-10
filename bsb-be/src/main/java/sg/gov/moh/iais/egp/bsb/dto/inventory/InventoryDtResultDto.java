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
public class InventoryDtResultDto{
    private PageInfo pageInfo;

    private List<DateResultDto> bsbDt;


    public Map<String,DateResultDto>  retrieveMap(){
        return this.bsbDt.stream().collect(Collectors.toMap(DateResultDto::getDataSubBatId, Function.identity()));
    }
}
