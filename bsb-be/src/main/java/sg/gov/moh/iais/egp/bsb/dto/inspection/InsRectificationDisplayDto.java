package sg.gov.moh.iais.egp.bsb.dto.inspection;

import lombok.Data;


@Data
public class InsRectificationDisplayDto {
    private String item;
    private String remark;
    private String status;
    private String deadline;
}
