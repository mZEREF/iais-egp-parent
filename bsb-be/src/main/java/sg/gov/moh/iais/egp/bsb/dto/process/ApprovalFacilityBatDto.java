package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2022/3/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalFacilityBatDto implements Serializable {
    private String id;
    private String schedule;
    private String batName;
    private String approval;

    private String activityId;
}
