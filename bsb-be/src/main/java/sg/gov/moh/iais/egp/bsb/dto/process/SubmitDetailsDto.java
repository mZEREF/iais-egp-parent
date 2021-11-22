package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.entity.ApplicationMisc;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.FacilityActivity;
import sg.gov.moh.iais.egp.bsb.entity.RoutingHistory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/10/14
 */
@Data
public class SubmitDetailsDto implements Serializable {

    private String taskId;

    private String applicationNo;

    private String applicationType;

    private String processType;

    private String facilityType;

    private String facilityName;

    private String facilityAddress;

    private Date applicationDt;

    private String applicationStatus;

    private FacilityActivity facilityActivity;

    private List<Biological> biologicals;

    /*private List<RoutingHistory> routingHistories;*/

    /**
     * This is used to display the last misc of the previous person
     */
    private ApplicationMisc applicationMisc;
}
