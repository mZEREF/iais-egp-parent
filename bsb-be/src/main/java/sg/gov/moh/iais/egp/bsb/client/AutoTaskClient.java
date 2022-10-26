package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class)
public interface AutoTaskClient {
    @PostMapping(value = "/auto/inspection/non-compliance/remind",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void doInspectionRemindUserDoNCTask();

    @PostMapping(value = "auto/inspection/afc/moh-no-response",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void doInspectionAFCDOOrAONotRespond();

    @PostMapping(value = "/auto/inspection/checklist/remind",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void doInspectionSubmitChecklistRemindTask();

    @PostMapping(value = "/auto/inspection/cert-report/applicant-not-response",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void doInspectionCertReportApplicantNotRespond();

    @PostMapping(value = "/auto/application/rf/inventory-declaration/reminder",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void doRfPVInventoryDeclarationRemindTask();

    @PostMapping(value = "/auto/application/second-schedule-work-activities/moh/reminder",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void doSecondScheduleWorkActivitiesEndDateReminder();

    @PostMapping(value = "/auto/inspection/applicant-not-respond-nc/moh/reminder",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void doApplicantNotResponseNCMOhReminder();

    @PostMapping(value = "/auto/inspection/not-mark-as-ready/moh/reminder",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void doMohInsTaskNotMarkAsReady();

    @PostMapping(value = "/auto/inspection/not-respond-follow-up/applicant/reminder",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void doNotResponseFollowupApplicantReminder();

    @PostMapping(value = "/auto/inspection/ins-report-response/applicant/reminder",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void applicantRespondInsReportReminder();

    @PostMapping(value = "/auto/inspection/not-indicate-afc-selection/moh/reminder",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    void mohNotIndicateAfcReminder();

}
