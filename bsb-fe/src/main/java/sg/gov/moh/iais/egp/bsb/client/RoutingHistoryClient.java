package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;

import java.util.List;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class)
public interface RoutingHistoryClient {
    @GetMapping(path = "/routing-history-info/process-history/{applicationNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<List<ProcessHistoryDto>> getRoutingHistoryListByAppNo(@PathVariable("applicationNo") String applicationNo);
}
