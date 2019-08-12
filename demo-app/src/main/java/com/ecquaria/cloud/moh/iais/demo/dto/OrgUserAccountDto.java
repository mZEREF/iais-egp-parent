/*
 *   This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.demo.dto;

import com.ecquaria.cloud.moh.iais.demo.validate.OrgUserAccountValidate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.ValidateWithMethod;
import sg.gov.moh.iais.common.utils.StringUtil;
import sg.gov.moh.iais.common.validation.annotations.CustomValidate;

import java.io.Serializable;


/**
 * Org User Account
 *
 * @author suocheng
 * @date 7/11/2019
 */
@CustomValidate(impClass = OrgUserAccountValidate.class, properties = {"create", "edit"})
public class OrgUserAccountDto implements Serializable {
    private static final long serialVersionUID = 2178140403780660322L;

    @Getter @Setter
    private Integer id;

    @Getter @Setter
    private String rowguid;

    @Getter @Setter
    private String name;

    @ApiModelProperty(value = "nircNo", required = true)
    @NotNull(message = "nircNo is mandatory null.", profiles = {"create", "edit"})
    @NotBlank(message = "nircNo is mandatory Blank.", profiles = {"create", "edit"})
    @ValidateWithMethod(methodName = "validateNricEdit", parameterType = String.class, message = "Cannot change NRIC No.",
            profiles ="edit")
    @Getter @Setter
    private String nircNo;
    @Getter @Setter private String oldNricNo;

    @Getter @Setter
    private String corpPassId;

    @Getter @Setter
    private String status;

    @ValidateWithMethod.List(value={@ValidateWithMethod(methodName = "fakeValidateA", parameterType = String.class,
            message = "12117", profiles={"create","edit"}),
            @ValidateWithMethod(methodName = "fakeValidateB", parameterType = String.class, message = "12134")})
    @Getter @Setter
    private String orgId;
    @Getter @Setter private boolean editFlag;

    //Validation method
    public boolean validateNricEdit(String nric) {
        if (StringUtil.isEmpty(nric))
            return true;

        return nric.equals(oldNricNo);
    }

    public boolean fakeValidateA(String arg) {
        if (StringUtil.isEmpty(arg))
            return false;
        return true;
    }

    public boolean fakeValidateB(String arg) {
        if (StringUtil.isEmpty(arg))
            return false;
        return true;
    }
}
