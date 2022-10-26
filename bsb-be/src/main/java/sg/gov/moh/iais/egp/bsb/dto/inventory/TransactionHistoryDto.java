package sg.gov.moh.iais.egp.bsb.dto.inventory;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/

@Data
public class TransactionHistoryDto implements Serializable {

    private String bat;

    private String transferType;

    private BigDecimal transferredQTY;

    private String sendFacility;

    private String recFacility;

    private String transferDate;

    private String exceptedArrivalDate;

    private String providerName;
}
