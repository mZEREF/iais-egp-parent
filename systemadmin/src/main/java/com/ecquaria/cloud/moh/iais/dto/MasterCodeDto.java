package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.validation.annotations.CustomValidate;
import com.ecquaria.cloud.moh.iais.validate.MasterCodeValidate;
import lombok.Getter;
import lombok.Setter;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Hua_Chong
 * @Date 2019/8/6 15:11
 */
@CustomValidate(impClass = MasterCodeValidate.class, properties = {"create", "edit"})
public class MasterCodeDto implements Serializable {
    private static final long serialVersionUID = 37804421724981355L;

    @NotNull(message = "masterCodeId is mandatory null.", profiles = {"create", "edit"})
    @NotBlank(message = "masterCodeId is mandatory Blank.", profiles = {"create", "edit"})
    @Getter @Setter private int masterCodeId;
    @Getter @Setter private String rowguid;
    @Getter @Setter private String masterCodeKey;
    @Getter @Setter private int  codeCategory;
    @Getter @Setter private String codeValue;
    @Getter @Setter private String codeDescription;
    @Getter @Setter private int status;

}
