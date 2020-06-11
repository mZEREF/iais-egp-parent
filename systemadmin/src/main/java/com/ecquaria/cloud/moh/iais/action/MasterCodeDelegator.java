package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
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
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        filterParameter.setFilters(null);
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
        SearchParam searchParam = SearchResultHelper.getSearchParam(request, filterParameter, true);
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
        boolean isExist = masterCodeService.masterCodeKeyIsExist(masterCodeDto.getMasterCodeKey());
        if (!isExist){
            validationResult.setHasErrors(true);
        }
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
            if (!isExist){
                validationResult.setHasErrors(true);
                errorMap.put("masterCodeKey", "This master code key has duplicate");
            }
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            return;
        }
        String codeCategory = masterCodeService.findCodeCategoryByDescription(masterCodeDto.getCodeCategory());
        masterCodeDto.setCodeCategory(codeCategory);
        if (isExist){
            masterCodeService.saveMasterCode(masterCodeDto);
        }
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
        Map<String, Object> masterCodeMap = IaisCommonUtils.genNewHashMap();
        if (!StringUtil.isEmpty(categoryDescription) && !"Please Select".equals(categoryDescription)) {
            masterCodeMap.put(MasterCodeConstants.MASTER_CODE_CATEGORY, categoryDescription);
        } else {
            masterCodeMap.remove(MasterCodeConstants.MASTER_CODE_CATEGORY);
        }
        if (!StringUtil.isEmpty(codeStatus) && !"Please Select".equals(codeStatus)) {
            masterCodeMap.put(MasterCodeConstants.MASTER_CODE_STATUS, codeStatus);
        } else {
            masterCodeMap.remove(MasterCodeConstants.MASTER_CODE_STATUS);
        }
        if (!StringUtil.isEmpty(codeDescription)) {
            masterCodeMap.put(MasterCodeConstants.MASTER_CODE_DESCRIPTION, "%" + codeDescription + "%");
        } else {
            masterCodeMap.remove(MasterCodeConstants.MASTER_CODE_DESCRIPTION);
        }
        if (!StringUtil.isEmpty(codeValue)) {
            masterCodeMap.put(MasterCodeConstants.MASTER_CODE_VALUE, "%" + codeValue + "%");
        } else {
            masterCodeMap.remove(MasterCodeConstants.MASTER_CODE_VALUE);
        }
        if (!StringUtil.isEmpty(filterValue)) {
            masterCodeMap.put(SystemAdminBaseConstants.MASTER_CODE_FILTER_VALUE, "%" + filterValue + "%");
        } else {
            masterCodeMap.remove(SystemAdminBaseConstants.MASTER_CODE_FILTER_VALUE);
        }
        if (codeEffFrom != null && codeEffTo != null) {
            if (codeEffFrom.compareTo(codeEffTo) <= 0) {
                if (!StringUtil.isEmpty(codeStartDate)) {
                    masterCodeMap.put(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM, codeStartDate);
                } else {
                    masterCodeMap.remove(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM);
                }
                if (!StringUtil.isEmpty(codeEndDate)) {
                    masterCodeMap.put(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO, codeEndDate);
                } else {
                    masterCodeMap.remove(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO);
                }
            } else {
                ParamUtil.setRequestAttr(request, "ERR_EED", "Effective Start Date cannot be later than Effective End Date");
            }
        } else {
            if (!StringUtil.isEmpty(codeStartDate)) {
                masterCodeMap.put(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM, codeStartDate);
            } else {
                masterCodeMap.remove(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM);
            }
            if (!StringUtil.isEmpty(codeEndDate)) {
                masterCodeMap.put(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO, codeEndDate);
            } else {
                masterCodeMap.remove(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO);
            }
        }
        filterParameter.setFilters(masterCodeMap);
    }


    @GetMapping(value = "master-code-file")
    public @ResponseBody
    void fileHandler(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));

        List<MasterCodeToExcelDto> masterCodeToExcelDtoList = masterCodeService.findAllMasterCode();
        if (masterCodeToExcelDtoList != null) {
            ExcelWriter excelWriter = new ExcelWriter();
            excelWriter.setClz(MasterCodeToExcelDto.class);
            excelWriter.setFileName("Master_Code_File");
            try {
                File file = excelWriter.writerToExcel(masterCodeToExcelDtoList);
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
        SearchResultHelper.doPage(request, filterParameter);
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
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            return;
        }
        MultipartFile file = mulReq.getFile("selectedFile");
        File toFile = FileUtils.multipartFileToFile(file);
        Map<String, String> errorMap = validationFile(request, file);
        if (errorMap != null && !errorMap.isEmpty()){
            return;
        }
        List<MasterCodeToExcelDto> masterCodeToExcelDtoList = FileUtils.transformToJavaBean(toFile, MasterCodeToExcelDto.class);
        List<String> duplicateCode = IaisCommonUtils.genNewArrayList();
        List<String> emptyCode = IaisCommonUtils.genNewArrayList();
        for (MasterCodeToExcelDto masterCodeToExcelDto : masterCodeToExcelDtoList) {
            String masterCodeKey = masterCodeToExcelDto.getMasterCodeKey();
            boolean result = false;
            if (masterCodeToExcelDto.getCodeCategory().isEmpty()
                    ||masterCodeToExcelDto.getStatus().isEmpty()
                    ||masterCodeToExcelDto.getEffectiveFrom().isEmpty())
                    {
                emptyCode.add(masterCodeToExcelDto.getCodeValue());
                result = true;
            }
            if (!masterCodeService.masterCodeKeyIsExist(masterCodeKey)){
                result = true;
                duplicateCode.add(masterCodeToExcelDto.getCodeValue());
            }
            if (result){
                errorMap.put(MasterCodeConstants.MASTER_CODE_UPLOAD_FILE, "There is an error in the file contents");
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(request,"ERR_CONTENT","SUCCESS");
                ParamUtil.setRequestAttr(request,"ERR_DUPLICATE_CODE",duplicateCode);
                ParamUtil.setRequestAttr(request,"ERR_EMPTY_CODE",emptyCode);
            }else{
                masterCodeService.saveMasterCodeList(masterCodeToExcelDtoList);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            }
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
        SearchResultHelper.doSort(request, filterParameter);
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
        boolean isExist = masterCodeService.masterCodeKeyIsExist(masterCodeDto.getMasterCodeKey());
        if (!isExist){
            validationResult.setHasErrors(true);
        }
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
            if (!isExist){
                validationResult.setHasErrors(true);
                errorMap.put("masterCodeKey", "This master code key has duplicate");
            }
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            ParamUtil.setRequestAttr(request, "codeCategory", ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_CATEGORY));
            return;
        }
        if (isExist){
            masterCodeService.saveMasterCode(masterCodeDto);
        }
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
            mcStatusSelectList.add(new SelectOption(masterCodeDto.getStatus(), MasterCodeUtil.getCodeDesc(masterCodeDto.getStatus())));
            mcStatusSelectList.add(new SelectOption("CMSTAT001", "Active"));
            mcStatusSelectList.add(new SelectOption("CMSTAT002", "Deleted"));
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
        getValueFromPage(masterCodeDto, request);
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
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            return;
        }
        String codeCategory = masterCodeService.findCodeCategoryByDescription(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_CATEGORY));
        masterCodeDto.setCodeCategory(codeCategory);
        masterCodeService.updateMasterCode(masterCodeDto);
        ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
        ParamUtil.setRequestAttr(request, "UPDATED_DATE", new Date());

    }

    private void getValueFromPage(MasterCodeDto masterCodeDto, HttpServletRequest request) throws ParseException {
        masterCodeDto.setMasterCodeKey(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_KEY_CMC));
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
        masterCodeDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
    }

    private void getCategoryValueFromPage(MasterCodeDto masterCodeDto, HttpServletRequest request) throws ParseException {
        masterCodeDto.setMasterCodeId(null);
        masterCodeDto.setFilterValue(ParamUtil.getString(request, "codeCategoryFilterValue"));
        masterCodeDto.setMasterCodeKey(ParamUtil.getString(request, "codeCategoryKey"));
        masterCodeDto.setCodeValue(ParamUtil.getString(request, "codeCategoryValue"));
        masterCodeDto.setCodeDescription(ParamUtil.getString(request, "codeCategoryDescription"));
        masterCodeDto.setStatus(ParamUtil.getString(request, "codeCategoryStatus"));
        masterCodeDto.setRemarks(ParamUtil.getString(request, "codeCategoryRemarks"));
        masterCodeDto.setSequence(StringUtil.isEmpty(ParamUtil.getString(request, "codeCategorySequence")) ? 100 : ParamUtil.getInt(request, "codeCategorySequence"));
        masterCodeDto.setEffectiveFrom(Formatter.parseDate(ParamUtil.getString(request, "categoryEsd")));
        masterCodeDto.setEffectiveTo(Formatter.parseDate(ParamUtil.getString(request, "categoryEed")));
//        masterCodeDto.setVersion(Float.parseFloat("1.0"));
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
        if (file == null){
            errorMap.put(MasterCodeConstants.MASTER_CODE_UPLOAD_FILE, MessageCodeKey.GENERAL_ERR0004);
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
