package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.followup.*;
import sg.gov.moh.iais.egp.bsb.dto.followup.view.Followup1AViewDto;
import sg.gov.moh.iais.egp.bsb.dto.followup.view.Followup1BViewDto;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.BatBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.facility.FacilityBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.*;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.view.InvestViewDto;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.IncidentInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.IncidentNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.PersonInvolvedInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.PersonReportingDto;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.view.IncidentViewDto;

import java.util.List;
import java.util.Map;

/**
 * @author YiMing
 * @version 2022/3/2 13:51
 **/
@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "reportableEvent")
public interface ReportableEventClient {
    @PostMapping(path = "/reportable-event/incident-notification/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftIncidentNotification(@RequestBody IncidentNotificationDto dto);

    @PostMapping(path = "/reportable-event/incident-notification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewIncidentNotification(@RequestBody IncidentNotificationDto dto);

    @GetMapping(path = "/reportable-event/incident-notification/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentNotificationDto> retrieveIncidentNotByReferenceId(@PathVariable("id") String refId);

    @GetMapping(path = "/reportable-event/incident-notification/view/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<IncidentViewDto> findIncidentViewDtoByIncidentId(@PathVariable("id") String incidentId);

    @PostMapping(path = "/reportable-event/incident-notification/form-validation/incident-info", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateIncidentInfo(@RequestBody IncidentInfoDto dto);

    @PostMapping(path = "/reportable-event/incident-notification/form-validation/person-reporting-info", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePersonInvolvedInfo(@RequestBody PersonInvolvedInfoDto dto);

    @PostMapping(path = "/reportable-event/incident-notification/form-validation/involved-person", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePersonReporting(@RequestBody PersonReportingDto dto);

    @PostMapping(path = "/reportable-event/incident-notification/form-validation/docs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePrimaryDoc(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @GetMapping(path = "/bat-info/{facId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, List<BatBasicInfo>> queryFacilityActivityFacIdMap(@PathVariable("facId") String facId);

    @GetMapping(path = "/reportable-event/incident-notification/all/facility", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FacilityBasicInfo> queryDistinctFacilityName();




    @PostMapping(path = "/reportable-event/investigation-report/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftInvestigationReport(@RequestBody InvestReportDto dto);

    @GetMapping(path = "/reportable-event/incident-notification/{refNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    IncidentDto queryIncidentByRefNo(@PathVariable("refNo") String refNo);

    @GetMapping(path = "/reportable-event/investigation-report/reference-no", produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> queryAllRefNo();

    @PostMapping(path = "/reportable-event/investigation-report", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewIncidentInvest(@RequestBody InvestReportDto dto);

    @PostMapping(path = "/reportable-event/investigation-report/form-validation/selection", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateReferNoSelectionDto(@RequestBody ReferNoSelectionDto dto);

    @PostMapping(path = "/reportable-event/investigation-report/form-validation/incident-info", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateInvestigationIncidentInfo(@RequestBody sg.gov.moh.iais.egp.bsb.dto.report.investigation.IncidentInfoDto dto);

    @PostMapping(path = "/reportable-event/investigation-report/form-validation/incident-investigation", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateIncidentInvestDto(@RequestBody IncidentInvestDto dto);

    @PostMapping(path = "/reportable-event/investigation-report/form-validation/medical-investigation", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateMedicalInvestDto(@RequestBody MedicalInvestDto dto);

    @PostMapping(path = "/reportable-event/investigation-report/form-validation/docs", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateInvestigationReportPrimaryDoc(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @GetMapping(path = "/reportable-event/investigation-report/view/{referNo}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InvestViewDto> findInvestViewDtoByReferenceNo(@PathVariable("referNo") String referNo);




    @GetMapping(path = "/reportable-event/follow-up/reportA/all/refNo",produces =MediaType.APPLICATION_JSON_VALUE)
    List<String> queryAll1ARefNo();

    @GetMapping(path = "/reportable-event/follow-up/reportB/all/refNo", produces =MediaType.APPLICATION_JSON_VALUE)
    List<String> queryAll1BRefNo();

    @GetMapping(path = "/reportable-event/follow-up/reportA/init-data/{refNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupPreviewADto> queryFollowupInfoAByRefNo(@PathVariable("refNo") String refNo);

    @GetMapping(path = "/reportable-event/reportB/init-data/{refNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupPreviewBDto> queryFollowupInfoBByRefNo(@PathVariable("refNo") String refNo);

    @GetMapping(path = "/reportable-event/follow-up/reportB/{refNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupReport1BDto> retrieveFollowupReport1B(@PathVariable("refNo") String refNo);

    @GetMapping(path = "/reportable-event/follow-up/reportA/{refNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupReport1ADto> retrieveFollowupReport1A(@PathVariable("refNo") String refNo);

    @PostMapping(path = "/reportable-event/follow-up/reportA", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewFollowupReport1A(@RequestBody FollowupReport1ADto dto);

    @PostMapping(path = "/reportable-event/follow-up/reportB", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewFollowupReport1B(@RequestBody FollowupReport1BDto dto);

    @PostMapping(path = "/reportable-event/follow-up/form-validation/followup1A", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFollowup1A(@RequestBody Followup1AMetaDto dto);

    @PostMapping(path = "/reportable-event/follow-up/form-validation/followup1B", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFollowup1B(@RequestBody Followup1BMetaDto dto);

    @PostMapping(path = "/reportable-event/follow-up/reportA/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftFollowup1A(@RequestBody FollowupReport1ADto dto);

    @PostMapping(path = "/reportable-event/follow-up/reportB/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftFollowup1B(@RequestBody FollowupReport1BDto dto);

    @GetMapping(path = "/reportable-event/follow-up/report1A/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupReport1ADto> retrieveFollowup1AByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/reportable-event/follow-up/report1B/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupReport1BDto> retrieveFollowup1BByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/reportable-event/follow-up/view/followup1A/{referNo}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Followup1AViewDto> findFollowup1AViewDtoByReferenceNo(@PathVariable("referNo") String referNo);

    @GetMapping(path = "/reportable-event/follow-up/view/followup1B/{referNo}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Followup1BViewDto> findFollowup1BViewDtoByReferenceNo(@PathVariable("referNo") String referNo);
}
