package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.*;

/**
 * @author Zhu Tangtang
 */
@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class, contextId = "dataSubmission")
public interface DataSubmissionClient {
    @GetMapping(value = "/facility-info/getFacList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacListDto> queryAllApprovalFacList();

    @PostMapping(value = "/data-submission/consumption", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveConsumeNot(@RequestBody ConsumeNotificationDto.ConsumeDto consumeDto);

    @PostMapping(value = "/data-submission/disposal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDisposalNot(@RequestBody DisposalNotificationDto.DisposalDto disposalDto);

    @PostMapping(value = "/data-submission/export", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveExportNot(@RequestBody ExportNotificationDto.ExportDto exportDto);

    @PostMapping(value = "/data-submission/receipt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveReceiptNot(@RequestBody ReceiptNotificationDto.ReceiptDto receiptDto);

    @PostMapping(path = "/data-submission/form-validation/consumption", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateConsumeNot(@RequestBody ConsumeNotificationDto.ConsumeDto consumeDto);

    @PostMapping(path = "/data-submission/form-validation/disposal", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDisposalNot(@RequestBody DisposalNotificationDto.DisposalDto disposalDto);

    @PostMapping(path = "/data-submission/form-validation/export", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateExportNot(@RequestBody ExportNotificationDto.ExportDto exportDto);

    @PostMapping(path = "/data-submission/form-validation/receipt", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateReceiptNot(@RequestBody ReceiptNotificationDto.ReceiptDto receiptDto);

    @GetMapping(value = "/data-submission/view/{id}")
    ResponseDto<DataSubmissionInfo> getDataSubmissionInfo(@PathVariable(name = "id") String id);

    @PostMapping(value = "/data-submission/draft/consumption", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftConsume(@RequestBody ConsumeNotificationDto.ConsumeDto consumeDto);

    @PostMapping(value = "/data-submission/draft/disposal", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftDisposal(@RequestBody DisposalNotificationDto.DisposalDto disposalDto);

    @PostMapping(value = "/data-submission/draft/export", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftExport(@RequestBody ExportNotificationDto.ExportDto exportDto);

    @PostMapping(value = "/data-submission/draft/receipt", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftReceipt(@RequestBody ReceiptNotificationDto.ReceiptDto receiptDto);

    @PostMapping(path = "/data-submission/transfer/notification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewTransferNotification(@RequestBody TransferNotificationDto.TransferDto transferDto);

    @PostMapping(path = "/data-submission/form-validation/transfer/notification", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateTransferNotification(@RequestBody TransferNotificationDto.TransferDto transferDto);

    @PostMapping(path = "/data-submission/report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewReportAndInventory(@RequestBody ReportInventoryDto.SaveDocDto saveDocDto);

    @PostMapping(path = "/data-submission/form-validation/report", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateReportAndInventory(@RequestBody ReportInventoryDto.DocsMetaDto dto);

    @PostMapping(path = "/data-submission/form-validation/transfer/request", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateRequestTransfer(@RequestBody TransferRequestDto dto);

    @PostMapping(path = "/data-submission/transfer/request", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveRequestTransfer(@RequestBody TransferRequestDto dto);

    @PostMapping(value = "/data-submission/draft/transfer/request", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftRequestTransfer(@RequestBody TransferRequestDto dto);

    @PostMapping(path = "/data-submission/form-validation/transfer/receipt", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateTransferReceipt(@RequestBody AckTransferReceiptDto.AckTransferReceiptMeta dto);

    @PostMapping(path = "/data-submission/transfer/receipt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveTransferReceipt(@RequestBody AckTransferReceiptDto.AckTransferReceiptSaved dto);

    @GetMapping(path = "/data-submission/transfer/receipt/{facId}",consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AckTransferReceiptDto> getReceiptDataSubNoMap(@PathVariable("facId") String facId);

    @PostMapping(value = "/data-submission/draft/transfer/notification", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftTransferNotification(@RequestBody TransferNotificationDto.TransferDto transferDto);

    @PostMapping(path = "/data-submission/draft/transfer/receipt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    String saveTransferReceiptDraft(@RequestBody AckTransferReceiptDto.AckTransferReceiptSaved saved);
}
