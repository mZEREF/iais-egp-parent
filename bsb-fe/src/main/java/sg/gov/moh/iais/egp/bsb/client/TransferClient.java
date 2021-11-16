package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.ReportInventoryDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.TransferNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.TransferRequestDto;


/**
 * @author YiMing
 * @version 2021/11/4 15:47
 **/

@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "transfer")
public interface TransferClient {

    @PostMapping(path = "/transfer/not/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewTransferNot(@RequestBody TransferNotificationDto.TransferNotNeedR transferNotNeedR);

    @PostMapping(path = "/transfer/validate/trNot", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateTransferNot(@RequestBody TransferNotificationDto.TransferNotNeedR transferNotNeedR);

    @PostMapping(path = "/transfer/report/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewReportAndInventory(@RequestBody ReportInventoryDto.SaveDocDto saveDocDto);

    @PostMapping(path = "/transfer/validate/report", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateReportAndInventory(@RequestBody ReportInventoryDto.DocsMetaDto dto);

    @PostMapping(path = "/transfer/validate/req", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateRequestTransfer(@RequestBody TransferRequestDto dto);

    @PostMapping(path = "/transfer/req/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveTransferReceipt(@RequestBody TransferRequestDto dto);

    @PostMapping(path = "/transfer/validate/ack", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateTransferReceipt(@RequestBody TransferRequestDto dto);

    @PostMapping(path = "/transfer/ack/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveRequestTransfer(@RequestBody TransferRequestDto dto);



}
