package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.DraftDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.*;

/**
 * @author Zhu Tangtang
 */
@FeignClient(name = "bsb-fe-api", configuration = FeignConfiguration.class, contextId = "dataSubmission")
public interface DataSubmissionClient {
    @GetMapping(value = "/facility-info/getFacList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacListDto> queryAllApprovalFacList();

    @GetMapping(value = "/facList/getAll", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacListDto.ReceiptFacility> queryAllFacility();

    @PostMapping(value = "/data-submission/consumption", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveConsumeNot(@RequestBody ConsumeNotificationDto.ConsumeNotNeedR consumeNotNeedR);

    @PostMapping(value = "/data-submission/disposal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDisposalNot(@RequestBody DisposalNotificationDto.DisposalNotNeedR disposalNotNeedR);

    @PostMapping(value = "/data-submission/export", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveExportNot(@RequestBody ExportNotificationDto.ExportNotNeedR exportNotNeedR);

    @PostMapping(value = "/data-submission/receipt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveReceiptNot(@RequestBody ReceiptNotificationDto.ReceiptNotNeedR receiptNotNeedR);

    @PostMapping(path = "/data-submission/form-validation/consumption", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateConsumeNot(@RequestBody ConsumeNotificationDto.ConsumeNotNeedR consumeNotNeedR);

    @PostMapping(path = "/data-submission/form-validation/disposal", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDisposalNot(@RequestBody DisposalNotificationDto.DisposalNotNeedR disposalNotNeedR);

    @PostMapping(path = "/data-submission/form-validation/export", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateExportNot(@RequestBody ExportNotificationDto.ExportNotNeedR exportNotNeedR);

    @PostMapping(path = "/data-submission/form-validation/receipt", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateReceiptNot(@RequestBody ReceiptNotificationDto.ReceiptNotNeedR receiptNotNeedR);

    @GetMapping(value = "/data-submission/view/{id}")
    ResponseDto<DataSubmissionInfo> getDataSubmissionInfo(@PathVariable(name = "id") String id);

    @PostMapping(value = "/data-submission/draft/consumption", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftConsume(@RequestBody ConsumeNotificationDto.ConsumeNotNeedR consumeNotNeedR);

    @PostMapping(value = "/data-submission/draft/disposal", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftDisposal(@RequestBody DisposalNotificationDto.DisposalNotNeedR disposalNotNeedR);

    @PostMapping(value = "/data-submission/draft/export", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftExport(@RequestBody ExportNotificationDto.ExportNotNeedR exportNotNeedR);

    @PostMapping(value = "/data-submission/draft/receipt", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftReceipt(@RequestBody ReceiptNotificationDto.ReceiptNotNeedR receiptNotNeedR);

    @GetMapping(value = "/data-submission/draft/getDraftDto", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DraftDto> getDraftDto(@RequestParam("appId") String appId);
}
