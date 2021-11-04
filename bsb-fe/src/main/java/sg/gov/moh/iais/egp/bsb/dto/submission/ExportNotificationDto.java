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
public class ExportNotificationDto {
    @Data
    @NoArgsConstructor
    public static class ExportList implements Serializable {
        private String scheduleType;
        private String bat;
        private String transferType;
        private String transferQty;
        private String meaUnit;
        private DocumentDto batDocumentDto;
    }

    private List<ExportList> exportLists;
    private DocumentDto documentDto;
    private String receivedFacility;
    private String receivedCountry;
    private String exportDate;
    private String provider;
    private String flightNo;
    private String remarks;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ExportNotificationDto() {
        exportLists = new ArrayList<>();
        exportLists.add(new ExportList());
    }

    public List<ExportList> getExportLists() { return new ArrayList<>(exportLists);
    }

    public void clearExportLists(){
        this.exportLists.clear();
    }

    public void addExportLists(ExportList exportList){
        this.exportLists.add(exportList);
    }

    public void setExportLists(List<ExportList> exportLists) {
        this.exportLists = new ArrayList<>(exportLists);
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
//        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("cerRegFeignClient", "validateFacilityAdmin", new Object[]{exportLists,docsMetaDto});
        return validationResultDto.isPass();
    }


    //----------------------request-->object----------------------------------
//    private static final String SEPARATOR                   = "--v--";
//    private static final String KEY_SECTION_AMT             = "sectionAmt";
//    private static final String KEY_PREFIX_SCHEDULE_TYPE    = "scheduleType";
//    private static final String KEY_PREFIX_BAT         = "bat";
//    private static final String KEY_PREFIX_TRANSFER_TYPE = "transferType";
//    private static final String KEY_PREFIX_TRANSFER_QTY      = "transferQty";
//    private static final String KEY_PREFIX_MEASUREMENT_UNIT = "meaUnit";
//    private static final String KEY_PREFIX_RECEIVED_FACILITY = "receivedFacility";
//    private static final String KEY_PREFIX_RECEIVED_COUNTRY = "receivedCountry";
//    private static final String KEY_PREFIX_EXPORT_DATE = "exportDate";
//    private static final String KEY_PREFIX_PROVIDER = "provider";
//    private static final String KEY_PREFIX_FLIGHT_NO = "flightNo";
//    private static final String KEY_PREFIX_REMARKS = "remarks";


    /**
     * reqObjectMapping
     * get value from request
     * */
    public void reqObjectMapping(HttpServletRequest request){
        int amt = ParamUtil.getInt(request,KEY_SECTION_AMT);
        clearExportLists();

        for (int i = 0; i < amt; i++) {
            ExportList exportList = new ExportList();
            exportList.setScheduleType(ParamUtil.getString(request,KEY_PREFIX_SCHEDULE_TYPE+SEPARATOR+i));
            exportList.setBat(ParamUtil.getString(request,KEY_PREFIX_BAT+ SEPARATOR+i));
            exportList.setTransferType(ParamUtil.getString(request,KEY_PREFIX_TRANSFER_TYPE+ SEPARATOR+i));
            exportList.setTransferQty(ParamUtil.getString(request,KEY_PREFIX_TRANSFER_QTY + SEPARATOR+i));
            exportList.setMeaUnit(ParamUtil.getString(request, KEY_PREFIX_MEASUREMENT_UNIT+ SEPARATOR+i));
            addExportLists(exportList);
        }

    }
}
