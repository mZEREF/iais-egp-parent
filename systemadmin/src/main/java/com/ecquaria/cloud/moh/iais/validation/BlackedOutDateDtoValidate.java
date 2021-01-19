package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;

import java.io.Serializable;

/**
 * @author: yichen
 * @date time:12/30/2019 9:41 AM
 * @description:
 */

@CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.BlackedOutDateValidator")
public class BlackedOutDateDtoValidate implements Serializable {
}
