package sg.gov.moh.iais.egp.bsb.dto.adhocrfi;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.List;

@Data
public class AdhocRfiQueryResultDto {
    private PageInfo pageInfo;
    private List<AdhocRfiListViewDto> rfiList;
    private String facilityStatus;
}
