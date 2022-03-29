package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2022/3/14
 */
@Data
public class InfoDto implements Serializable {
    //submission details
    private String referenceNo;
    private String applicationType;
    private String submissionType;
    private String applicationStatus;
}
