package com.ecquaria.cloud.moh.iais.dto;

/*
 *File Name: MessageDto
 *Creator: yichen
 *Creation time:2019/8/6 10:05
 *Describe:
 */

import lombok.Getter;
import lombok.Setter;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.ValidateWithMethod;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageDto implements Serializable {
    private static final long serialVersionUID = -2542988198043832001L;
    public static final String MESSAGE_REQUEST_DTO = "msgRequestDto";

    @Setter @Getter
    private String id;
    @Setter @Getter
    private String rowguid;

    @Setter @Getter
    @NotBlank(message = "can not is blank!", profiles = {"edit"})
    @NotNull(message = "can not is null!", profiles = {"edit"})
    private String domainType;

    @Setter @Getter
    @NotBlank(message = "can not is blank!", profiles = {"edit"})
    @NotNull(message = "can not is null!", profiles = {"edit"})
    private String msgType;

    @Setter @Getter
    @NotBlank(message = "can not is blank!", profiles = {"edit"})
    @NotNull(message = "can not is null!", profiles = {"edit"})
    private String module;

    @Setter @Getter
    @Length(min = 1, max = 255, message = "can not is blank!", profiles = {"edit"})
    @NotBlank(message = "can not is blank!", profiles = {"edit"})
    @NotNull(message = "can not is null!", profiles = {"edit"})
    @ValidateWithMethod(methodName = "validateDescriptionRegEx", parameterType = String.class, message = "no special characters are allowed",
            profiles ="edit")
    private String description;

    @Getter
    @Setter
    private Integer status;

    @Getter
    @Setter
    private String codeKey;

    public boolean validateDescriptionRegEx(String description){
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(description);
        if(matcher.find()){
            return false;
        }
        return true;
    }

}
