package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;

import java.io.Serializable;

/**
 * @Author: jiahao
 * @Date: 2019/12/16 16:03
 */
public class CheckListVadlidateDto {
    @CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.InspectionCheckListValidation")
    public class ApplicationValidateDto implements Serializable {
        private static final long serialVersionUID = 2291544600420550312L;
    }

}
