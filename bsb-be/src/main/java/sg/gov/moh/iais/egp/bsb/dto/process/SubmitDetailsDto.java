package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.entity.Biological;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/10/14
 */
@Data
public class SubmitDetailsDto implements Serializable {

    private String applicationNo;

    private String applicationType;

    private String processType;

    private String facilityType;

    private String facilityName;

    private String postalCode;

    private String blkNo;

    private String floorNo;

    private String unitNo;

    private String streetName;

    private Date applicationDt;

    private String applicationStatus;

    private List<Biological> biologicals;

}
