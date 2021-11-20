package sg.gov.moh.iais.egp.bsb.dto.inventory;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/
@Data
public class TransactionHistoryDto implements Serializable {
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
