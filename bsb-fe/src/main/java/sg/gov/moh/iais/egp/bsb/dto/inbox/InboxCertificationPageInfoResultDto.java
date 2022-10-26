package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.List;


@Data
public class InboxCertificationPageInfoResultDto {
    private PageInfo pageInfo;

    private List<InboxAFCResultDto> certificationReports;
}
