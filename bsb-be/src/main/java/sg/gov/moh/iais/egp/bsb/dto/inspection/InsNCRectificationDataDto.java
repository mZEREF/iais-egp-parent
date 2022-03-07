package sg.gov.moh.iais.egp.bsb.dto.inspection;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.util.List;

@Data
public class InsNCRectificationDataDto {
    private InsFacInfoDto facInfoDto;
    private List<InsRectificationDisplayDto> rectificationDisplayDtoList;
    private List<DocRecordInfo> rectificationDoc;
    //AO preview DO decision
    private String decision;
}
