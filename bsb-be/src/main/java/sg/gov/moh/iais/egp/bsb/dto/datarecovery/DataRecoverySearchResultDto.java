package sg.gov.moh.iais.egp.bsb.dto.datarecovery;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.List;

@Data
public class DataRecoverySearchResultDto {
    private PageInfo pageInfo;
    private List<DataRecoveryDisplayDto> dataRecoveryDisplayDtoList;
}
