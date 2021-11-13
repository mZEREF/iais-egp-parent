package sg.gov.moh.iais.egp.bsb.dto.submission;

import lombok.Data;

import java.util.Date;

/**
 * @author tangtang
 **/
@Data
public class DataSubmissionDocInfo {
    private String id;
    private DataSubmissionInfo dataSubmissionInfo;
    private String docName;
    private Long docSize;
    private String fileRepoId;
    private Date submitDt;
    private String submitBy;
    private String docType;
}