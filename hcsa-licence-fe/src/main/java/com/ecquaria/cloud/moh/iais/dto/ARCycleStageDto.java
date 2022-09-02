package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author tangtang
 * @date 2022/7/25 16:36
 */
@Getter
@Setter
public class ARCycleStageDto implements Serializable {
    private static final long serialVersionUID = 2291544600420550312L;
    private String stepKey;
    private String stepValue;
    // Determine the color of the display
    private String status;
    // To determine what the current stage can do
    private String permissions;

    public ARCycleStageDto() {
    }

    public ARCycleStageDto(String stepKey, String stepValue, String status, String permissions) {
        this.stepKey = stepKey;
        this.stepValue = stepValue;
        this.status = status;
        this.permissions = permissions;
    }
}
