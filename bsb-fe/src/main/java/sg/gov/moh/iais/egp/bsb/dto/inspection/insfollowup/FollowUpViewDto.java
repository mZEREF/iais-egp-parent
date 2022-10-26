package sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;

import java.io.Serializable;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FollowUpViewDto implements Serializable {
    private List<FollowUpDisplayDto> followUpDisplayDtos;
    private List<RemarkHistoryItemDto> processHistoryDtoList;
    private String appId;

    //user entered data
    private String requestExtension;
    private String reasonForExtension;
    private String remarks;
    private List<DocMeta> docMetas;

}
