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
    private String stepKey;
    private String stepValue;
    private String status;

    public ARCycleStageDto() {
    }

    public ARCycleStageDto(String stepKey, String stepValue, String status) {
        this.stepKey = stepKey;
        this.stepValue = stepValue;
        this.status = status;
    }
}
