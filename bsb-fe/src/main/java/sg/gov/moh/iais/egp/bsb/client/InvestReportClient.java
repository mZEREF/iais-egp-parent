package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.IncidentDto;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.InvestReportDto;

import java.util.List;


/**
 * @author YiMing
 * @version 2021/12/20 10:41
 **/
@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "investRep")
public interface InvestReportClient {
    @GetMapping(path = "/invest/query/{refNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    IncidentDto queryIncidentByRefNo(@PathVariable("refNo") String refNo);

    @GetMapping(path = "/invest/query/all/refNo", produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> queryAllRefNo();

    @PostMapping(path = "/invest/save/incidentInvest", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewIncidentInvest(@RequestBody InvestReportDto dto);
}
