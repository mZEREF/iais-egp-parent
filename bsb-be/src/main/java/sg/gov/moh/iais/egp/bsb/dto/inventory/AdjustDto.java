package sg.gov.moh.iais.egp.bsb.dto.inventory;

import lombok.Data;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/
@Data
public class AdjustDto {
    private String adjustType;

    private String facilityName;

    private String transferType;

    private String initQuantity;

    private String changeQuantity;

    private String remarks;
}
