package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.assessmentGuide.GuideConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
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
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import com.ecquaria.egov.core.common.constants.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Hua_Chong
 * @Date 2019/8/5 15:36
 */
@Delegator(value = "masterCodeDelegator")
@Slf4j
public class MasterCodeDelegator {

//    private final FilterParameter filterParameter = new FilterParameter.Builder()
//            .clz(MasterCodeQueryDto.class)
//            .searchAttr(MasterCodeConstants.SEARCH_PARAM)
//            .resultAttr(MasterCodeConstants.SEARCH_RESULT)
//            .sortField(MasterCodeConstants.MASTERCODE_SORT_COLUM).sortType(SearchParam.ASCENDING).build();


    private final MasterCodeService masterCodeService;


    @Autowired
    private MasterCodeDelegator(MasterCodeService masterCodeService) {
        this.masterCodeService = masterCodeService;
    }

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
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
    public void prepareData(BaseProcessClass bpc) {
        logAboutStart("prepareData");
        HttpServletRequest request = bpc.request;
        List<SelectOption> selectCodeStatusList = IaisCommonUtils.genNewArrayList();
        selectCodeStatusList.add(new SelectOption("", "Please Select"));
        selectCodeStatusList.add(new SelectOption("CMSTAT001", "Active"));
        selectCodeStatusList.add(new SelectOption("CMSTAT003", "Inactive"));
        List<MasterCodeCategoryDto> masterCodeCategoryDtoList = masterCodeService.getAllCodeCategory();
        List<SelectOption> mcCategorySelectList = IaisCommonUtils.genNewArrayList();
        mcCategorySelectList.add(new SelectOption("", "Please Select"));
        for (MasterCodeCategoryDto masterCodeCategoryDto : masterCodeCategoryDtoList
                ) {
            mcCategorySelectList.add(new SelectOption(masterCodeCategoryDto.getCategoryDescription(), masterCodeCategoryDto.getCategoryDescription()));
        }
        ParamUtil.setRequestAttr(bpc.request, "allCodeCategory", mcCategorySelectList);
        ParamUtil.setRequestAttr(bpc.request, "codeStatus", selectCodeStatusList);
//        SearchParam searchParam = SearchResultHelper.getSearchParam(request, filterParameter, true);
        SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request, MasterCodeConstants.SEARCH_PARAM,MasterCodeQueryDto.class.getName(),MasterCodeConstants.MASTERCODE_SORT_COLUM,SearchParam.ASCENDING,false);
        QueryHelp.setMainSql(MasterCodeConstants.MSG_TEMPLATE_FILE, MasterCodeConstants.MSG_TEMPLATE_SQL, searchParam);
        SearchResult searchResult = masterCodeService.doQuery(searchParam);
        List<MasterCodeQueryDto> masterCodeQueryDtoList = searchResult.getRows();
        for (MasterCodeQueryDto masterCodeQueryDto : masterCodeQueryDtoList
                ) {
            if (StringUtil.isEmpty(masterCodeQueryDto.getCodeValue())) {
                masterCodeQueryDto.setCodeValue("N/A");
            }
            masterCodeQueryDto.setStatus(MasterCodeUtil.getCodeDesc(masterCodeQueryDto.getStatus()));
        }

