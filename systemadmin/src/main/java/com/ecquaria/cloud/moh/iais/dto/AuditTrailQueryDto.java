/*
 * This file is generated by ECQ project skeleton automatically.
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

package com.ecquaria.cloud.moh.iais.dto;


import com.ecquaria.cloud.moh.iais.common.validation.annotations.CustomValidate;
import com.ecquaria.cloud.moh.iais.validate.AuditTrailDtoValidate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@CustomValidate(impClass = AuditTrailDtoValidate.class, properties = {"query"})
public
class AuditTrailQueryDto implements Serializable {
    private static final long serialVersionUID = -6184748147127672799L;

    private Integer auditId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actionTime;
    private int operation;
    private String  nricNumber;
    private String  uenId;
    private String  mohUserId;
    private int loginType;
    private String  sessionId;
    private String  clientIp;
    private String  userAgent;
    private String  appNum;
    private String  licenseNum;
    private String  module;
    private String  functionName;
    private String  programmeName;
    private String  beforeAction;
    private String  afterAction;
    private String  validationFail;
    private String  viewParams;
    private String  failReason;
    private String  operationType;

}
