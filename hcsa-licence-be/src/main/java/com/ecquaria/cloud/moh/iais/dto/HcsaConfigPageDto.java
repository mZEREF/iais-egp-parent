package com.ecquaria.cloud.moh.iais.dto;

import lombok.Data;

import java.io.Serializable;

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
    private String workingGroup;
    private Integer manhours;

    private String stageCode;

    private String stageName;

}
