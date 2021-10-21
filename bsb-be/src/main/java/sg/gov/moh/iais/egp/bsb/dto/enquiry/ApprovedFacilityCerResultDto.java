package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.FacilityCertifierReg;

import java.util.List;


/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/

@Data
public class ApprovedFacilityCerResultDto {
    private PageInfo pageInfo;

    private List<FacilityCertifierReg> bsbAFC;
}
