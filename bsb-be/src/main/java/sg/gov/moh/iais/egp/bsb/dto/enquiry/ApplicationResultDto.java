package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;
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
}
