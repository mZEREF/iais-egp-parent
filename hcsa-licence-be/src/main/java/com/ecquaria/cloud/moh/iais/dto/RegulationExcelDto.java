package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: yichen
 * @date time:12/31/2019 4:32 PM
 * @description:
 */
@Getter
@Setter
@ToString
public class RegulationExcelDto {
    @ExcelProperty( index = 0)
    private Integer sn;

    @ExcelProperty( index = 1)
    private String clauseNo;

    @ExcelProperty( index = 3)
    private String clause;
}
