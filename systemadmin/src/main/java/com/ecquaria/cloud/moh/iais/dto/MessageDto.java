package com.ecquaria.cloud.moh.iais.dto;

/*
 *File Name: MessageDto
 *Creator: yichen
 *Creation time:2019/8/6 10:05
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.validate.MessageValidate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import sg.gov.moh.iais.common.validation.annotations.CustomValidate;

@CustomValidate(impClass = MessageValidate.class, properties = {"search", "edit", "create"})
public class MessageDto {
    public static final String MESSAGE_REQUEST_DTO = "msgRequestDto";

    @Setter @Getter
    private String msgId;
    @Setter @Getter
    private String rowId;

    @Setter @Getter
    @ApiModelProperty(required = true,value = "domainType")
    @NotNull(message = "Domain Type can not is null!", profiles = {"search", "edit", "create"})
    @Length(min = 1)
    private String domainType;
    @Setter @Getter
    @Length(min = 1)
    private String msgType;

    @Setter @Getter
    @Length(min = 1)
    private String module;

    @Setter @Getter
    @Length(min = 1, max = 255)
    @NotBlank(message = "Description  can not is blank!", profiles = {"edit", "create"})
    @NotNull(message = "Description  can not is null!", profiles = {"edit", "create"})
    private String description;
}
