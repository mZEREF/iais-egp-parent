package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Auther chenlei on 4/20/2022.
 */
@Getter
@Setter
public class ChklItemExcelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(
            cellIndex = 0,
            cellName = "S/N",
            readOnly = true
    )
    private String snNo;
    @ExcelProperty(
            cellIndex = 1,
            cellName = "Section",
            readOnly = true
    )
    private String section;
    @ExcelProperty(
            cellIndex = 2,
            cellName = "Item Description",
            readOnly = true
    )
    private String checklistItem;

    @ExcelProperty(
            cellIndex = 3,
            cellName = "Answer"
    )
    private String answer;

    @ExcelProperty(
            cellIndex = 4,
            cellName = "Remarks"
    )
    private String remarks;

    @ExcelProperty(
            cellIndex = 10,
            readOnly = true,
            hidden = true
    )
    private String itemKey;

}
