package sg.gov.moh.iais.bsb.dto.inventoryManagement;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/27 9:30
 * DESCRIPTION: TODO
 **/
@Setter
@Getter
public class inventoryManagementQueryDto implements Serializable{
    private static final long serialVersionUID = 1L;

    private String scheduleType;
    private String biologicalAgent;
    private String typeOfTransaction;
    private Date dateOfTransactionFrom;
    private Date dateOfTransactionTo;
    private String facilityName;
    private String sendingFacility;
    private String receivingFacility;
    private String nameOfBiologicalAgent;

}
