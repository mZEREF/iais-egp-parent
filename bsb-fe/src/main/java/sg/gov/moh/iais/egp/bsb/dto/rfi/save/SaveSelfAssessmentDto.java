package sg.gov.moh.iais.egp.bsb.dto.rfi.save;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;

@Data
public class SaveSelfAssessmentDto {
    private RfiDisplayDto rfiDisplayDto;
    private SelfAssessmtChklDto selfAssessmtChklDto;
}
