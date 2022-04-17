package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.List;


@Data
public class FacResultPageInfoDto {

    private PageInfo pageInfo;
    private List<FacResultDto> bsbFac;
}
