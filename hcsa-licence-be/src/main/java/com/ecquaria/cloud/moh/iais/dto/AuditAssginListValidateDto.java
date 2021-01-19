package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;

import java.io.Serializable;

/**
 * @Author: jiahao
 * @Date: 2020/2/27 9:18
 */
@CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.AuditAssginListValidate")
public class AuditAssginListValidateDto implements Serializable {
    private static final long serialVersionUID = 2079484719509656141L;
}