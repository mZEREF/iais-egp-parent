package sg.gov.moh.iais.egp.bsb.dto.inventory;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YiMing
 * @version 2021/12/23 15:10
 **/

@Data
public class DateResultDto implements Serializable {
    private String dataSubBatId;
    private String batName;
    private String sendFacility;
    private String recFacility;
    private String transactionType;
    private String transactionDt;
    private String transactionStatus;
    private TransactionHistoryDto transactionHistoryDto;
}
