package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import java.util.List;


@Data
public class InboxAppSearchResultDto {
    private PageInfo pageInfo;
    private List<AppMainInfo> applications;
}
