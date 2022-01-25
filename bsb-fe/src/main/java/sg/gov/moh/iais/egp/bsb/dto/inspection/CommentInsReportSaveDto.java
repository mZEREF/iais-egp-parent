package sg.gov.moh.iais.egp.bsb.dto.inspection;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.util.List;


@Data
public class CommentInsReportSaveDto {
    private String appId;
    private String upload;
    private List<DocRecordInfo> attachmentList;
}
