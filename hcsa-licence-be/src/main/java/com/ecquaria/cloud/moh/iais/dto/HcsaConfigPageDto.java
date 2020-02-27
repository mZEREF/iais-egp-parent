package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Wenkang
 * @date 2020/2/17 12:12
 */
@Data
public class HcsaConfigPageDto implements Serializable {
    private static final long serialVersionUID = 1184219139394029281L;
    private String routingSchemeId;
    private String workloadId;
    private String appType;
    private String stage;
    private String workingGroupId;
    private String workingGroupName;
    private Integer manhours;
    private List<WorkingGroupDto>  workingGroup;
    private String stageCode;
    private String workStageId;
    private String stageName;
    private String stageId;
    private String appTypeName;

}
