package sg.gov.moh.iais.bsb.dto.inventoryManagement;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 18:17
 * DESCRIPTION: TODO
 **/

@Getter
@Setter
public class transactionHistoryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String biologicalAgent;
    private String typeOfTransfer;
    private BigDecimal quantityTransferred;
    private String sendingFacility;
    private String receivingFacility;
    private Date dateOfTransfer;
    private Date expectedArrivalTimeAtReceivingFacility;
    private String nameOfCourierOfServiceProvider;
}
