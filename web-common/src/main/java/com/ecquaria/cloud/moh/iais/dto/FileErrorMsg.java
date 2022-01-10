package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileErrorMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private int row;

    private int col;

    private String colHeader;

    private String cellName;

    //private String fieldName;

    private String message;

    private Map<String, String> arguments;

    public FileErrorMsg() {
        arguments = new HashMap<>();
    }

    public FileErrorMsg(int row, ExcelPropertyDto excelPropertyDto, String message) {
        this();
        this.row = row;
        if (excelPropertyDto != null) {
            this.col = excelPropertyDto.getCellIndex();
            this.cellName = excelPropertyDto.getCellName();
        }
        this.message = message;
    }

    public FileErrorMsg(int row, int col, String cellName, String message) {
        this();
        this.row = row;
        this.col = col;
        this.cellName = cellName;
        this.message = message;
    }

    public FileErrorMsg addMsgArg(String placeHolder, String replaceVal) {
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

    public String getColHeader() {
        if (colHeader == null) {
            colHeader = IaisCommonUtils.getColHeader(col);
        }
        return colHeader;
    }

}
