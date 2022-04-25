package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Auther chenlei on 4/20/2022.
 */
@Getter
@Setter
public class ExcelCellDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int rownum;
    private int column;
    private boolean hidden;
    private boolean readOnly;

    private Object value;

}
