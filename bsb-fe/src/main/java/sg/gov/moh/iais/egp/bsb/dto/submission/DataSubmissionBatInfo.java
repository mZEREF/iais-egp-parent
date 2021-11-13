package sg.gov.moh.iais.egp.bsb.dto.submission;

import lombok.Data;

/**
 * @author Zhu Tangtang
 * @date 2021/11/8 17:38
 */
@Data
public class DataSubmissionBatInfo {
    private String id;
    private DataSubmissionInfo dataSubmissionInfo;
    private String expectedQty;
    private String actualQty;
    private String measurementUnit;
    private String destructionMethod;
    private String destructionProceduresDetails;
    private String discrepantReason;
    private String handleType;
    private String biologicalName;
    private String schedule;
}
