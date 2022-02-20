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
import sg.gov.moh.iais.egp.bsb.dto.submission.*;


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
    ResponseDto<String> saveRequestTransfer(@RequestBody TransferRequestDto dto);

    @PostMapping(value = "/transfer/draft/request", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftRequestTransfer(@RequestBody TransferRequestDto dto);

    @PostMapping(path = "/transfer/validate/ack", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateTransferReceipt(@RequestBody AckTransferReceiptDto.AckTransferReceiptMeta dto);

    @PostMapping(path = "/transfer/ack/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveTransferReceipt(@RequestBody AckTransferReceiptDto.AckTransferReceiptSaved dto);

    @GetMapping(path = "/transfer/get/{facId}",consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AckTransferReceiptDto> getReceiptDataSubNoMap(@PathVariable("facId") String facId);

    @PostMapping(value = "/transfer/draft/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftTransfer(@RequestBody TransferNotificationDto.TransferNotNeedR transferNotNeedR);

    @PostMapping(path = "/transfer/draft/receipt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    String saveTransferReceiptDraft(@RequestBody AckTransferReceiptDto.AckTransferReceiptSaved saved);
}
