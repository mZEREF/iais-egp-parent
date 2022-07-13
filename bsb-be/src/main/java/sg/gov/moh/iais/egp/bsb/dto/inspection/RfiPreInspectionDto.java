package sg.gov.moh.iais.egp.bsb.dto.inspection;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiProcessDto;

import java.io.Serializable;

@Data
public class RfiPreInspectionDto implements Serializable {
    private InsProcessDto insProcessDto;
    private int rfiFlag;
    private RfiProcessDto rfiProcessDto;
}
