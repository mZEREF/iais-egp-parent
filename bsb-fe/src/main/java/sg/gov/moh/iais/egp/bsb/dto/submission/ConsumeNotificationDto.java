package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.*;
/**
 * @author Zhu Tangtang
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ConsumeNotificationDto {
    @Data
    @NoArgsConstructor
    public static class ConsumptionList implements Serializable {
        private String scheduleType;
        private String bat;
        private String consumeType;
        private String consumedQty;
        private String meaUnit;
        private DocumentDto batDocumentDto;
    }

    private List<ConsumptionList> consumptionLists;
    private DocumentDto documentDto;
    private String remarks;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ConsumeNotificationDto() {
        consumptionLists = new ArrayList<>();
        consumptionLists.add(new ConsumptionList());
    }

    public List<ConsumptionList> getConsumptionLists() { return new ArrayList<>(consumptionLists);
    }

    public void clearConsumptionLists(){
        this.consumptionLists.clear();
    }

    public void addConsumptionLists(ConsumptionList consumptionLists){
        this.consumptionLists.add(consumptionLists);
    }

    public void setConsumptionLists(List<ConsumptionList> consumptionLists) {
        this.consumptionLists = new ArrayList<>(consumptionLists);
    }

    public DocumentDto getDocumentDto() {
        return documentDto;
    }

    public void setDocumentDto(DocumentDto documentDto) {
        this.documentDto = documentDto;
    }

    public boolean doValidation() {
        List<DocumentDto.DocMeta> docsMetaDto = null;
        if(documentDto != null){
            docsMetaDto = documentDto.getMetaDtoList();
        }
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("cerRegFeignClient", "validateFacilityAdmin", new Object[]{consumptionLists,docsMetaDto});
        return validationResultDto.isPass();
    }


    //----------------------request-->object----------------------------------
//    private static final String SEPARATOR                   = "--v--";
//    private static final String KEY_SECTION_AMT             = "sectionAmt";
//    private static final String KEY_PREFIX_SCHEDULE_TYPE    = "scheduleType";
//    private static final String KEY_PREFIX_BAT         = "bat";
//    private static final String KEY_PREFIX_CONSUME_TYPE = "consumeType";
//    private static final String KEY_PREFIX_CONSUME_QTY      = "consumedQty";
//    private static final String KEY_PREFIX_MEASUREMENT_UNIT = "meaUnit";
//    public static final String KEY_PREFIX_REMARKS = "remarks";


    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        int amt = ParamUtil.getInt(request, KEY_SECTION_AMT);
        clearConsumptionLists();

        for (int i = 0; i < amt; i++) {
            ConsumptionList consumptionList = new ConsumptionList();
            consumptionList.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_SCHEDULE_TYPE+SEPARATOR+i));
            consumptionList.setBat(ParamUtil.getString(request,KEY_PREFIX_BAT+SEPARATOR+i));
            consumptionList.setConsumeType(ParamUtil.getString(request,KEY_PREFIX_CONSUME_TYPE+SEPARATOR+i));
            consumptionList.setConsumedQty(ParamUtil.getString(request,KEY_PREFIX_CONSUME_QTY +SEPARATOR+i));
            consumptionList.setMeaUnit(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+SEPARATOR+i));
            addConsumptionLists(consumptionList);
        }

    }
}
