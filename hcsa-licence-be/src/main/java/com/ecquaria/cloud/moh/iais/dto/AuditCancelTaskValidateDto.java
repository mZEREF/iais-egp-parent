package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;

import java.io.Serializable;

/**
 * @Author: wangyu
 * @Date: 2020/3/24
 */
@CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.AuditCancelTaskValidate")
public class AuditCancelTaskValidateDto implements Serializable {
    private static final long serialVersionUID = 9174005124748046194L;

}
