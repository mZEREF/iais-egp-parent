package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.submission.ReportInventoryDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YiMing
 * @version 2021/11/10 9:37
 **/
@Delegator(value = "reportInventoryDelegator")
public class BsbReportInventoryDelegator {
    private static final String DTO_BSB_REPORT_INVENTORY = "repReportDto";

    /**
     * step1
     * */
    public void step1(BaseProcessClass bpc){

    }

    /**
     * prepare
     * */
    public void prepare(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ReportInventoryDto inventoryDto = getReportInventoryDto(request);
        ParamUtil.setRequestAttr(request,"doSettings",getDocSettingMap());
        //show error message
    }

    /**
     * preSubmitFile
     * */
    public void preSubmitFile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ReportInventoryDto inventoryDto = getReportInventoryDto(request);
        inventoryDto.reqObjMapping(request);
        //begin to validate
        ParamUtil.setSessionAttr(request,DTO_BSB_REPORT_INVENTORY,inventoryDto);
        ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.YES);

    }

    /**
     * doSubmit
     * */
    public void doSubmit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ReportInventoryDto inventoryDto = getReportInventoryDto(request);
        //write a method to save to db
    }

    public ReportInventoryDto getReportInventoryDto(HttpServletRequest request){
       ReportInventoryDto reportInventoryDto = (ReportInventoryDto) ParamUtil.getSessionAttr(request,DTO_BSB_REPORT_INVENTORY);
       return reportInventoryDto == null?getDefaultDto():reportInventoryDto;
    }

    public ReportInventoryDto getDefaultDto(){
        return new ReportInventoryDto();
    }

    /**
     *a way to get default display in jsp
     * getDocSettingMap
     * @return Map<String,DocSetting>
     * */
    private Map<String, DocSetting> getDocSettingMap(){
        Map<String,DocSetting> settingMap = new HashMap<>();
        settingMap.put("report",new DocSetting(DocConstants.DOC_REPORT_UPLOAD,"report",true));
        settingMap.put("others",new DocSetting(DocConstants.DOC_TYPE_OTHERS,"others",false));
        return settingMap;
    }

}
