package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;
import java.io.Serializable;

/**
 * ApplicationValidateDto
 *
 * @author Jinhua
 * @date 2019/12/13 9:26
 */
@CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.NewAppValidator")
public class ApplicationValidateDto implements Serializable {
    private static final long serialVersionUID = 2291544600420550312L;
}
