package sg.gov.moh.iais.egp.bsb.dto.adhocInspection;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.List;


@Data
public class AdhocInspectionSearchResultDto {
    private PageInfo pageInfo;
    private List<AdhocInspectionDisplayDto> displayDtos;
}
