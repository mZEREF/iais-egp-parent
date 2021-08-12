package sg.gov.moh.iais.egp.bsb.dto.enquiry;

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
public class ApplicationResultDto {
    private PageInfo pageInfo;
    private List<ApplicationInfoDto> bsbApp;

    public static ApplicationResultDto of(Page<ApplicationInfoDto> applicationPage) {
        ApplicationResultDto dto = new ApplicationResultDto();
        dto.pageInfo = PageInfo.of(applicationPage);
        dto.bsbApp = applicationPage.getContent();
        return dto;
    }
}
