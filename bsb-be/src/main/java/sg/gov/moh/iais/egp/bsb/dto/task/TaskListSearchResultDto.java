package sg.gov.moh.iais.egp.bsb.dto.task;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import java.util.Date;
import java.util.List;


@Data
public class TaskListSearchResultDto {
    private PageInfo pageInfo;
    private List<TaskInfo> tasks;

    @Data
    public static class TaskInfo {
        private String appId;
        private String applicationNo;
        private String appType;
        private String facilityName;
        private String facilityClassification;
        private String facilityBlkNo;
        private String facilityFloorNo;
        private String facilityUnitNo;
        private String facilityStreetName;
        private String facilityPostalCode;
        private String processType;
        private List<String> bats;
        private Date applicationDt;
        private String appStatus;
    }
}
