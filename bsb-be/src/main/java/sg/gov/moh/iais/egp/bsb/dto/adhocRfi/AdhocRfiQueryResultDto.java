package sg.gov.moh.iais.egp.bsb.dto.adhocrfi;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocRfiDto;

import java.util.List;

@Data
public class AdhocRfiQueryResultDto {
    private PageInfo pageInfo;
    private List<AdhocRfiDto> rfiList;
}
