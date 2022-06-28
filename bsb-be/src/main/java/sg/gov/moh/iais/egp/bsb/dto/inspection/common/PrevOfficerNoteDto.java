package sg.gov.moh.iais.egp.bsb.dto.inspection.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class PrevOfficerNoteDto implements Serializable {
    private String remark;
    private String recommendation;
}
