package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class IUICycleStageDto implements Serializable {
    private String stepKey;
    private String stepValue;
    // Determine the color of the display
    private String status;
    // To determine what the current stage can do
    private String permissions;

    public IUICycleStageDto() {
    }

    public IUICycleStageDto(String stepKey, String stepValue, String status, String permissions) {
        this.stepKey = stepKey;
        this.stepValue = stepValue;
        this.status = status;
        this.permissions = permissions;
    }
}
