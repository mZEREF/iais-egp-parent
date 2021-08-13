package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;
import org.springframework.data.domain.Page;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/8/13 13:33
 * DESCRIPTION: TODO
 **/
@Data
public class ApprovalResultDto {
    private PageInfo pageInfo;
    private List<ApprovalInfoDto> bsbApproval;

    public static ApprovalResultDto of(Page<ApprovalInfoDto> approvalPage) {
        ApprovalResultDto dto = new ApprovalResultDto();
        dto.pageInfo = PageInfo.of(approvalPage);
        dto.bsbApproval = approvalPage.getContent();
        return dto;
    }

}
