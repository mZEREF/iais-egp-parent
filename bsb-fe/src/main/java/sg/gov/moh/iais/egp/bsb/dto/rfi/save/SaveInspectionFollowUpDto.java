package sg.gov.moh.iais.egp.bsb.dto.rfi.save;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;

@Data
public class SaveInspectionFollowUpDto {
    private RfiDisplayDto rfiDisplayDto;
    private FollowUpSaveDto followUpSaveDto;
}
