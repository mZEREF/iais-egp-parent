package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.FacilityAgentSample;


import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/8/13 13:33
 * DESCRIPTION: TODO
 **/
@Data
public class ApprovalResultDto {
    private PageInfo pageInfo;

    private List<FacilityAgentSample> bsbApproval;
}