        if (!StringUtil.isEmpty(searchResult)) {
            ParamUtil.setSessionAttr(request, MasterCodeConstants.SEARCH_PARAM, searchParam);
            ParamUtil.setRequestAttr(request, MasterCodeConstants.SEARCH_RESULT, searchResult);
        }
    }

    /**
     * AutoStep: PrepareSwitch
     *
     * @throws
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        logAboutStart("prepareSwitch");
        HttpServletRequest request = bpc.request;

        String currentAction = request.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        logAboutStart(currentAction);
    }

    public void preateCreateCode(BaseProcessClass bpc) {
        logAboutStart("preateCreateCode");
        HttpServletRequest request = bpc.request;
        List<SelectOption> codeStatusSelectList = IaisCommonUtils.genNewArrayList();
        codeStatusSelectList.add(new SelectOption("CMSTAT001", "Active"));
        codeStatusSelectList.add(new SelectOption("CMSTAT003", "Inactive"));
        ParamUtil.setRequestAttr(bpc.request, "codeStatusSelectList", codeStatusSelectList);
        String masterCodeId = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_VALUE);
        if (!StringUtil.isEmpty(masterCodeId)) {
            MasterCodeDto masterCodeDto = masterCodeService.findMasterCodeByMcId(masterCodeId);
            masterCodeDto.setFilterValue(masterCodeDto.getCodeValue());
            ParamUtil.setSessionAttr(request, "MasterCodeView", masterCodeDto);
        }
    }

    /**
     * AutoStep: PrepareCreate
     *
     * @param bpc
     * @throws
     */
    public void prepareCreate(BaseProcessClass bpc) {
        logAboutStart("prepareCreate");
        HttpServletRequest request = bpc.request;

    }

    /**
     * AutoStep: createCode
     *
     * @param bpc
     * @throws
     */
    public void createCode(BaseProcessClass bpc) throws ParseException {
        logAboutStart("prepareCreate");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        if (!SystemAdminBaseConstants.SAVE_ACTION.equals(type)) {
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
            return;
        }
        MasterCodeDto masterCodeDto = (MasterCodeDto) ParamUtil.getSessionAttr(request, "MasterCodeView");
        getCategoryValueFromPage(masterCodeDto, request);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult validationResult = WebValidationHelper.validateProperty(masterCodeDto, SystemAdminBaseConstants.SAVE_ACTION);
        if (masterCodeDto.getEffectiveFrom() != null && masterCodeDto.getEffectiveTo() != null) {
            if (!masterCodeDto.getEffectiveFrom().before(masterCodeDto.getEffectiveTo())) {
                validationResult.setHasErrors(true);
            }
        }
        if (validationResult != null && validationResult.isHasErrors()) {
            errorMap = validationResult.retrieveAll();
            if (masterCodeDto.getEffectiveFrom() != null && masterCodeDto.getEffectiveTo() != null) {
                if (!masterCodeDto.getEffectiveFrom().before(masterCodeDto.getEffectiveTo())) {
                    validationResult.setHasErrors(true);
                    errorMap.put("effectiveTo", "Effective Start Date cannot be later than Effective End Date");
                }
            }
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            return;
        }
        String codeCategory = masterCodeService.findCodeCategoryByDescription(masterCodeDto.getCodeCategory());
        masterCodeDto.setCodeCategory(codeCategory);
        masterCodeService.saveMasterCode(masterCodeDto);
        ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
        ParamUtil.setRequestAttr(request, "CREATED_DATE", new Date());
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
        String codeValue = ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_VALUE);
        String codeStatus = ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_STATUS);
        String filterValue = ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_FILTER_VALUE);
        Date codeEffFrom = Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM));
        Date codeEffTo = Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO));
        String codeStartDate = Formatter.formatDateTime(codeEffFrom, SystemAdminBaseConstants.DATE_FORMAT);
        String codeEndDate = Formatter.formatDateTime(codeEffTo, SystemAdminBaseConstants.DATE_FORMAT);
