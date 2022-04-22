package sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.util.List;

@Data
public class FollowUpSaveDto {
    private String appId;
    private String requestExtension;
    private String reasonForExtension;
    private String remarks;
    private List<DocRecordInfo> attachmentList;
}
