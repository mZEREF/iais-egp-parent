package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;

import java.io.Serializable;

/**
 * @Author: jiahao
 * @Date: 2019/12/24 13:46
 */
@CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.HcsaLeadershipValidate")
public class HcsaRiskLeaderShipVadlidateDto implements Serializable {
    private static final long serialVersionUID = -535671983530080387L;
}
