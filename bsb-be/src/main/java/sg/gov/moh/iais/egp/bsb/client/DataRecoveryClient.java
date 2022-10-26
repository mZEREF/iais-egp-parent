package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.datarecovery.DataRecoverySearchDto;
import sg.gov.moh.iais.egp.bsb.dto.datarecovery.DataRecoverySearchResultDto;

@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class)
public interface DataRecoveryClient {
    @GetMapping(value = "/data-recovery", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DataRecoverySearchResultDto> searchDataRecoveryList(@SpringQueryMap DataRecoverySearchDto dto);

    @PostMapping(value = "/data-recovery/{id}")
    String recoverDataById(@PathVariable("id") String id);
}
