package sg.gov.moh.iais.egp.bsb.dto.inventory;

import lombok.Data;

/**
 * AUTHOR: YiMing
 * DATE:2021/8/31 13:48
 * DESCRIPTION: TODO
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
