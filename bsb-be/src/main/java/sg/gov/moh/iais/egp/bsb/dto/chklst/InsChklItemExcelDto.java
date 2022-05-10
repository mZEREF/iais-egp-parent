package sg.gov.moh.iais.egp.bsb.dto.chklst;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther chenlei on 4/25/2022.
 */
@Setter
@Getter
public class InsChklItemExcelDto implements Serializable {

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
            cellName = "Compliance"
    )
    private String answer;

    @ExcelProperty(
            cellIndex = 4,
            cellName = "Findings/Non-Compliance"
    )
    private String findings;

    @ExcelProperty(
            cellIndex = 5,
            cellName = "Actions Required"
    )
    private String actionRequired;

    @ExcelProperty(
            cellIndex = 6,
            cellName = "Rectified"
    )
    private String rectified;

    @ExcelProperty(
            cellIndex = 7,
            cellName = "Follow-up Item"
    )
    private String followupItem;

    @ExcelProperty(
            cellIndex = 8,
            cellName = "Observations for Follow-up"
    )
    private String observeFollowup;

    @ExcelProperty(
            cellIndex = 9,
            cellName = "Action Required"
    )
    private String followupAction;

    @ExcelProperty(
            cellIndex = 10,
            cellName = "Due Date",
            objectType = Date.class
    )
    private String dueDate;

    @ExcelProperty(
            cellIndex = 15,
            readOnly = true,
            hidden = true
    )
    private String itemKey;

}
