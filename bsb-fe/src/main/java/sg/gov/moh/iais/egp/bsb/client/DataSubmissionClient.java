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
    @GetMapping(value = "/facList/getFacList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacListDto> queryAllApprovalFacList();

    @GetMapping(value = "/facList/getAll", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FacListDto.ReceiptFacility> queryAllFacility();

    @PostMapping(value = "/dataSubmission/saveConsumeNot", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveConsumeNot(@RequestBody ConsumeNotificationDto.ConsumeNotNeedR consumeNotNeedR);

    @PostMapping(value = "/dataSubmission/saveDisposalNot", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveDisposalNot(@RequestBody DisposalNotificationDto.DisposalNotNeedR disposalNotNeedR);

    @PostMapping(value = "/dataSubmission/saveExportNot", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveExportNot(@RequestBody ExportNotificationDto.ExportNotNeedR exportNotNeedR);

    @PostMapping(value = "/dataSubmission/saveReceiptNot", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveReceiptNot(@RequestBody ReceiptNotificationDto.ReceiptNotNeedR receiptNotNeedR);

    @PostMapping(path = "/dataSubmission/validate/conNot", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateConsumeNot(@RequestBody ConsumeNotificationDto.ConsumeNotNeedR consumeNotNeedR);

    @PostMapping(path = "/dataSubmission/validate/disNot", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateDisposalNot(@RequestBody DisposalNotificationDto.DisposalNotNeedR disposalNotNeedR);

    @PostMapping(path = "/dataSubmission/validate/expNot", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateExportNot(@RequestBody ExportNotificationDto.ExportNotNeedR exportNotNeedR);

    @PostMapping(path = "/dataSubmission/validate/recNot", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateReceiptNot(@RequestBody ReceiptNotificationDto.ReceiptNotNeedR receiptNotNeedR);

    @GetMapping(value = "/dataSubmission/view/{id}")
    ResponseDto<DataSubmissionInfo> getDataSubmissionInfo(@PathVariable(name = "id") String id);

    @PostMapping(value = "/dataSubmission/draft/consume", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftConsume(@RequestBody ConsumeNotificationDto.ConsumeNotNeedR consumeNotNeedR);

    @PostMapping(value = "/dataSubmission/draft/disposal", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftDisposal(@RequestBody DisposalNotificationDto.DisposalNotNeedR disposalNotNeedR);

    @PostMapping(value = "/dataSubmission/draft/export", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftExport(@RequestBody ExportNotificationDto.ExportNotNeedR exportNotNeedR);

    @PostMapping(value = "/dataSubmission/draft/receipt", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftReceipt(@RequestBody ReceiptNotificationDto.ReceiptNotNeedR receiptNotNeedR);

    @GetMapping(value = "/dataSubmission/draft/getDraftDto", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DraftDto> getDraftDto(@RequestParam("appId") String appId);
}
