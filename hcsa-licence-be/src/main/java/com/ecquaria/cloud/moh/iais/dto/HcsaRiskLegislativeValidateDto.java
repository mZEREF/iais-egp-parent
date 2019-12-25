package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;

import java.io.Serializable;

/**
 * @Author: jiahao
 * @Date: 2019/12/25 13:28
 */
@CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.HcsaLegislativeValidate")
public class HcsaRiskLegislativeValidateDto implements Serializable {
    private static final long serialVersionUID = -329448822674776035L;
}
