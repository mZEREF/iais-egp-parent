package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;

import java.io.Serializable;

/**
 * @Author: jiahao
 * @Date: 2020/1/7 14:39
 */
@CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.HcsaLicTenVadlidate")
public class HcsaRiskLicTenValidateDto implements Serializable {
    private static final long serialVersionUID = -7880878549649744114L;
}
