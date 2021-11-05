package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.*;
/**
 * @author Zhu Tangtang
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptNotificationDto {
    @Data
    @NoArgsConstructor
    public static class ReceiptList implements Serializable {
        private String scheduleType;
        private String bat;
        private String receiveQty;
        private String meaUnit;
        private DocumentDto batDocumentDto;
    }

    private List<ReceiptList> receiptLists;
    private DocumentDto documentDto;
    private String modeProcurement;
    private String sourceFacilityName;
    private String sourceFacilityAddress;
    private String sourceFacilityContactPerson;
    private String contactPersonEmail;
    private String contactPersonTel;
    private String provider;
    private String flightNo;
    private String actualArrivalDate;
    private String actualArrivalTime;
    private String remarks;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ReceiptNotificationDto() {
        receiptLists = new ArrayList<>();
        receiptLists.add(new ReceiptList());
    }

    public List<ReceiptList> getReceiptLists() { return new ArrayList<>(receiptLists);
    }

    public void clearReceiptLists(){
        this.receiptLists.clear();
    }

    public void addReceiptLists(ReceiptList receiptLists){
        this.receiptLists.add(receiptLists);
    }

    public void setConsumptionLists(List<ReceiptList> receiptLists) {
        this.receiptLists = new ArrayList<>(receiptLists);
    }

    public DocumentDto getDocumentDto() {
        return documentDto;
    }

    public void setDocumentDto(DocumentDto documentDto) {
        this.documentDto = documentDto;
    }

    public boolean doValidation() {
//        List<DocumentDto.DocMeta> docsMetaDto = null;
//        if(documentDto != null){
//            docsMetaDto = documentDto.getMetaDtoList();
//        }
//        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("cerRegFeignClient", "validateFacilityAdmin", new Object[]{receiptLists,docsMetaDto});
        return validationResultDto.isPass();
    }


    //----------------------request-->object----------------------------------
//    private static final String SEPARATOR                   = "--v--";
//    private static final String KEY_SECTION_AMT             = "sectionAmt";
//    private static final String KEY_PREFIX_SCHEDULE_TYPE    = "scheduleType";
//    private static final String KEY_PREFIX_BAT         = "bat";
//    private static final String KEY_PREFIX_RECEIVE_QTY = "receivedQty";
//    private static final String KEY_PREFIX_MEASUREMENT_UNIT = "meaUnit";
//    private static final String KEY_PREFIX_MODE_PROCUREMENT      = "modeProcurement";
//    private static final String KEY_PREFIX_SOURCE_FACILITY_NAME = "sourceFacilityName";
//    private static final String KEY_PREFIX_SOURCE_FACILITY_ADDRESS = "sourceFacilityAddress";
//    private static final String KEY_PREFIX_SOURCE_FACILITY_CONTACT_PERSON = "sourceFacilityContactPerson";
//    private static final String KEY_PREFIX_CONTACT_PERSON_EMAIL = "contactPersonEmail";
//    private static final String KEY_PREFIX_CONTACT_PERSON_TEL = "contactPersonTel";
//    private static final String KEY_PREFIX_PROVIDER = "provider";
//    private static final String KEY_PREFIX_FLIGHT_NO = "flightNo";
//    private static final String KEY_PREFIX_ACTUAL_ARRIVAL_DATE = "actualArrivalDate";
//    private static final String KEY_PREFIX_ACTUAL_ARRIVAL_TIME = "actualArrivalTime";
//    private static final String KEY_PREFIX_REMARKS = "remarks";


    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        int amt = ParamUtil.getInt(request,KEY_SECTION_AMT);
        clearReceiptLists();

        for (int i = 0; i < amt; i++) {
            ReceiptList ReceiptList = new ReceiptList();
            ReceiptList.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_SCHEDULE_TYPE+SEPARATOR+i));
            ReceiptList.setBat(ParamUtil.getString(request,KEY_PREFIX_BAT+SEPARATOR+i));
            ReceiptList.setReceiveQty(ParamUtil.getString(request,KEY_PREFIX_RECEIVE_QTY+SEPARATOR+i));
            ReceiptList.setMeaUnit(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+SEPARATOR+i));
            addReceiptLists(ReceiptList);
        }

    }
}
