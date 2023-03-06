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

    private int sheetAt;

    private int cellIndex;

    private String cellName;

    private String fieldName;

    private boolean indicator;

    public ExcelPropertyDto() {
    }

    public ExcelPropertyDto(int cellIndex, String cellName, String fieldName) {
        this.cellIndex = cellIndex;
        this.cellName = cellName;
        this.fieldName = fieldName;
    }

    public ExcelPropertyDto(int cellIndex, String cellName, String fieldName, boolean indicator) {
        this.cellIndex = cellIndex;
        this.cellName = cellName;
        this.fieldName = fieldName;
        this.indicator = indicator;
    }

    public ExcelPropertyDto(int sheetAt, int cellIndex, String cellName, String fieldName) {
        this.sheetAt = sheetAt;
        this.cellIndex = cellIndex;
        this.cellName = cellName;
        this.fieldName = fieldName;
    }

}
