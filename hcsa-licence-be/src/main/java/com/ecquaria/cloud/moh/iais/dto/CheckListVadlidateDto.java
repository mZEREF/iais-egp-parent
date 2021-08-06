package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;

import java.io.Serializable;

/**
 * @Author: jiahao
 * @Date: 2019/12/16 16:03
 */

@CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.InspectionCheckListValidation")
public class CheckListVadlidateDto implements Serializable {
    private static final long serialVersionUID = 1L;
}


