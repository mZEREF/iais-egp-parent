package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;


import java.util.List;


/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/
@Data
public class ApprovalResultDto{

    private PageInfo pageInfo;

    private List<ApprovalInfoDto> bsbApproval;
}
