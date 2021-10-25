package sg.gov.moh.iais.egp.bsb.dto.inventory;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 18:27
 * DESCRIPTION: TODO
 **/


@Data
public class AdjustmentHistoryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String facilityName;

    private BigDecimal typeOfTransferBefore;

    private BigDecimal typeOfTransferAfter;

    private String adjustmentType;

    private Date adjustmentDateTime;

    private String adjustedBy;

    private BigDecimal quantityBefore;

    private BigDecimal quantityAfter;


}
