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
public class DisposalNotificationDto {
    @Data
    @NoArgsConstructor
    public static class DisposalList implements Serializable {
        private String scheduleType;
        private String bat;
        private String disposedQty;
        private String meaUnit;
    }

    private List<DisposalList> disposalLists;
    private String destructMethod;
    private String destructDetails;
    private String remarks;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public DisposalNotificationDto() {
        disposalLists = new ArrayList<>();
        disposalLists.add(new DisposalList());
    }

    public List<DisposalList> getDisposalLists() { return new ArrayList<>(disposalLists);
    }

    public void clearDisposalLists(){
        this.disposalLists.clear();
    }

    public void addDisposalLists(DisposalList receiptLists){
        this.disposalLists.add(receiptLists);
    }

    public void setDisposalLists(List<DisposalList> receiptLists) {
        this.disposalLists = new ArrayList<>(receiptLists);
    }


    public boolean doValidation() {
//        List<DocumentDto.DocMeta> docsMetaDto = null;
//        if(documentDto != null){
//            docsMetaDto = documentDto.getMetaDtoList();
//        }
//        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("cerRegFeignClient", "validateFacilityAdmin", new Object[]{disposalLists,docsMetaDto});
        return validationResultDto.isPass();
    }



    //----------------------request-->object----------------------------------
//    private static final String SEPARATOR                   = "--v--";
//    private static final String KEY_SECTION_AMT             = "sectionAmt";
//    private static final String KEY_PREFIX_SCHEDULE_TYPE    = "scheduleType";
//    private static final String KEY_PREFIX_BAT         = "bat";
//    private static final String KEY_PREFIX_DISPOSE_QTY = "disposedQty";
//    private static final String KEY_PREFIX_MEASUREMENT_UNIT = "meaUnit";
//    private static final String KEY_PREFIX_DESTRUCT_METHOD = "destructMethod";
//    private static final String KEY_PREFIX_DESTRUCT_DETAILS = "destructDetails";
//    private static final String KEY_PREFIX_REMARKS = "remarks";


    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        int amt = ParamUtil.getInt(request,KEY_SECTION_AMT);
        clearDisposalLists();

        for (int i = 0; i < amt; i++) {
            DisposalList disposalList = new DisposalList();
            disposalList.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_SCHEDULE_TYPE+SEPARATOR+i));
            disposalList.setBat(ParamUtil.getString(request,KEY_PREFIX_BAT+SEPARATOR+i));
            disposalList.setDisposedQty(ParamUtil.getString(request,KEY_PREFIX_DISPOSE_QTY+SEPARATOR+i));
            disposalList.setMeaUnit(ParamUtil.getString(request,KEY_PREFIX_MEASUREMENT_UNIT+ SEPARATOR+i));
            addDisposalLists(disposalList);
        }

    }
}
