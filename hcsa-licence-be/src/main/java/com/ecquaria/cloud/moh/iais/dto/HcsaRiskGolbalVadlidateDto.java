package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;

import java.io.Serializable;

/**
 * @Author: jiahao
 * @Date: 2019/12/30 16:31
 */
@CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.HcsaGolbalValidate")
public class HcsaRiskGolbalVadlidateDto implements Serializable {
    private static final long serialVersionUID = -197865676069878454L;
}
