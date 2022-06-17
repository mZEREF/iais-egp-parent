package com.ecquaria.cloud.moh.iais.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * ComplianceDto
 *
 * @author suocheng
 * @date 6/16/2022
 */
@Getter
@Setter
public class ComplianceDto implements Serializable {

    private String submissionType;
    private String submissionTypeDisplay;
    private String mins;
}
