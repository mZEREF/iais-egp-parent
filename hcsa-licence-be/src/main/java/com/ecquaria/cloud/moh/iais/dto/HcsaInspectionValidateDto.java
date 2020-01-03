package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;

import java.io.Serializable;

/**
 * @Author: jiahao
 * @Date: 2020/1/2 18:23
 */
@CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.HcsaInspectionValidate")
public class HcsaInspectionValidateDto implements Serializable {
    private static final long serialVersionUID = -1440270701271474102L;
}
