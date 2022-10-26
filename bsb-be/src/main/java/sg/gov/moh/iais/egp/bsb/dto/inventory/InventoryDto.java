package sg.gov.moh.iais.egp.bsb.dto.inventory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;

import java.io.Serializable;
import java.util.List;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class InventoryDto extends PagingAndSortingDto implements Serializable {

    private String scheduleType;

    private String bioName;

    private String transactionType;

    private String transactionDtFrom;

    private String transactionDtTo;

    private List<String> facilityName;

    private String sendFacility;

    private String recFacility;


    public  void clearAllFields(){
     scheduleType = "";
     bioName= "";
     transactionType = "";
     transactionDtFrom = "";
     transactionDtTo = "";
     facilityName = null;
     sendFacility = "";
     recFacility = "";
    }



}
