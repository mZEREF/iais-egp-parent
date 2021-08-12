package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;
import org.springframework.data.domain.Page;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/8/12 13:12
 * DESCRIPTION: TODO
 **/
@Data
public class FacilityResultDto {
    private PageInfo pageInfo;
    private List<FacilityInfoDto> bsbFac;

    public static FacilityResultDto of(Page<FacilityInfoDto> facilityPage) {
        FacilityResultDto dto = new FacilityResultDto();
        dto.pageInfo = PageInfo.of(facilityPage);
        dto.bsbFac = facilityPage.getContent();
        return dto;
    }

}
