package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;

import java.io.Serializable;

/**
 * @author Wenkang
 * @date 2019/12/14 15:34
 */
@CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.ClinicalOfficerValidate")
public class ClinicalOfficerValidateDto implements Serializable {
    private static final long serialVersionUID = 4203854142264240458L;
}
