package com.ecquaria.cloud.moh.iais.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

public class AuditTrailDto {
    @ApiModelProperty(value = "operation", required = true)
    @Getter @Setter private int operation;
    @ApiModelProperty(value = "nricNumber", required = false)
    @Getter @Setter private String nricNumber;
    @ApiModelProperty(value = "uenId", required = false)
    @Getter @Setter private String uenId;
    @ApiModelProperty(value = "mohUserId", required = false)
    @Getter @Setter private String mohUserId;
    @ApiModelProperty(value = "loginType", required = false)
    @Getter @Setter private int loginType;
    @ApiModelProperty(value = "userDomain", required = false)
    @Getter @Setter private String userDomain;
    @ApiModelProperty(value = "sessionId", required = false)
    @Getter @Setter private String sessionId;
    @ApiModelProperty(value = "clientIp", required = false)
    @Getter @Setter private String clientIp;
    @ApiModelProperty(value = "userAgent", required = false)
    @Getter @Setter private String userAgent;
    @ApiModelProperty(value = "applicationNum", required = false)
    @Getter @Setter private String applicationNum;
    @ApiModelProperty(value = "licenseNum", required = false)
    @Getter @Setter private String licenseNum;
    @ApiModelProperty(value = "module", required = false)
    @Getter @Setter private String module;
    @ApiModelProperty(value = "functionName", required = false)
    @Getter @Setter private String functionName;
    @ApiModelProperty(value = "programeName", required = false)
    @Getter @Setter private String programeName;
    @ApiModelProperty(value = "beforeAction", required = false)
    @Getter @Setter private String beforeAction;
    @ApiModelProperty(value = "afterAction", required = false)
    @Getter @Setter private String afterAction;
    @ApiModelProperty(value = "validationFail", required = false)
    @Getter @Setter private String validationFail;
    @ApiModelProperty(value = "viewParams", required = false)
    @Getter @Setter private String viewParams;
    @ApiModelProperty(value = "failReason", required = false)
    @Getter @Setter private String failReason;
}
