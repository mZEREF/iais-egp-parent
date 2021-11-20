package sg.gov.moh.iais.egp.bsb.dto.inventory;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/

@Data
public class InventoryDto extends PagingAndSortingDto implements Serializable {

    private String scheduleType;

    private String biologicalAgent;

    private String transactionType;

    private Date transactionDtFrom;

    private Date transactionDtTo;

    private List<String> facilityName;

    private String sendFacility;

    private String recFacility;


    public  void clearAllFields(){
     scheduleType = "";
     biologicalAgent= "";
     transactionType = "";
     transactionDtFrom = null;
     transactionDtTo = null;
     facilityName = null;
     sendFacility = "";
     recFacility = "";
    }

}
