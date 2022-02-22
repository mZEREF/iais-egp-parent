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
import sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.*;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.view.InvestViewDto;

import java.util.List;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "investRep")
public interface IncidentInvestigationReportClient {

    @PostMapping(path = "/incident/investigation/draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftInvestigationReport(@RequestBody InvestReportDto dto);

    @GetMapping(path = "/incident/investigation/query/{refNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    IncidentDto queryIncidentByRefNo(@PathVariable("refNo") String refNo);

    @GetMapping(path = "/incident/investigation/query/all/refNo", produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> queryAllRefNo();

    @PostMapping(path = "/incident/investigation", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewIncidentInvest(@RequestBody InvestReportDto dto);

    @PostMapping(path = "/incident/investigation/form-validation/selection", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateReferNoSelectionDto(@RequestBody ReferNoSelectionDto dto);

    @PostMapping(path = "/incident/investigation/form-validation/incident-info", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateIncidentInfo(@RequestBody IncidentInfoDto dto);

    @PostMapping(path = "/incident/investigation/form-validation/incident-investigation", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateIncidentInvestDto(@RequestBody IncidentInvestDto dto);

    @PostMapping(path = "/incident/investigation/form-validation/medical-investigation", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateMedicalInvestDto(@RequestBody MedicalInvestDto dto);

    @PostMapping(path = "/incident/investigation/form-validation/docs", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validatePrimaryDoc(@RequestBody PrimaryDocDto.DocsMetaDto dto);

    @GetMapping(path = "/incident/investigation/view/{referNo}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InvestViewDto> findInvestViewDtoByReferenceNo(@PathVariable("referNo") String referNo);
}
