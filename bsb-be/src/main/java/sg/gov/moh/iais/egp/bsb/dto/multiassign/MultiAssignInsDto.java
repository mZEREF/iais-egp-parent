package sg.gov.moh.iais.egp.bsb.dto.multiassign;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class MultiAssignInsDto implements Serializable {
    private String taskId;
    private String applicationId;
    private String applicationNo;
    private String applicationStatus;
    private String facClassification;
    private String activityType;
    private String facName;
    private String facAddress;
    private String insLeader;

    private Map<String, SelectOption> optionMap;
    private List<String> inspectors;
    private List<String> inspectorsDisplayName;
    private String module;
    private boolean canMultiAssign;
}
