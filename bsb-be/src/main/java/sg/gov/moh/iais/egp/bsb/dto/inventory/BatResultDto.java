package sg.gov.moh.iais.egp.bsb.dto.inventory;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YiMing
 * @version 2021/12/23 9:49
 **/

@Data
public class BatResultDto implements Serializable {
    private String dataSubBatId;
    private String facName;
    private String bat;
    //hardCode
    private String toxinQty;
    //hardCode
    private String batPossession;
    private String transactionType;
    private String dataSubNo;

    private TransactionHistoryDto transactionHistoryDto;
}
