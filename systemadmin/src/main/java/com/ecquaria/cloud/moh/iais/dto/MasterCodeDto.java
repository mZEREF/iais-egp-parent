package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.validation.MasterCodeValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import sg.gov.moh.iais.common.validation.annotations.CustomValidate;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Hua_Chong
 * @Date 2019/8/6 15:11
 */
@Data
@CustomValidate(impClass = MasterCodeValidator.class, properties = {"create", "edit"})
public class MasterCodeDto implements Serializable {


    private static final long serialVersionUID = 37804421724981355L;

    @ApiModelProperty(value = "masterCodeId", required = true)
    @NotNull(message = "masterCodeId is mandatory null.", profiles = {"create", "edit"})
    @NotBlank(message = "masterCodeId is mandatory Blank.", profiles = {"create", "edit"})
    private int masterCodeId;

    private String rowguid;

    private String masterCodeKey;

    private int  codeCategory;

    private String codeValue;

    private String codeDescription;

    private String filterValue;

    private float sequence;

    private String remarks;

    private int status;

    private Date effectiveFrom;

    private Date effectiveTo;

    private int version;
}
