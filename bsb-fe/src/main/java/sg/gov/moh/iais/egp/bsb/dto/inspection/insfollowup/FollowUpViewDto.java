package sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;

import java.io.Serializable;
import java.util.List;

@Data
public class FollowUpViewDto implements Serializable {
    private List<FollowUpDisplayDto> followUpDisplayDtos;
    private List<ProcessHistoryDto> processHistoryDtoList;
    private String appId;

    //user entered data
    private String requestExtension;
    private String reasonForExtension;
    private String remarks;
    private List<DocMeta> docMetas;

}
