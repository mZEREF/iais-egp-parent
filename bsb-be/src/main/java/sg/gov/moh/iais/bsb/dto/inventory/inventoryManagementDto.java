package sg.gov.moh.iais.bsb.dto.inventory;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 18:11
 * DESCRIPTION: TODO
 **/
@Getter
@Setter
public class inventoryManagementDto implements Serializable{
    private static final long serialVersionUID = 1L;

    private String facilityName;
    private String biologicalAgent;
    private BigDecimal quantityOfToxinInPossession;
    private BigDecimal physicalPossessionOfBiologicalAgent;
    private String typeOfTransaction;
    //Search by Transaction Date
    private String nameOfBiologicalAgent;
    private String sendingFacility;
    private String receivingFacility;
    private String transactionType;
    private Date transactionDate;

}
