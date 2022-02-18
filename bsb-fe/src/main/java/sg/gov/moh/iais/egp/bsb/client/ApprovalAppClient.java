package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.*;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.entity.FacilityActivity;

import java.util.Collection;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/10/13
 */
@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "approvalApp")
public interface ApprovalAppClient {

    @PostMapping(path = "/approvalApp/approvalToPossess/validate/activity", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateActivity(@RequestBody ActivityDto dto);

    @PostMapping(path = "/approvalApp/approvalToPossess/validate/approvalProfile", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovalProfile(@RequestBody ApprovalProfileDto dto);

    @PostMapping(path = "/approvalApp/approvalToPossess/validate/primaryDocs", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateApprovalPrimaryDocs(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @GetMapping(path = "/approvalApp/approvalToPossess/application/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalAppDto> getApprovalAppAppDataByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/approvalApp/approvalToPossess/facDoc/{facilityId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Collection<DocRecordInfo>> getFacDocByFacId(@PathVariable("facilityId") String facilityId);

    @GetMapping(path = "/approvalApp/approvalToPossess/getAllApprovalFac", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<Facility>> getAllMainActApprovalFac();

    @GetMapping(path = "/approvalApp/approvalToPossess/getApprovalFAByFacId/{facilityId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FacilityActivity>> getApprovalFAByFacId(@PathVariable("facilityId") String facilityId);

    @GetMapping(path = "/approvalApp/approvalToPossess/getBiologicalBySchedule/{schedule}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<Biological>> getBiologicalBySchedule(@PathVariable("schedule") String schedule);

    @PostMapping(path = "/approvalApp/approvalToPossess/application", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewApprovalApp(@RequestBody ApprovalAppDto approvalAppDto);

    @PostMapping(path = "/approvalApp/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveApprovalAppDraft(@RequestBody ApprovalAppDto dto);
    /*******************RFC********************/
    @GetMapping(path = "/approvalApp/approvalToPossess/rfc/application/{approvalId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApprovalAppDto> getApprovalAppAppDataByApprovalId(@PathVariable("approvalId") String approvalId);

    @PostMapping(path = "/approvalApp/approvalToPossess/rfc/amendment/saveApprovalApp", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveAmendmentApprovalApp(@RequestBody ApprovalAppDto approvalAppDto);
}
