package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Description FileErrorMsg
 * @Auther chenlei on 11/23/2021.
 */
@Getter
@Setter
public class FileErrorMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private int row;

    private String fieldName;

    private String message;

    private Map<String, String> arguments;

    public FileErrorMsg() {
        arguments = new HashMap<>();
    }

    public FileErrorMsg(int row, String fieldName, String message) {
        this.row = row;
        this.fieldName = fieldName;
        this.message = message;
    }

    public FileErrorMsg addMsgArgs(String placeHolder, String replaceVal) {
        arguments.put(Objects.requireNonNull(placeHolder), Objects.requireNonNull(replaceVal));
        return this;
    }

    public String getMessage() {
        if (arguments.isEmpty()) {
            message = MessageUtil.getMessageDesc(message);
        } else {
            message = MessageUtil.getMessageDesc(message, arguments);
        }
        return message;
    }

}
