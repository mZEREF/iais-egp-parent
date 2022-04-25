package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Auther chenlei on 4/20/2022.
 */
@Getter
@Setter
public class ExcelSheetDto implements Serializable {

    private String sheetName;
    private int sheetAt;
    private int startRowIndex;
    private boolean block;
    private Map<Integer, List<Integer>> unlockCellMap;
    private String pwd;

    private boolean changeHeight;
    private Short defaultRowHeight;
    private Map<Integer, Integer> widthMap;

    private boolean needFiled;
    private int[] filedRows;

    // write
    private List<?> source;
    private Class<?> sourceClass;
    // read
    private boolean defaultValueNull;

}
