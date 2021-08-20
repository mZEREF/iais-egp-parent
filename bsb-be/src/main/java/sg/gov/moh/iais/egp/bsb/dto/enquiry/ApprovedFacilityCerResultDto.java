package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.Facility;

import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/8/16 9:31
 * DESCRIPTION: TODO
 **/

@Data
public class ApprovedFacilityCerResultDto {
    private PageInfo pageInfo;

    private List<Facility> bsbAFC;
}
