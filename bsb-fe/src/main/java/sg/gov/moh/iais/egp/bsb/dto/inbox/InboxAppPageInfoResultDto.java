package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import java.util.List;
import java.util.Map;


@Data
public class InboxAppPageInfoResultDto {
    private PageInfo pageInfo;
    private List<InboxAppResultDto> applications;
}
