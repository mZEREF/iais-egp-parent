package sg.gov.moh.iais.egp.bsb.dto.appointment;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;

import java.io.Serializable;
import java.util.List;

/**
 * @author tangtang
 */
@Data
public class AppointmentReviewDataDto implements Serializable {
    // application info page
    private SubmissionDetailsDto submissionDetailsDto;
    // application
    private String applicationId;
    private String applicationType;
    private String applicationNo;
    // task
    private String taskId;
    // user
    private String owner;
    // doc
    private List<DocDisplayDto> docDisplayDtoList;
    // user specify new date
    private String specifyStartDt;
    private String specifyEndDt;
    private String specifyStartHour;
    private String specifyEndHour;
    // save
    private String apptRefNo;
    private String userId;
    private String startDt;
    private String endDt;
    private String module;
}
