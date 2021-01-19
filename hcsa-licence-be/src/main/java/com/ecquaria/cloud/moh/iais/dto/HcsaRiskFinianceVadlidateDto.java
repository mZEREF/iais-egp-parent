package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;

import java.io.Serializable;

/**
 * @Author: jiahao
 * @Date: 2019/12/24 10:50
 */
@CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.HcsaFinancialRiskValidate")
public class HcsaRiskFinianceVadlidateDto implements Serializable {
    private static final long serialVersionUID = -6546223455260532084L;
}
