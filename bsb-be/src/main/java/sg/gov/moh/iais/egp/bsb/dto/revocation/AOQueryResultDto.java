package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Data;
import org.springframework.data.domain.Page;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/8/11 14:28
 * DESCRIPTION: TODO
 **/

@Data
public class AOQueryResultDto {
    private PageInfo pageInfo;
    private List<AOQueryInfoDto> bsbInboxes;

    public static AOQueryResultDto of(Page<AOQueryInfoDto> applicationPage) {
        AOQueryResultDto dto = new AOQueryResultDto();
        dto.pageInfo = PageInfo.of(applicationPage);
        dto.bsbInboxes = applicationPage.getContent();
        return dto;
    }
}
