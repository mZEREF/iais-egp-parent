package sg.gov.moh.iais.egp.bsb.dto.approval;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import lombok.Data;

import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/10/29
 */
@Data
public class FacilityActivitySelectDto {
    private String facilityId;

    private List<SelectOption> activityIdList;

    public FacilityActivitySelectDto(String facilityId, List<SelectOption> activityIdList) {
        this.facilityId = facilityId;
        this.activityIdList = activityIdList;
    }
}