//        Map<String, Object> masterCodeMap = IaisCommonUtils.genNewHashMap();
        SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request, MasterCodeConstants.SEARCH_PARAM,MasterCodeQueryDto.class.getName(),MasterCodeConstants.MASTERCODE_SORT_COLUM,SearchParam.ASCENDING,true);
        if (!StringUtil.isEmpty(categoryDescription) && !"Please Select".equals(categoryDescription)) {
            searchParam.addFilter(MasterCodeConstants.MASTER_CODE_CATEGORY, categoryDescription,true);
        } else {
            searchParam.removeFilter(MasterCodeConstants.MASTER_CODE_CATEGORY);
        }
        if (!StringUtil.isEmpty(codeStatus) && !"Please Select".equals(codeStatus)) {
            searchParam.addFilter(MasterCodeConstants.MASTER_CODE_STATUS, codeStatus,true);
        } else {
            searchParam.removeFilter(MasterCodeConstants.MASTER_CODE_STATUS);
        }
        if (!StringUtil.isEmpty(codeDescription)) {
            searchParam.addFilter(MasterCodeConstants.MASTER_CODE_DESCRIPTION, "%" + codeDescription + "%",true);
        } else {
            searchParam.removeFilter(MasterCodeConstants.MASTER_CODE_DESCRIPTION);
        }
        if (!StringUtil.isEmpty(codeValue)) {
            searchParam.addFilter(MasterCodeConstants.MASTER_CODE_VALUE, "%" + codeValue + "%",true);
        } else {
            searchParam.removeFilter(MasterCodeConstants.MASTER_CODE_VALUE);
        }
        if (!StringUtil.isEmpty(filterValue)) {
            searchParam.addFilter(SystemAdminBaseConstants.MASTER_CODE_FILTER_VALUE, "%" + filterValue + "%",true);
        } else {
            searchParam.removeFilter(SystemAdminBaseConstants.MASTER_CODE_FILTER_VALUE);
        }
        if (codeEffFrom != null && codeEffTo != null) {
            if (codeEffFrom.compareTo(codeEffTo) <= 0) {
                if (!StringUtil.isEmpty(codeStartDate)) {
                    searchParam.addFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM, codeStartDate,true);
                } else {
                    searchParam.removeFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM);
                }
                if (!StringUtil.isEmpty(codeEndDate)) {
                    searchParam.addFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO, codeEndDate,true);
                } else {
                    searchParam.removeFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO);
                }
            } else {
                ParamUtil.setRequestAttr(request, "ERR_EED", "Effective Start Date cannot be later than Effective End Date");
            }
        } else {
            if (!StringUtil.isEmpty(codeStartDate)) {
                searchParam.addFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM, codeStartDate,true);
            } else {
                searchParam.removeFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM);
            }
            if (!StringUtil.isEmpty(codeEndDate)) {
                searchParam.addFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO, codeEndDate,true);
            } else {
                searchParam.removeFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO);
            }
        }
    }


    @GetMapping(value = "master-code-file")
    public @ResponseBody
    void fileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        List<MasterCodeToExcelDto> masterCodeToExcelDtoList = IaisCommonUtils.genNewArrayList();
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(request, MasterCodeConstants.SEARCH_PARAM);
        searchParam.setPageSize(Integer.MAX_VALUE);
        SearchResult<MasterCodeQueryDto> searchResult = masterCodeService.doQuery(searchParam);
        searchResult.getRows().forEach(h ->{
            MasterCodeToExcelDto masterCodeToExcelDto = new MasterCodeToExcelDto();
            masterCodeToExcelDto.setCodeCategory(h.getCodeCategory());
            masterCodeToExcelDto.setSequence(String.valueOf(h.getSequence()));
            masterCodeToExcelDto.setVersion(h.getVersion());
            masterCodeToExcelDto.setCodeDescription(h.getCodeDescription());
            masterCodeToExcelDto.setCodeValue(h.getCodeValue());
            masterCodeToExcelDto.setEffectiveFrom(Formatter.formatDateTime(h.getEffectiveStartDate()));
            masterCodeToExcelDto.setEffectiveTo(Formatter.formatDateTime(h.getEffectiveEndDate()));
            masterCodeToExcelDto.setFilterValue(h.getFilterValue());
            masterCodeToExcelDto.setRemakes(h.getRemarks());
            masterCodeToExcelDto.setStatus(MasterCodeUtil.getCodeDesc(h.getStatus()));
            masterCodeToExcelDtoList.add(masterCodeToExcelDto);
        });
        if (masterCodeToExcelDtoList.size()>0) {
            try {
                File file =  ExcelWriter.writerToExcel(masterCodeToExcelDtoList, MasterCodeToExcelDto.class, "Master_Code_File");
                FileUtils.writeFileResponseContent(response, file);
                FileUtils.deleteTempFile(file);

            } catch (Exception e) {
                log.error("=======>fileHandler error >>>>>", e);
            }
        }
    }


    @GetMapping(value = "suggest-code-description")
    public @ResponseBody
    List<String> suggerMasterCode(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        String codeDescription = request.getParameter("description");
        List<String> codeDescriptionList = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(codeDescription)){
            codeDescriptionList = masterCodeService.suggestCodeDescription(codeDescription);
        }
        return codeDescriptionList;
    }

    public void doUpload(BaseProcessClass bpc) throws Exception {
        logAboutStart("doUpload");
        HttpServletRequest request = bpc.request;
    }

    /**
     * AutoStep: doPaging
     *
     * @param bpc
     * @throws
     */
    public void doPaging(BaseProcessClass bpc) {
        logAboutStart("doPaging");
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, MasterCodeConstants.SEARCH_PARAM);
        HalpSearchResultHelper.doPage(request,searchParam);
    }

    /**
     * AutoStep: doPaging
     *
     * @param bpc
     * @throws
     */
    public void uploadStep(BaseProcessClass bpc) throws Exception {
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String actionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        if (!"doUpload".equals(actionType)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }
        MultipartFile file = mulReq.getFile("selectedFile");
        Map<String, String> errorMap = validationFile(request, file);
        if (errorMap != null && errorMap.size()>0) {
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }
        File toFile = FileUtils.multipartFileToFile(file);
        try{
            List<MasterCodeToExcelDto> masterCodeToExcelDtoList = FileUtils.transformToJavaBean(toFile, MasterCodeToExcelDto.class);
            boolean result = false;
            List<Map<String,List<String>>> errResult = IaisCommonUtils.genNewArrayList();
            for (MasterCodeToExcelDto masterCodeToExcelDto : masterCodeToExcelDtoList) {
                Map<String,List<String>> errMap = IaisCommonUtils.genNewHashMap();
                List<String> errItems = IaisCommonUtils.genNewArrayList();
                if (StringUtil.isEmpty(masterCodeToExcelDto.getCodeCategory()))
                {
                    String errMsg = "Code Category is a mandatory field.";
                    errItems.add(errMsg);
                    result = true;
                }
                if (StringUtil.isEmpty(masterCodeToExcelDto.getStatus())){
                    String errMsg = "Status is a mandatory field.";
                    errItems.add(errMsg);
                    result = true;
                }
                if (StringUtil.isEmpty(masterCodeToExcelDto.getEffectiveFrom())){
                    String errMsg = "Effective From is a mandatory field.";
                    errItems.add(errMsg);
                    result = true;
                }
                if (!StringUtil.isEmpty(masterCodeToExcelDto.getFilterValue())){
                    List<MasterCodeToExcelDto> masterCodeToExcelDtos = masterCodeService.findAllMasterCode();
                    List<String> codeValueList = IaisCommonUtils.genNewArrayList();
                    masterCodeToExcelDtos.forEach(h -> {
                        codeValueList.add(h.getCodeValue());
                    });
                    if (!codeValueList.contains(masterCodeToExcelDto.getFilterValue())){
                        String errMsg = "Filter Value must be an existing Code Value";
                        errItems.add(errMsg);
                        result = true;
                    }
                }
                if(!StringUtil.isEmpty(masterCodeToExcelDto.getCodeCategory())){
                    masterCodeToExcelDto.setCodeCategory(masterCodeService.findCodeCategoryByDescription(masterCodeToExcelDto.getCodeCategory()));
                }
                if (!StringUtil.isEmpty(masterCodeToExcelDto.getStatus())){
                    if ("Active".equals(masterCodeToExcelDto.getStatus())){
                        masterCodeToExcelDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    }else if ("Deleted".equals(masterCodeToExcelDto.getStatus())){
                        masterCodeToExcelDto.setStatus(AppConsts.COMMON_STATUS_DELETED);
                    }else if ("Inactive".equals(masterCodeToExcelDto.getStatus())){
                        masterCodeToExcelDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    }else{
                        String errMsg = "The Status can only be Active/Deleted/Inactive";
                        errItems.add(errMsg);
                        result = true;
                    }
                }
                errMap.put(masterCodeToExcelDto.getCodeValue(),errItems);
                errResult.add(errMap);
            }
            if (result){
                if (errorMap != null){
                    errorMap.put(MasterCodeConstants.MASTER_CODE_UPLOAD_FILE, "There is an error in the file contents");
                }
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(request,"ERR_CONTENT","SUCCESS");
                ParamUtil.setRequestAttr(request,"ERR_RESULT_LIST_MAP",errResult);
            }else{
                masterCodeService.saveMasterCodeList(masterCodeToExcelDtoList);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
                ParamUtil.setRequestAttr(request, "UPLOAD_DATE", new Date());
            }
        }catch (Exception e){
            errorMap.put(MasterCodeConstants.MASTER_CODE_UPLOAD_FILE, "There is an error in the file contents");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
        }
    }

    /**
     * AutoStep: doSorting
     *
     * @param bpc
     * @throws
     */
    public void doSorting(BaseProcessClass bpc) {
        logAboutStart("doSorting");
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, MasterCodeConstants.SEARCH_PARAM);
        HalpSearchResultHelper.doSort(request,searchParam);
    }

    /**
     * AutoStep: doDelete
     *
     * @param bpc
     * @throws
     */
    public void doDelete(BaseProcessClass bpc) {
        logAboutStart("doDelete");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        if ("doDelete".equals(type)) {
            String masterCodeId = ParamUtil.getString(bpc.request, SystemAdminBaseConstants.CRUD_ACTION_VALUE);
            MasterCodeDto masterCodeDto = masterCodeService.findMasterCodeByMcId(masterCodeId);
            if (masterCodeDto.getEffectiveFrom().before(new Date())) {
                String codeCategory = masterCodeService.findCodeCategoryByDescription(masterCodeDto.getCodeCategory());
                masterCodeDto.setCodeCategory(codeCategory);
                masterCodeDto.setStatus("CMSTAT003");
                masterCodeService.updateMasterCode(masterCodeDto);
            } else {
                masterCodeService.deleteMasterCodeById(masterCodeId);
            }
        }
        ParamUtil.setRequestAttr(request, "DELETE_DATE", new Date());
    }

    public void prepareCode(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        prepareSelect(request);
        List<MasterCodeCategoryDto> masterCodeCategoryDtoList = masterCodeService.getAllCodeCategory();
        List<SelectOption> mcCategorySelectList = IaisCommonUtils.genNewArrayList();
        for (MasterCodeCategoryDto masterCodeCategoryDto : masterCodeCategoryDtoList
                ) {
            mcCategorySelectList.add(new SelectOption(masterCodeCategoryDto.getCodeCategory(), masterCodeCategoryDto.getCategoryDescription()));
        }
        ParamUtil.setRequestAttr(bpc.request, "codeCategory", mcCategorySelectList);

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
        if (!SystemAdminBaseConstants.SAVE_ACTION.equals(type)) {
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
            return;
        }
        MasterCodeDto masterCodeDto = new MasterCodeDto();
        getValueFromPage(masterCodeDto, request);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult validationResult = WebValidationHelper.validateProperty(masterCodeDto, SystemAdminBaseConstants.SAVE_ACTION);
        if (masterCodeDto.getEffectiveFrom() != null && masterCodeDto.getEffectiveTo() != null) {
            if (!masterCodeDto.getEffectiveFrom().before(masterCodeDto.getEffectiveTo())) {
                validationResult.setHasErrors(true);
            }
        }
        if (validationResult != null && validationResult.isHasErrors()) {
            errorMap = validationResult.retrieveAll();
            if (masterCodeDto.getEffectiveFrom() != null && masterCodeDto.getEffectiveTo() != null) {
                if (!masterCodeDto.getEffectiveFrom().before(masterCodeDto.getEffectiveTo())) {
                    validationResult.setHasErrors(true);
                    errorMap.put("effectiveTo", "Effective Start Date cannot be later than Effective End Date");
                }
            }
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            ParamUtil.setRequestAttr(request, "codeCategory", ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_CATEGORY));
            return;
        }
        masterCodeService.saveMasterCode(masterCodeDto);
        ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
        ParamUtil.setRequestAttr(request, "CREATED_DATE", new Date());

    }

    /**
     * AutoStep: prepareEdit
     *
     * @param bpc
     * @throws
     */
    public void prepareEdit(BaseProcessClass bpc) {
        logAboutStart("prepareEdit");
        HttpServletRequest request = bpc.request;
        String masterCodeId = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_VALUE);
        if (!masterCodeId.isEmpty()) {
            MasterCodeDto masterCodeDto = masterCodeService.findMasterCodeByMcId(masterCodeId);
            List<SelectOption> mcStatusSelectList = IaisCommonUtils.genNewArrayList();
            mcStatusSelectList.add(new SelectOption("", "Please Select"));
            mcStatusSelectList.add(new SelectOption("CMSTAT001", "Active"));
            mcStatusSelectList.add(new SelectOption("CMSTAT003", "Inactive"));
            ParamUtil.setRequestAttr(bpc.request, "mcStatusSelectList", mcStatusSelectList);
            ParamUtil.setSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR, masterCodeDto);
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
        String type = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        if (!SystemAdminBaseConstants.EDIT_ACTION.equals(type)) {
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
            return;
        }
        MasterCodeDto masterCodeDto = (MasterCodeDto) ParamUtil.getSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR);
        getEditValueFromPage(masterCodeDto, request);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult validationEditResult = WebValidationHelper.validateProperty(masterCodeDto, "edit");
        if (masterCodeDto.getEffectiveFrom() != null && masterCodeDto.getEffectiveTo() != null) {
            if (!masterCodeDto.getEffectiveFrom().before(masterCodeDto.getEffectiveTo())) {
                validationEditResult.setHasErrors(true);
            }
        }
        if (validationEditResult != null && validationEditResult.isHasErrors()) {
            logAboutStart("Edit validation");
            errorMap = validationEditResult.retrieveAll();
            if (masterCodeDto.getEffectiveFrom() != null && masterCodeDto.getEffectiveTo() != null) {
                if (!masterCodeDto.getEffectiveFrom().before(masterCodeDto.getEffectiveTo())) {
                    errorMap.put("effectiveTo", "Effective Start Date cannot be later than Effective End Date");
                }
            }
            ParamUtil.setSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR, masterCodeDto);
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            return;
        }
        String codeCategory = masterCodeService.findCodeCategoryByDescription(masterCodeDto.getCodeCategory());
        masterCodeDto.setCodeCategory(codeCategory);
        masterCodeDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
        masterCodeService.updateMasterCode(masterCodeDto);

        masterCodeDto.setMasterCodeId(null);
        masterCodeDto.setVersion(masterCodeDto.getVersion() + 1);
        masterCodeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        masterCodeService.saveMasterCode(masterCodeDto);
        ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
        ParamUtil.setRequestAttr(request, "UPDATED_DATE", new Date());

    }

    private void getEditValueFromPage(MasterCodeDto masterCodeDto, HttpServletRequest request) throws ParseException {
        masterCodeDto.setCodeValue(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_VALUE_ED));
        masterCodeDto.setCodeDescription(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_DESCRIPTION_ED));
        masterCodeDto.setStatus(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_STATUS_ED));
        masterCodeDto.setRemarks(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_REMARKS_ED));
        masterCodeDto.setSequence(StringUtil.isEmpty(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_SEQUENCE_ED)) ? null : ParamUtil.getInt(request, MasterCodeConstants.MASTER_CODE_SEQUENCE_ED));
        masterCodeDto.setEffectiveFrom(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_FROM_ED)));
        masterCodeDto.setEffectiveTo(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_TO_ED)));
        masterCodeDto.setIsEditable(0);
        masterCodeDto.setIsCentrallyManage(0);
        masterCodeDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
    }

    private void getValueFromPage(MasterCodeDto masterCodeDto, HttpServletRequest request) throws ParseException {
        masterCodeDto.setCodeValue(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_VALUE_CMC));
        masterCodeDto.setCodeCategory(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_CATEGORY_CMC));
        masterCodeDto.setCodeDescription(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_DESCRIPTION_CMC));
        masterCodeDto.setFilterValue(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_FILTER_VALUE_CMC));
        masterCodeDto.setStatus(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_STATUS_CMC));
        masterCodeDto.setRemarks(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_REMARKS_CMC));
        masterCodeDto.setSequence(StringUtil.isEmpty(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_SEQUENCE_CMC)) ? null : ParamUtil.getInt(request, MasterCodeConstants.MASTER_CODE_SEQUENCE_CMC));
        masterCodeDto.setVersion(StringUtil.isEmpty(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_VERSION_CMC)) ? null : Float.parseFloat(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_VERSION_CMC)));
        masterCodeDto.setEffectiveFrom(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_FROM_CMC)));
        masterCodeDto.setEffectiveTo(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_TO_CMC)));
        masterCodeDto.setIsEditable(0);
        masterCodeDto.setIsCentrallyManage(0);
        masterCodeDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
    }

    private void getCategoryValueFromPage(MasterCodeDto masterCodeDto, HttpServletRequest request) throws ParseException {
        masterCodeDto.setMasterCodeId(null);
        masterCodeDto.setCodeValue(ParamUtil.getString(request, "codeCategoryValue"));
        masterCodeDto.setCodeDescription(ParamUtil.getString(request, "codeCategoryDescription"));
        masterCodeDto.setStatus(ParamUtil.getString(request, "codeCategoryStatus"));
        masterCodeDto.setRemarks(ParamUtil.getString(request, "codeCategoryRemarks"));
        masterCodeDto.setSequence(StringUtil.isEmpty(ParamUtil.getString(request, "codeCategorySequence")) ? 100 : ParamUtil.getInt(request, "codeCategorySequence"));
        masterCodeDto.setEffectiveFrom(Formatter.parseDate(ParamUtil.getString(request, "categoryEsd")));
        masterCodeDto.setEffectiveTo(Formatter.parseDate(ParamUtil.getString(request, "categoryEed")));
        masterCodeDto.setIsEditable(0);
        masterCodeDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
    }

    private void logAboutStart(String methodName) {
        log.debug(StringUtil.changeForLog("**** The  " + methodName + "  Start ****"));
    }

    private void prepareSelect(HttpServletRequest request) {
        List<SelectOption> selectCodeStatusList = IaisCommonUtils.genNewArrayList();
        selectCodeStatusList.add(new SelectOption("", "Please Select"));
        selectCodeStatusList.add(new SelectOption("CMSTAT001", "Active"));
        selectCodeStatusList.add(new SelectOption("CMSTAT003", "Inactive"));
        ParamUtil.setRequestAttr(request, "codeStatus", selectCodeStatusList);
    }

    private Map<String, String> validationFile(HttpServletRequest request, MultipartFile file){
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        if (file.isEmpty()){
            errorMap.put(MasterCodeConstants.MASTER_CODE_UPLOAD_FILE, "GENERAL_ERR0020");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }

        String originalFileName = file.getOriginalFilename();
        if (!FileUtils.isExcel(originalFileName)){
            errorMap.put(MasterCodeConstants.MASTER_CODE_UPLOAD_FILE, MessageCodeKey.GENERAL_ERR0005);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }

        if (FileUtils.outFileSize(file.getSize())){
            errorMap.put(MasterCodeConstants.MASTER_CODE_UPLOAD_FILE, MessageCodeKey.GENERAL_ERR0004);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }
        return errorMap;
    }
}
