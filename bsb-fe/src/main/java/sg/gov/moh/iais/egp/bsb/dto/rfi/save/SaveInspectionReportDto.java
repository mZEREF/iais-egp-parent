package sg.gov.moh.iais.egp.bsb.dto.rfi.save;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;

@Data
public class SaveInspectionReportDto {
    private RfiDisplayDto rfiDisplayDto;
    private ReportDto reportDto;
    private String appId;
}
