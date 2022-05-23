package sg.gov.moh.iais.egp.bsb.dto.rfi.save;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;

@Data
public class SaveInspectionNCDto {
    private RfiDisplayDto rfiDisplayDto;
    private RectifyInsReportSaveDto rectifyInsReportSaveDto;
}
