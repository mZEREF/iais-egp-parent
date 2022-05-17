package sg.gov.moh.iais.egp.bsb.dto.inspection;

import lombok.Data;

import java.io.Serializable;

@Data
public class RfiPreInspectionDto implements Serializable {
    private InsProcessDto insProcessDto;
    private int rfiFlag;
    private RfiApplicationDto rfiApplicationDto;
}
