package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Description ExcelPropertyDto
 * @Auther chenlei on 12/30/2021.
 */
@Getter
@Setter
public class ExcelPropertyDto implements Serializable {

    private int cellIndex;

    private String cellName;

    private String fieldName;

    public ExcelPropertyDto() {
    }

    public ExcelPropertyDto(int cellIndex, String cellName, String fieldName) {
        this.cellIndex = cellIndex;
        this.cellName = cellName;
        this.fieldName = fieldName;
    }

}
