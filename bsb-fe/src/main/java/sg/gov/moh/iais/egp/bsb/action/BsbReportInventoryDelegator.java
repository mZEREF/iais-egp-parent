package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.FacListDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.ReportInventoryDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.DataSubmissionConstants.KEY_FAC_LISTS;

/**
 * @author YiMing
 * @version 2021/11/10 9:37
 **/
@Slf4j
@Delegator("reportInventoryDelegator")
public class BsbReportInventoryDelegator {
    private static final String DTO_BSB_REPORT_INVENTORY = "repReportDto";
    private static final String KEY_FACILITY_INFO = "facilityInfo";
    private static final String KEY_OTHERS = "others";
    private final BsbSubmissionCommon subCommon;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private final DataSubmissionClient submissionClient;

    public BsbReportInventoryDelegator(BsbSubmissionCommon subCommon, FileRepoClient fileRepoClient, BsbFileClient bsbFileClient, DataSubmissionClient submissionClient) {
        this.subCommon = subCommon;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.submissionClient = submissionClient;
    }


    public void step1(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO, null);
        AuditTrailHelper.auditFunction("Data Submission", "Data Submission");
    }


    public void preSelfFacSelect(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        selectOption(request);
    }

    public void preSwitch0(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,null);
        String facId = ParamUtil.getRequestString(request,KEY_FAC_ID);
        facId = MaskUtil.unMaskValue("id",facId);
        ParamUtil.setSessionAttr(request,KEY_FAC_ID,facId);
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
        ParamUtil.setSessionAttr(request,KEY_SUBMISSION_TYPE,KEY_DATA_SUBMISSION_TYPE_BAT_INVENTORY);
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
        List<NewFileSyncDto> newFilesToSync = null;
        if (!inventoryDto.getNewDocMap().isEmpty()) {
            MultipartFile[] files = inventoryDto.getNewDocMap().values().stream().map(ReportInventoryDto.NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            //change newFile to savedFile
            newFilesToSync = inventoryDto.newFileSaved(repoIds);
        }
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
        submissionClient.saveNewReportAndInventory(saveDocDto);

        try {
            // sync files to BE file-repo (save new added files, delete useless files)
            if (newFilesToSync!= null && !newFilesToSync.isEmpty()) {
                /* Ignore the failure of sync files currently.
                 * We should add a mechanism to retry synchronization of files in the future */
                FileRepoSyncDto syncDto = new FileRepoSyncDto();
                syncDto.setNewFiles(newFilesToSync);
                bsbFileClient.saveFiles(syncDto);
            }
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
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

    /**
     * This method is used to query all Facility info
     */
    private void selectOption(HttpServletRequest request) {
        ParamUtil.setSessionAttr(request,KEY_FAC_LISTS,null);
        FacListDto facListDto = submissionClient.queryAllApprovalFacList().getEntity();
        List<FacListDto.FacList> facLists = facListDto.getFacLists();
        //Removes the newly created object where is null
        facLists.remove(0);
        List<SelectOption> selectModel = new ArrayList<>(facLists.size());
        for (FacListDto.FacList fac : facLists) {
            selectModel.add(new SelectOption(MaskUtil.maskValue("id",fac.getFacId()), fac.getFacName()));
        }
        ParamUtil.setRequestAttr(request, KEY_FAC_SELECTION, selectModel);
        //Put in session called for later operations
        ParamUtil.setSessionAttr(request,KEY_FAC_LISTS,(Serializable) facLists);
    }

}
