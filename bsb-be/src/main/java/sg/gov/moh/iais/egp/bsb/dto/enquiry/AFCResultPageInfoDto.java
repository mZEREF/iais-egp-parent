package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;

import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.List;


@Data
public class AFCResultPageInfoDto {

    private PageInfo pageInfo;

    private List<AFCSearchResultDto> bsbAFC;

}
