package sg.gov.moh.iais.egp.bsb.dto.inspection;

import lombok.Data;

import java.io.Serializable;


@Data
public class InsRectificationDisplayDto implements Serializable {
    private String itemValue;
    private String checklistItem;
    private String itemDesc;
    private String finding;
    private String actionRequired;
    private String remark;
    private String status;
}
