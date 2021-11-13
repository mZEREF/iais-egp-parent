package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.TransferClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.submission.FacListDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.ReportInventoryDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.TransferNotificationDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_ACTION_TYPE;

/**
 * @author YiMing
 * @version 2021/11/10 9:37
 **/
@Slf4j
@Delegator(value = "reportInventoryDelegator")
public class BsbReportInventoryDelegator {
    private static final String DTO_BSB_REPORT_INVENTORY = "repReportDto";
    private static final String KEY_FACILITY_INFO = "facilityInfo";
    private static final String KEY_OTHERS = "others";
    private final BsbSubmissionCommon subCommon;
    private final TransferClient transferClient;
    private final FileRepoClient fileRepoClient;

    public BsbReportInventoryDelegator(BsbSubmissionCommon subCommon, TransferClient transferClient, FileRepoClient fileRepoClient) {
        this.subCommon = subCommon;
        this.transferClient = transferClient;
        this.fileRepoClient = fileRepoClient;
    }


    public void step1(BaseProcessClass bpc){

    }


    public void preFacilityInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ReportInventoryDto inventoryDto = getReportInventoryDto(request);
        FacListDto.FacList facList = subCommon.getFacInfo(request);
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,facList);
        ParamUtil.setRequestAttr(request,"doSettings",getDocSettingMap());
        ParamUtil.setRequestAttr(request,DTO_BSB_REPORT_INVENTORY,inventoryDto);
        Map<String, List<ReportInventoryDto.NewDocInfo>> newFiles =  inventoryDto.getNewDocTypeMap();
        String reportType = inventoryDto.getReportType();
        if(StringUtils.hasLength(reportType)){
            List<ReportInventoryDto.NewDocInfo> reportNewFile = newFiles.get(inventoryDto.getReportType());
            ParamUtil.setRequestAttr(request,"reportNewFile",reportNewFile);
            List<ReportInventoryDto.NewDocInfo> othersNewFile = newFiles.get(KEY_OTHERS);
            ParamUtil.setRequestAttr(request,"othersNewFile",othersNewFile);
        }
        ParamUtil.setRequestAttr(request,"newFiles", newFiles);
        //when back do data showing in page
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        //show error message
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,inventoryDto.retrieveValidationResult());
        }
    }

    public void preSwitch(BaseProcessClass bpc){
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String actionType = request.getParameter(KEY_ACTION_TYPE);
        ParamUtil.setSessionAttr(bpc.request, KEY_ACTION_TYPE, actionType);
    }


    public void preSubmitDoc(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ReportInventoryDto inventoryDto = getReportInventoryDto(request);
        inventoryDto.reqObjMapping(request);
        //begin to validate
        doValidation(inventoryDto,request);
        ParamUtil.setSessionAttr(request,DTO_BSB_REPORT_INVENTORY,inventoryDto);
        ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.YES);
        Map<String, List<ReportInventoryDto.NewDocInfo>> newFiles =  inventoryDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request,KEY_FACILITY_INFO,subCommon.getFacInfo(request));
        ParamUtil.setRequestAttr(request,"newFiles", newFiles);
        ParamUtil.setRequestAttr(request,"reportType",inventoryDto.getReportType());
    }

    public void saveReport(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ReportInventoryDto inventoryDto = getReportInventoryDto(request);
        //write a method to save to db
        //save file to db and get repoId
        MultipartFile[] files = inventoryDto.getNewDocMap().values().stream().map(ReportInventoryDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
        List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
        //change newFile to savedFile
        inventoryDto.newFileSaved(repoIds);
        //put need value to SaveDocDto
        ReportInventoryDto.SaveDocDto saveDocDto = new ReportInventoryDto.SaveDocDto();
        saveDocDto.setReportType(inventoryDto.getReportType());
        FacListDto.FacList facInfo = subCommon.getFacInfo(request);
        if(facInfo != null){
            saveDocDto.setFacId(facInfo.getFacId());
        }else{
            if(log.isInfoEnabled()){
                log.info("Facility id is empty");
            }
        }
        saveDocDto.setSavedDocInfo(new ArrayList<>(inventoryDto.getSavedDocMap().values()));
        transferClient.saveNewReportAndInventory(saveDocDto);
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
        settingMap.put(KEY_OTHERS,new DocSetting(DocConstants.DOC_TYPE_OTHERS,KEY_OTHERS,false));
        return settingMap;
    }

    /**
     * just a method to do simple valid,maybe update in the future
     * doValidation
     * */
    private void doValidation(ReportInventoryDto dto, HttpServletRequest request){
        if(dto.doValidation()){
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.YES);
        }else{
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.NO);
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH,Boolean.TRUE);
        }
    }

}
