package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MasterCodeConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeCategoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeToExcelDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Author Hua_Chong
 * @Date 2019/8/5 15:36
 */
@Delegator(value = "masterCodeDelegator")
@Slf4j
public class MasterCodeDelegator {

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(MasterCodeQueryDto.class)
            .searchAttr(MasterCodeConstants.SEARCH_PARAM)
            .resultAttr(MasterCodeConstants.SEARCH_RESULT)
            .sortField(MasterCodeConstants.MASTERCODE_SORT_COLUM).sortType(SearchParam.ASCENDING).build();


    private final MasterCodeService masterCodeService;

    @Autowired
    private MasterCodeDelegator(MasterCodeService masterCodeService){
        this.masterCodeService = masterCodeService;
    }

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        logAboutStart("doStart");
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("MasterCode",
                "MasterCode Function");
        ParamUtil.setSessionAttr(request, MasterCodeConstants.SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(request, MasterCodeConstants.SEARCH_RESULT, null);
        ParamUtil.setSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR, null);
    }

    /**
     * AutoStep: PrepareData
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc){
        logAboutStart("prepareData");
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = SearchResultHelper.getSearchParam(request,filterParameter,true);
        QueryHelp.setMainSql(MasterCodeConstants.MSG_TEMPLATE_FILE, MasterCodeConstants.MSG_TEMPLATE_SQL,searchParam);
        SearchResult searchResult = masterCodeService.doQuery(searchParam);
        List<MasterCodeQueryDto> masterCodeQueryDtoList = searchResult.getRows();
        for (MasterCodeQueryDto masterCodeQueryDto:masterCodeQueryDtoList
             ) {
            if (StringUtil.isEmpty(masterCodeQueryDto.getCodeValue())){
                masterCodeQueryDto.setCodeValue("N.A");
            }
            masterCodeQueryDto.setStatus(MasterCodeUtil.getCodeDesc(masterCodeQueryDto.getStatus()));
        }

        if(!StringUtil.isEmpty(searchResult)){
            ParamUtil.setSessionAttr(request,MasterCodeConstants.SEARCH_PARAM, searchParam);
            ParamUtil.setRequestAttr(request,MasterCodeConstants.SEARCH_RESULT, searchResult);
        }

    }

    /**
     * AutoStep: PrepareSwitch
     *
     * @throws
     */
    public void prepareSwitch(BaseProcessClass bpc){
        logAboutStart("prepareSwitch");
        String type = ParamUtil.getString(bpc.request, SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        logAboutStart(type);
    }

    /**
     * AutoStep: PrepareCreate
     *
     * @param bpc
     * @throws
     */
    public void prepareCreate(BaseProcessClass bpc){
        logAboutStart("prepareCreate");
        HttpServletRequest request = bpc.request;
    }



    /**
     * AutoStep: doSearch
     *
     * @param bpc
     * @throws
     */
    public void doSearch(BaseProcessClass bpc) throws ParseException {
        logAboutStart("doSearch");
        HttpServletRequest request = bpc.request;
        String categoryDescription = ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_CATEGORY);
        String codeDescription = ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_DESCRIPTION);
        String codeStatus = ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_STATUS);
        String codeStartDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM)),
                SystemAdminBaseConstants.DATE_FORMAT);
        String codeEndDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO)),
                SystemAdminBaseConstants.DATE_FORMAT);
        Map<String,Object> masterCodeMap = IaisCommonUtils.genNewHashMap();

        if (!StringUtil.isEmpty(categoryDescription)){
            String codeCategory = masterCodeService.findCodeCategoryByDescription(categoryDescription);
            masterCodeMap.put(MasterCodeConstants.MASTER_CODE_CATEGORY,codeCategory);
        }
        if(!StringUtil.isEmpty(codeStatus)){
            masterCodeMap.put(MasterCodeConstants.MASTER_CODE_STATUS,codeStatus);
        }
        if(!StringUtil.isEmpty(codeDescription)){
            masterCodeMap.put(MasterCodeConstants.MASTER_CODE_DESCRIPTION,codeDescription);
        }
        if (!StringUtil.isEmpty(codeStartDate)){
            masterCodeMap.put( SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM,codeStartDate);
        }
        if (!StringUtil.isEmpty(codeEndDate)){
            masterCodeMap.put( SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO,codeEndDate);
        }
        filterParameter.setFilters(masterCodeMap);
    }
    /**
     * AutoStep: doSorting
     *
     * @param bpc
     * @throws
     */
    public void doSorting(BaseProcessClass bpc){
        logAboutStart("doSorting");
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,bpc.request);
    }

    @GetMapping(value = "master-code-file")
    public @ResponseBody void fileHandler(HttpServletRequest request, HttpServletResponse response){
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        String action = ParamUtil.getString(request, "action");
        File file = null;
        List<MasterCodeToExcelDto> masterCodeToExcelDtoList = masterCodeService.findAllMasterCode();
        if (masterCodeToExcelDtoList != null){
            file = ExcelWriter.exportExcel(masterCodeToExcelDtoList, MasterCodeToExcelDto.class, "Master_Code_File");
        }
        try {
            FileUtils.writeFileResponeContent(response, file);
            FileUtils.deleteTempFile(file);
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
    }

    public void doUpload(BaseProcessClass bpc) throws Exception {
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        MultipartFile file = mulReq.getFile("selectedFile");
        File toFile = FileUtils.multipartFileToFile(file);
        List<MasterCodeToExcelDto> masterCodeToExcelDtoList = FileUtils.transformToJavaBean(toFile, MasterCodeToExcelDto.class);

    }
    /**
     * AutoStep: doPaging
     *
     * @param bpc
     * @throws
     */
    public void doPaging(BaseProcessClass bpc){
        logAboutStart("doPaging");
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = SearchResultHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * AutoStep: doDelete
     *
     * @param bpc
     * @throws
     */
    public void doDelete(BaseProcessClass bpc){
        logAboutStart("doDelete");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        String action = ParamUtil.getString(request, "crud_action_deactivate");

        if ("doDelete".equals(type)){
            String masterCodeId = ParamUtil.getString(bpc.request,SystemAdminBaseConstants.CRUD_ACTION_VALUE);
            if("doDeactivate".equals(action)){
                MasterCodeDto masterCodeDto = masterCodeService.findMasterCodeByMcId(masterCodeId);
                String codeCategory = masterCodeService.findCodeCategoryByDescription(masterCodeDto.getCodeCategory());
                masterCodeDto.setCodeCategory(codeCategory);
                masterCodeDto.setStatus("CMSTAT003");
                masterCodeService.updateMasterCode(masterCodeDto);
            }else{
                masterCodeService.deleteMasterCodeById(masterCodeId);
            }

        }
    }

    public void doCreateCategory(BaseProcessClass bpc) throws ParseException {
        logAboutStart("doCreate");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        if (!SystemAdminBaseConstants.SAVE_ACTION.equals(type)){
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
            return;
        }
        MasterCodeCategoryDto masterCodeCategoryDto = new MasterCodeCategoryDto();
        String categoryDescription = ParamUtil.getString(request,"codeKey");
        String isEditable = ParamUtil.getString(request,"editable");
        masterCodeCategoryDto.setCategoryDescription(categoryDescription);
        masterCodeCategoryDto.setIsEditable(isEditable == null?null:Integer.valueOf(isEditable));
        masterCodeCategoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        ValidationResult validationResult = WebValidationHelper.validateProperty(masterCodeCategoryDto,SystemAdminBaseConstants.SAVE_ACTION);
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            return;
        }
        if (!StringUtil.isEmpty(categoryDescription)){
            String codeCategory = masterCodeService.findCodeCategoryByDescription(categoryDescription);
            if (codeCategory == null){
                masterCodeService.saveMasterCodeCategory(masterCodeCategoryDto);
            }
        }
        ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
        ParamUtil.setRequestAttr(request, "codeCategory",categoryDescription);
    }

    public void prepareCode(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        prepareSelect(request);
        String type = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        if (SystemAdminBaseConstants.SAVE_ACTION.equals(type)){
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
        }else{
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
        }

    }

    /**
     * AutoStep: doCreate
     *
     * @param bpc
     * @throws
     */
    public void doCreateCode(BaseProcessClass bpc) throws ParseException {
        logAboutStart("doCreate");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        if (!SystemAdminBaseConstants.SAVE_ACTION.equals(type)){
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
            return;
        }
        MasterCodeDto masterCodeDto = new MasterCodeDto();
        getValueFromPage(masterCodeDto,request);
        ValidationResult validationResult = WebValidationHelper.validateProperty(masterCodeDto,SystemAdminBaseConstants.SAVE_ACTION);
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            return;
        }
        masterCodeDto.setCodeCategory(masterCodeService.findCodeCategoryByDescription(masterCodeDto.getCodeCategory()));
        masterCodeService.saveMasterCode(masterCodeDto);
        ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);

    }

    /**
     * AutoStep: prepareEdit
     *
     * @param bpc
     * @throws
     */
    public void prepareEdit(BaseProcessClass bpc){
        logAboutStart("prepareEdit");
        HttpServletRequest request = bpc.request;
        String masterCodeId = ParamUtil.getString(request,SystemAdminBaseConstants.CRUD_ACTION_VALUE);
        if (!masterCodeId.isEmpty()){
            MasterCodeDto masterCodeDto = masterCodeService.findMasterCodeByMcId(masterCodeId);
            List<SelectOption> mcStatusSelectList = IaisCommonUtils.genNewArrayList();
            mcStatusSelectList.add(new SelectOption(masterCodeDto.getStatus(), MasterCodeUtil.getCodeDesc(masterCodeDto.getStatus())));
            mcStatusSelectList.add(new SelectOption("CMSTAT001", "Active"));
            mcStatusSelectList.add(new SelectOption("CMSTAT002", "Deleted"));
            mcStatusSelectList.add(new SelectOption("CMSTAT003", "Inactive"));
            ParamUtil.setRequestAttr(bpc.request, "mcStatusSelectList", mcStatusSelectList);
            ParamUtil.setSessionAttr(request,MasterCodeConstants.MASTERCODE_USER_DTO_ATTR, masterCodeDto);
        }
    }

    /**
     * AutoStep: doEdit
     *
     * @param bpc
     * @throws
     */
    public void doEdit(BaseProcessClass bpc) throws ParseException {
        logAboutStart("doEdit");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        if (!SystemAdminBaseConstants.EDIT_ACTION.equals(type)){
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
            return;
        }
        MasterCodeDto masterCodeDto = (MasterCodeDto) ParamUtil.getSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR);
        getValueFromPage(masterCodeDto, request);
        ValidationResult validationEditResult =WebValidationHelper.validateProperty(masterCodeDto, "edit");
        if(validationEditResult != null && validationEditResult.isHasErrors()) {
            logAboutStart("Edit validation");
            Map<String, String> errorMap = validationEditResult.retrieveAll();
            ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            return;
        }
        String codeCategory = masterCodeService.findCodeCategoryByDescription(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_CATEGORY));
        masterCodeDto.setCodeCategory(codeCategory);
        masterCodeService.updateMasterCode(masterCodeDto);
        ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.ISVALID,SystemAdminBaseConstants.YES);

    }

    private void getValueFromPage(MasterCodeDto masterCodeDto, HttpServletRequest request) throws ParseException {
        masterCodeDto.setMasterCodeKey(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_KEY));
        masterCodeDto.setCodeValue(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_VALUE));
        masterCodeDto.setCodeCategory(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_CATEGORY));
        masterCodeDto.setCodeDescription(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_DESCRIPTION));
        masterCodeDto.setFilterValue(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_FILTER_VALUE));
        masterCodeDto.setStatus(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_STATUS));
        masterCodeDto.setSequence(StringUtil.isEmpty(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_SEQUENCE))? null : ParamUtil.getInt(request,MasterCodeConstants.MASTER_CODE_SEQUENCE));
        masterCodeDto.setVersion(StringUtil.isEmpty(ParamUtil.getString(request,MasterCodeConstants.MASTER_CODE_VERSION)) ? null : ParamUtil.getInt(request,MasterCodeConstants.MASTER_CODE_VERSION));
        masterCodeDto.setEffectiveFrom(Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM)));
        masterCodeDto.setEffectiveTo(Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO)));
        masterCodeDto.setIsEditable(0);
        masterCodeDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
    }

    private void logAboutStart(String methodName){
        log.debug("**** The  "+methodName+"  Start ****");
    }

    private void prepareSelect(HttpServletRequest request){
        List<SelectOption> selectCodeStatusList = IaisCommonUtils.genNewArrayList();
        selectCodeStatusList.add(new SelectOption("CMSTAT001", "Active"));
        selectCodeStatusList.add(new SelectOption("CMSTAT003", "Inactive"));
        ParamUtil.setRequestAttr(request, "codeStatus", selectCodeStatusList);
    }
}
