package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.TransferNotificationDto;

/**
 * @author YiMing
 * @version 2021/11/4 15:47
 **/

@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "transfer")
public interface TransferClient {

    @PostMapping(path = "/not/transfer/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewTransferNot(@RequestBody TransferNotificationDto notificationDto);

    @PostMapping(path = "/not/transfer/validate/orgProfile", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateTransferNot(@RequestBody TransferNotificationDto dto);
}
