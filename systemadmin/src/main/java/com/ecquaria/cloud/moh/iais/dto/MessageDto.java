package com.ecquaria.cloud.moh.iais.dto;

/*
 *File Name: MessageDto
 *Creator: yichen
 *Creation time:2019/8/6 10:05
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.validate.MessageValidate;
import lombok.Getter;
import lombok.Setter;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import sg.gov.moh.iais.common.validation.annotations.CustomValidate;

import java.io.Serializable;

@CustomValidate(impClass = MessageValidate.class, properties = {"search", "edit", "create"})
public class MessageDto implements Serializable {
    public static final String MESSAGE_REQUEST_DTO = "msgRequestDto";

    @Setter @Getter
    private String id;
    @Setter @Getter
    private String rowguid;

    @Setter @Getter
    private String domainType;
    @Setter @Getter
    private String msgType;

    @Setter @Getter
    private String module;

    @Setter @Getter
    @Length(min = 1, max = 255)
    @NotBlank(message = "Description  can not is blank!", profiles = {"edit"})
    @NotNull(message = "Description  can not is null!", profiles = {"edit"})
    private String description;

    @Getter
    @Setter
    private Integer status;

    @Getter
    @Setter
    private String codeKey;
}
