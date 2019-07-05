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

package com.ecquaria.cloud.moh.iais.test.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Class description
 *
 * @author suocheng
 * @date 7/4/2019
 */
public class FormTestDto {

    @Setter @Getter
    private String id;

    @Setter @Getter
    private String rowguid;

    @Setter @Getter
    private String name;

    @Setter @Getter
    @ApiModelProperty(required = true)
    @Min(18)
    @Max(100)
    private String age;
}
