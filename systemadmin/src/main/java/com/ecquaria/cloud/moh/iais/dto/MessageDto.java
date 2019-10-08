package com.ecquaria.cloud.moh.iais.dto;

/*
 *File Name: MessageDto
 *Creator: yichen
 *Creation time:2019/8/6 10:05
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.web.logging.dto.AuditTrailDto;
import lombok.*;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.ValidateWithMethod;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto implements Serializable {
    private static final long serialVersionUID = -2542988198043832001L;

    private String id;

    @NotBlank(message = "can not is blank!", profiles = {"edit", "search"})
    @NotNull(message = "can not is null!", profiles = {"edit", "search"})
    private String domainType;


    @NotBlank(message = "can not is blank!", profiles = {"edit"})
    @NotNull(message = "can not is null!", profiles = {"edit"})
    private String msgType;


    @NotBlank(message = "can not is blank!", profiles = {"edit"})
    @NotNull(message = "can not is null!", profiles = {"edit"})
    private String module;


    @Length(min = 1, max = 255, message = "length should in 1 - 255", profiles = {"edit"})
    @NotBlank(message = "can not is blank!", profiles = {"edit"})
    @NotNull(message = "can not is null!", profiles = {"edit"})
    @ValidateWithMethod(methodName = "validateDescriptionRegEx", parameterType = String.class, message = "no special characters are allowed",
            profiles ="edit")
    private String description;


    private Character status;

    private String codeKey;

    @Length(min = 1,  message = "can not is blank!", profiles = {"edit"})
    @NotBlank(message = "can not is blank!", profiles = {"edit"})
    @NotNull(message = "can not is null!", profiles = {"edit"})
    @ValidateWithMethod(methodName = "validateDescriptionRegEx", parameterType = String.class, message = "no special characters are allowed",
            profiles ="edit")
    private String message;

    private AuditTrailDto auditTrailDto;

    public boolean validateDescriptionRegEx(String description){
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(description);
        return !matcher.find();
    }

}
