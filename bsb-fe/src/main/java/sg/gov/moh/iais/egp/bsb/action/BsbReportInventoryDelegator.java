package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.TransferClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.submission.FacListDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.ReportInventoryDto;
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
    private static final String KEY_FAC_LIST_DTO = "facListDto";
    private static final String KEY_FACILITY_INFO = "facilityInfo";
    private final BsbSubmissionCommon subCommon;
    private final TransferClient transferClient;
    private final FileRepoClient fileRepoClient;

    public BsbReportInventoryDelegator(BsbSubmissionCommon subCommon, TransferClient transferClient, FileRepoClient fileRepoClient) {
        this.subCommon = subCommon;
        this.transferClient = transferClient;
        this.fileRepoClient = fileRepoClient;
    }

    /**
     * step1
     * */
    public void step1(BaseProcessClass bpc){

    }

    /**
     * preFacilityInfo
     * */
    public void preFacilityInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ReportInventoryDto inventoryDto = getReportInventoryDto(request);
        FacListDto facListDto = subCommon.getFacListDto(request);
        ParamUtil.setSessionAttr(request,KEY_FAC_LIST_DTO,facListDto);
        FacListDto.FacList facList = subCommon.getFacInfo(request);
        ParamUtil.setSessionAttr(request,KEY_FACILITY_INFO,facList);
        ParamUtil.setRequestAttr(request,"doSettings",getDocSettingMap());
        ParamUtil.setRequestAttr(request,DTO_BSB_REPORT_INVENTORY,inventoryDto);
        //when back do data showing in page
        //show error message
    }

    public void preSwitch(BaseProcessClass bpc){
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String actionType = request.getParameter(KEY_ACTION_TYPE);
        ParamUtil.setSessionAttr(bpc.request, KEY_ACTION_TYPE, actionType);
    }

    /**
     * preSubmitDoc
     * */
    public void preSubmitDoc(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ReportInventoryDto inventoryDto = getReportInventoryDto(request);
        inventoryDto.reqObjMapping(request);
        //begin to validate
        ParamUtil.setSessionAttr(request,DTO_BSB_REPORT_INVENTORY,inventoryDto);
        ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.YES);
        Map<String, List<ReportInventoryDto.NewDocInfo>> newFiles =  inventoryDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request,"newFiles", newFiles);
        ParamUtil.setRequestAttr(request,"reportType",inventoryDto.getReportType());
    }

    /**
     * saveReport
     * */
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
        settingMap.put("others",new DocSetting(DocConstants.DOC_TYPE_OTHERS,"others",false));
        return settingMap;
    }

}
