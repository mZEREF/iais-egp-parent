package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
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
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HalpSearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.util.CopyUtil;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

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
    private SystemParamConfig systemParamConfig;

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
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG, AuditTrailConsts.FUNCTION_MASTER_CODE_MANAGEMENT);
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
        selectCodeStatusList.add(new SelectOption("CMSTAT001", "Active"));
        selectCodeStatusList.add(new SelectOption("CMSTAT003", "Inactive"));
        List<MasterCodeCategoryDto> masterCodeCategoryDtoList = masterCodeService.getAllCodeCategory();
        List<SelectOption> mcCategorySelectList = IaisCommonUtils.genNewArrayList();
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
            String category = masterCodeQueryDto.getCodeCategory();
            MasterCodeCategoryDto masterCodeCategoryDto = masterCodeService.getMasterCodeCategory(category);
            masterCodeQueryDto.setCodeCategory(masterCodeCategoryDto.getCategoryDescription());
            Integer isCanEdit = masterCodeCategoryDto.getIsEditable();
            if (isCanEdit == 0){
                masterCodeQueryDto.setIsCentrallyManage(isCanEdit);
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
            if(masterCodeDto != null){
                masterCodeDto.setFilterValue(masterCodeDto.getCodeValue());
                ParamUtil.setSessionAttr(request, "MasterCodeView", masterCodeDto);
            }
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
        String mcuperrErrMsg8 = MessageUtil.getMessageDesc("MCUPERR008");
        List<MasterCodeToExcelDto> masterCodeToExcelDtos = masterCodeService.findAllMasterCode();
        String type = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        if (!SystemAdminBaseConstants.SAVE_ACTION.equals(type)) {
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
            return;
        }
        MasterCodeDto masterCodeDto = (MasterCodeDto) ParamUtil.getSessionAttr(request, "MasterCodeView");
        getCategoryValueFromPage(masterCodeDto, request);
        if (StringUtil.isEmpty(masterCodeDto.getVersion())){
            masterCodeDto.setVersion(1d);
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        Optional<MasterCodeToExcelDto> cartOptional = Optional.empty();
        if (!StringUtil.isEmpty(masterCodeDto.getCodeCategory()) && !StringUtil.isEmpty(masterCodeDto.getCodeValue())){
            cartOptional = masterCodeToExcelDtos.stream().filter(item -> item.getCodeValue().equals(masterCodeDto.getCodeValue())
                    && item.getCodeCategory().equals(masterCodeDto.getCodeCategory())).findFirst();
        }
        ValidationResult validationResult = WebValidationHelper.validateProperty(masterCodeDto, SystemAdminBaseConstants.SAVE_ACTION);
        if (masterCodeDto.getEffectiveFrom() != null && masterCodeDto.getEffectiveTo() != null) {
            if (!masterCodeDto.getEffectiveFrom().before(masterCodeDto.getEffectiveTo())) {
                validationResult.setHasErrors(true);
            }
            if (AppConsts.COMMON_STATUS_IACTIVE.equals(masterCodeDto.getStatus())){
                if (masterCodeDto.getEffectiveFrom().before(new Date())){
                    validationResult.setHasErrors(true);
                }
            }
        }
        if (masterCodeDto.getSequence() != null) {
            if (masterCodeDto.getSequence() == -1 || masterCodeDto.getSequence() == -2) {
                validationResult.setHasErrors(true);
            }
        }
        if (cartOptional.isPresent()) {
            validationResult.setHasErrors(true);
        }
        if (validationResult != null && validationResult.isHasErrors()) {
            errorMap = validationResult.retrieveAll();
            if (masterCodeDto.getEffectiveFrom() != null && masterCodeDto.getEffectiveTo() != null) {
                if (AppConsts.COMMON_STATUS_IACTIVE.equals(masterCodeDto.getStatus())){
                    if (masterCodeDto.getEffectiveFrom().before(new Date())){
                        validationResult.setHasErrors(true);
                        String errMsg = MessageUtil.getMessageDesc("MCUPERR007");
                        //The effective date of inactive data must be a future time
                        errorMap.put("effectiveFrom", errMsg);
                    }
                }
                if (AppConsts.COMMON_STATUS_ACTIVE.equals(masterCodeDto.getStatus())){
                    if (masterCodeDto.getEffectiveTo().before(new Date())){
                        validationResult.setHasErrors(true);
                        String errMsg = MessageUtil.getMessageDesc("MCUPERR009");
                        //The effective date of inactive data must be a future time
                        errorMap.put("effectiveTo", errMsg);
                    }
                }
                if (!masterCodeDto.getEffectiveFrom().before(masterCodeDto.getEffectiveTo())) {
                    validationResult.setHasErrors(true);
                    String errMsg = MessageUtil.getMessageDesc("EMM_ERR004");
                    errorMap.put("effectiveTo", errMsg);
                }
            }
            if (masterCodeDto.getSequence() != null) {
                if (masterCodeDto.getSequence() == -1 || masterCodeDto.getSequence() == -2) {
                    errorMap.put("sequence", mcuperrErrMsg8);
                    masterCodeDto.setSequence(null);
                }

            }
            if (cartOptional.isPresent()) {
                validationResult.setHasErrors(true);
                String errMsg = MessageUtil.replaceMessage("SYSPAM_ERROR0005","Code Value","Record Name");
                errorMap.put("codeValue", errMsg);
            }
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            return;
        }
        boolean isEffect = isEffect(masterCodeDto);
        log.info(StringUtil.changeForLog("isEffect:"+isEffect));
        if(!isEffect){
            masterCodeDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
        }
        String codeCategory = masterCodeService.findCodeCategoryByDescription(masterCodeDto.getCodeCategory());
        masterCodeDto.setCodeCategory(codeCategory);
        MasterCodeDto msDto = masterCodeService.saveMasterCode(masterCodeDto);
        //eic
        List<MasterCodeDto> syncMasterCodeList = IaisCommonUtils.genNewArrayList();
        msDto.setUpdateAt(new Date());
        syncMasterCodeList.add(msDto);
        masterCodeService.syncMasterCodeFe(syncMasterCodeList);
        ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
        Date date = new Date();
        String dateStr = Formatter.formatDateTime(date);
        String dateReplace = dateStr.replace(" "," at ");
        String ackMsg = MessageUtil.replaceMessage("ACKMCM001",dateReplace,"Date");
        ParamUtil.setRequestAttr(request,"CREATE_ACKMSG",ackMsg);
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
            searchParam.addFilter(MasterCodeConstants.MASTER_CODE_DESCRIPTION,  codeDescription ,true);
        } else {
            searchParam.removeFilter(MasterCodeConstants.MASTER_CODE_DESCRIPTION);
        }
        if (!StringUtil.isEmpty(codeValue)) {
            searchParam.addFilter(MasterCodeConstants.MASTER_CODE_VALUE,  codeValue ,true);
        } else {
            searchParam.removeFilter(MasterCodeConstants.MASTER_CODE_VALUE);
        }
        if (!StringUtil.isEmpty(filterValue)) {
            searchParam.addFilter(SystemAdminBaseConstants.MASTER_CODE_FILTER_VALUE,  filterValue ,true);
        } else {
            searchParam.removeFilter(SystemAdminBaseConstants.MASTER_CODE_FILTER_VALUE);
        }
        if (codeEffFrom != null && codeEffTo != null) {
            if (codeEffFrom.compareTo(codeEffTo) < 0) {
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
                String errStr = MessageUtil.getMessageDesc("EMM_ERR004");
                ParamUtil.setRequestAttr(request, "ERR_EED", errStr);
                Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put("MC_SEARCH_APP_ERR",errStr);
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
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
        List<MasterCodeToExcelDto> mctList = IaisCommonUtils.genNewArrayList();
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(request, MasterCodeConstants.SEARCH_PARAM);
        searchParam.setPageSize(Integer.MAX_VALUE);
        SearchResult<MasterCodeQueryDto> searchResult = masterCodeService.doQuery(searchParam);
        searchResult.getRows().forEach(h ->{
            MasterCodeToExcelDto mct = new MasterCodeToExcelDto();
            String category = h.getCodeCategory();
            MasterCodeCategoryDto masterCodeCategoryDto = masterCodeService.getMasterCodeCategory(category);
            mct.setCodeCategory(masterCodeCategoryDto.getCategoryDescription());
            mct.setSequence(String.valueOf(h.getSequence() / 1000));
            mct.setCodeDescription(h.getCodeDescription());
            mct.setFilterValue(h.getFilterValue());
            mct.setCodeValue(h.getCodeValue());
            mct.setVersion(h.getVersion());
            mct.setEffectiveFrom(h.getEffectiveStartDate());
            mct.setEffectiveTo(h.getEffectiveEndDate());
            mct.setRemakes(h.getRemarks());
            mct.setStatus(MasterCodeUtil.getCodeDesc(h.getStatus()));
            mctList.add(mct);
        });

        if (mctList.size()> 0) {
            try {
                File file =  ExcelWriter.writerToExcel(mctList, MasterCodeToExcelDto.class, "Master_Code_File");
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
        List<MasterCodeToExcelDto> masterCodeToExcelDtos = masterCodeService.findAllMasterCode();
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
        File toFile = FileUtils.multipartFileToFile(file, request.getSession().getId());
        try{
            List<MasterCodeToExcelDto> masterCodeToExcelDtoList = FileUtils.transformToJavaBean(toFile, MasterCodeToExcelDto.class);
            boolean result = false;
            List<Map<String,Set<String>>> errResult = IaisCommonUtils.genNewArrayList();
            for (MasterCodeToExcelDto masterCodeToExcelDto : masterCodeToExcelDtoList) {
                if(StringUtil.isEmpty(masterCodeToExcelDto.getCodeCategory())&&
                        StringUtil.isEmpty(masterCodeToExcelDto.getMasterCodeId())&&
                        StringUtil.isEmpty(masterCodeToExcelDto.getMasterCodeKey())&&
                        StringUtil.isEmpty(masterCodeToExcelDto.getCodeValue())&&
                        StringUtil.isEmpty(masterCodeToExcelDto.getCodeDescription())&&
                        StringUtil.isEmpty(masterCodeToExcelDto.getRemakes())&&
                        StringUtil.isEmpty(masterCodeToExcelDto.getSequence())&&
                        StringUtil.isEmpty(masterCodeToExcelDto.getStatus())&&
                        StringUtil.isEmpty(masterCodeToExcelDto.getVersion())&&
                        masterCodeToExcelDto.getEffectiveFrom()==null&&
                        masterCodeToExcelDto.getEffectiveTo()==null
                ){
                    continue;
                }
                Map<String,Set<String>> errMap = IaisCommonUtils.genNewHashMap();
                Set<String> errItems = IaisCommonUtils.genNewHashSet();
                Date codeEffFrom = null;
                Date codeEffTo = null;
                if (StringUtil.isEmpty(masterCodeToExcelDto.getCodeCategory()))
                {
                    String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0046","Code Category","field");
                    errItems.add(errMsg);
                    result = true;
                }
                if (StringUtil.isEmpty(masterCodeToExcelDto.getCodeDescription())){
                    String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0046","Code Description","field");
                    errItems.add(errMsg);
                    result = true;
                }else if(masterCodeToExcelDto.getCodeDescription().length() >255) {
                    Map<String,String> stringMap = new HashMap<>(2);
                    stringMap.put("field","Code Description");
                    stringMap.put("maxlength","255");
                    errItems.add( MessageUtil.getMessageDesc("GENERAL_ERR0041",stringMap));
                    result = true;
                }
                if (StringUtil.isEmpty(masterCodeToExcelDto.getCodeValue())){
                    String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0046","Code Value","field");
                    errItems.add(errMsg);
                    result = true;
                }else if(masterCodeToExcelDto.getCodeValue().length() >25) {
                    Map<String,String> stringMap = new HashMap<>(2);
                    stringMap.put("field","Code Value");
                    stringMap.put("maxlength","25");
                    errItems.add( MessageUtil.getMessageDesc("GENERAL_ERR0041",stringMap));
                    result = true;
                }
                if (StringUtil.isEmpty(masterCodeToExcelDto.getStatus())){
                    String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0046","Status","field");
                    errItems.add(errMsg);
                    result = true;
                }
                String errSequenceMsg = MessageUtil.replaceMessage("GENERAL_ERR0046","Sequence","field");
                if (StringUtil.isEmpty(masterCodeToExcelDto.getSequence())){
                    errItems.add(errSequenceMsg);
                    result = true;
                }else {
                    try{
                        if(masterCodeToExcelDto.getSequence().length() >3){
                            Map<String,String> stringMap = new HashMap<>(2);
                            stringMap.put("field","Sequence");
                            stringMap.put("maxlength","3");
                            errItems.add( MessageUtil.getMessageDesc("GENERAL_ERR0041",stringMap));
                            result = true;
                        }else {
                            Integer.parseInt(masterCodeToExcelDto.getSequence());
                        }
                    }catch (Exception e){
                        errItems.add(errSequenceMsg);
                        result = true;
                    }


                }
                String errStartDtMsg = MessageUtil.replaceMessage("GENERAL_ERR0046","Effective Start Date","field");
                if (StringUtil.isEmpty(masterCodeToExcelDto.getEffectiveFrom())){
                    errItems.add(errStartDtMsg);
                    result = true;
                }else{
                    try{
                        codeEffFrom = masterCodeToExcelDto.getEffectiveFrom();
                    }catch (Exception e){
                        errItems.add(errStartDtMsg);
                        result = true;
                    }
                }
                String errEndDtMsg = MessageUtil.replaceMessage("GENERAL_ERR0046","Effective End Date","field");
                if (StringUtil.isEmpty(masterCodeToExcelDto.getEffectiveTo())){
                    errItems.add(errEndDtMsg);
                    result = true;
                }else{
                    try{
                        codeEffTo = masterCodeToExcelDto.getEffectiveTo();
                    }catch (Exception e){
                        errItems.add(errEndDtMsg);
                        result = true;
                    }
                }
                if (codeEffFrom != null && codeEffTo != null){
                    if ("Inactive".equals(masterCodeToExcelDto.getStatus())){
                        if (codeEffFrom.before(new Date())){
                            String errMsg = MessageUtil.getMessageDesc("MCUPERR007");
                            errItems.add(errMsg);
                            result = true;
                        }
                    }
                    if ("Active".equals(masterCodeToExcelDto.getStatus())){
                        if (codeEffTo.before(new Date())){
                            String errMsg = MessageUtil.getMessageDesc("MCUPERR009");
                            errItems.add(errMsg);
                            result = true;
                        }
                    }
                    if (codeEffFrom.compareTo(codeEffTo) >= 0) {
                        String errMsg = MessageUtil.getMessageDesc("EMM_ERR004");
                        errItems.add(errMsg);
                        result = true;
                    }
                }
                Optional<MasterCodeToExcelDto> cartOptional = Optional.empty();
                if(!StringUtil.isEmpty(masterCodeToExcelDto.getCodeCategory())){
                    String codeCategory = masterCodeService.findCodeCategoryByDescription(masterCodeToExcelDto.getCodeCategory());
                    if (StringUtil.isEmpty(codeCategory)){
                        String errMsg = MessageUtil.getMessageDesc("MCUPERR001");
                        errItems.add(errMsg);
                        result = true;
                    }else{
                        masterCodeToExcelDto.setCodeCategory(codeCategory);
                    }
                }
                if (!StringUtil.isEmpty(masterCodeToExcelDto.getCodeCategory()) && !StringUtil.isEmpty(masterCodeToExcelDto.getCodeValue())){
                    cartOptional = masterCodeToExcelDtos.stream().filter(item -> item.getCodeValue().equals(masterCodeToExcelDto.getCodeValue())
                            && item.getCodeCategory().equals(masterCodeToExcelDto.getCodeCategory())).findFirst();
                }
                List<String> codeValueNewList = IaisCommonUtils.genNewArrayList();
                AtomicInteger count= new AtomicInteger();
                masterCodeToExcelDtoList.forEach(h -> {
                    codeValueNewList.add(h.getCodeValue());
                    if(masterCodeToExcelDto.getCodeValue().equals(h.getCodeValue())){
                        count.getAndIncrement();
                    }
                });
                if(count.get()>1){
                    errItems.add(MessageUtil.getMessageDesc("CHKL_ERR047"));
                    result = true;
                }

                if (!StringUtil.isEmpty(masterCodeToExcelDto.getFilterValue())){
                    if (cartOptional.isPresent()) {
                        MasterCodeToExcelDto masterCodeToExcelDto1 =  cartOptional.get();
                        if(masterCodeToExcelDto1.getFilterValue() != null){
                            log.info(StringUtil.changeForLog("masterCodeToExcelDto1  ===========> " + masterCodeToExcelDto1.getFilterValue()));
                        }
                        if(masterCodeToExcelDto1.getRemakes() != null) {
                            log.info(StringUtil.changeForLog("masterCodeToExcelDto1  ===========> " + masterCodeToExcelDto1.getRemakes()));
                        }
                        log.info(StringUtil.changeForLog("masterCodeToExcelDto1  ===========> " + JsonUtil.parseToJson(masterCodeToExcelDto1)));
                        if(StringUtil.isEmpty(masterCodeToExcelDto1.getFilterValue())){
                            String errMsg = MessageUtil.getMessageDesc("MCUPERR006");
                            errItems.add(errMsg);
                            result = true;
                        }
                    }

                    List<String> codeValueList = IaisCommonUtils.genNewArrayList();
                    masterCodeToExcelDtos.forEach(h -> {
                        codeValueList.add(h.getCodeValue());
                    });
                    if (!codeValueList.contains(masterCodeToExcelDto.getFilterValue())){
                        String errMsg = MessageUtil.getMessageDesc("MCUPERR002");
                        errItems.add(errMsg);
                        result = true;
                    }
                }

                if (!StringUtil.isEmpty(masterCodeToExcelDto.getStatus())){
                    if ("Active".equals(masterCodeToExcelDto.getStatus())){
                        masterCodeToExcelDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    }else if ("Deleted".equals(masterCodeToExcelDto.getStatus())){
                        masterCodeToExcelDto.setStatus(AppConsts.COMMON_STATUS_DELETED);
                    }else if ("Inactive".equals(masterCodeToExcelDto.getStatus())){
                        masterCodeToExcelDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    }else{
                        String errMsg = MessageUtil.getMessageDesc("MCUPERR011");
                        errItems.add(errMsg);
                        result = true;
                    }
                }
                if (!StringUtil.isEmpty(masterCodeToExcelDto.getVersion())){
                    String uploadVersion = masterCodeToExcelDto.getVersion();
                    if( !StringUtil.stringIsFewDecimal(uploadVersion,2)){
                        errItems.add(MessageUtil.getMessageDesc("GENERAL_ERR0027"));
                        result = true;
                    }else {
                        if(uploadVersion.length() > 4){
                            Map<String,String> stringMap = new HashMap<>(2);
                            stringMap.put("field","Version");
                            stringMap.put("maxlength","4");
                            errItems.add( MessageUtil.getMessageDesc("GENERAL_ERR0041",stringMap));
                            result = true;
                        }else {
                            double inputVer = Double.parseDouble(uploadVersion);
                            if(inputVer >= 10){
                                String errMsg = MessageUtil.getMessageDesc("MCUPERR010");
                                errItems.add(errMsg);
                                result = true;
                            }else {
                                if (cartOptional.isPresent()) {
                                    MasterCodeToExcelDto masterCodeToExcelDto1 =  cartOptional.get();
                                    String version = masterCodeToExcelDto1.getVersion();
                                    if (!StringUtil.isEmpty(version)){
                                        double versionDou = Double.parseDouble(version);
                                        if (versionDou >= inputVer){
                                            String errMsg = MessageUtil.getMessageDesc("SYSPAM_ERROR0006");
                                            errItems.add(errMsg);
                                            result = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else {
                    String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0046","Version","field");
                    errItems.add(errMsg);
                    result = true;
                }
                if( !StringUtil.isEmpty(masterCodeToExcelDto.getRemakes()) && masterCodeToExcelDto.getRemakes().length() >255){
                    Map<String,String> stringMap = new HashMap<>(2);
                    stringMap.put("field","Remarks");
                    stringMap.put("maxlength","255");
                    errItems.add( MessageUtil.getMessageDesc("GENERAL_ERR0041",stringMap));
                    result = true;
                }
                if (cartOptional.isPresent()) {
                    MasterCodeToExcelDto masterCodeToExcelDto1 =  cartOptional.get();
                    masterCodeToExcelDto.setMasterCodeId(masterCodeToExcelDto1.getMasterCodeId());
                    masterCodeToExcelDto.setMasterCodeKey(masterCodeToExcelDto1.getMasterCodeKey());
                }
                if (errItems.size()>0){
                    errMap.put(masterCodeToExcelDto.getCodeValue(),errItems);
                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                    errResult.add(errMap);
                }
            }
            if (!result){
                Date date = new Date();
                String dateStr = Formatter.formatDateTime(date);
                String dateReplace = dateStr.replace(" "," at ");
                String ackMsg = MessageUtil.replaceMessage("ACKMCM004",dateReplace,"Date");
                ParamUtil.setRequestAttr(request,"UPLOAD_ACKMSG",ackMsg);
                List<MasterCodeToExcelDto> masterCodeToExcelDtoSaveList=IaisCommonUtils.genNewArrayList();
                for (MasterCodeToExcelDto masterCodeToExcelDto : masterCodeToExcelDtoList) {
                    if(StringUtil.isEmpty(masterCodeToExcelDto.getCodeCategory())&&
                            StringUtil.isEmpty(masterCodeToExcelDto.getMasterCodeId())&&
                            StringUtil.isEmpty(masterCodeToExcelDto.getMasterCodeKey())&&
                            StringUtil.isEmpty(masterCodeToExcelDto.getCodeValue())&&
                            StringUtil.isEmpty(masterCodeToExcelDto.getCodeDescription())&&
                            StringUtil.isEmpty(masterCodeToExcelDto.getRemakes())&&
                            StringUtil.isEmpty(masterCodeToExcelDto.getSequence())&&
                            StringUtil.isEmpty(masterCodeToExcelDto.getStatus())&&
                            StringUtil.isEmpty(masterCodeToExcelDto.getVersion())&&
                            masterCodeToExcelDto.getEffectiveFrom()==null&&
                            masterCodeToExcelDto.getEffectiveTo()==null
                    ){
                        continue;
                    }
                    masterCodeToExcelDtoSaveList.add(masterCodeToExcelDto);
                }
                List<MasterCodeDto> syncMasterCodeList =masterCodeService.saveMasterCodeList(masterCodeToExcelDtoSaveList);
                masterCodeService.syncMasterCodeFe(syncMasterCodeList);
            }
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(request,"ERR_CONTENT","SUCCESS");
            ParamUtil.setSessionAttr(request,"ERR_RESULT_LIST_MAP",(Serializable) errResult);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            String errMsg = MessageUtil.getMessageDesc("MCUPERR004");
            errorMap.put(MasterCodeConstants.MASTER_CODE_UPLOAD_FILE, errMsg);
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
        }
    }

    private void fileValidation(HttpServletRequest request,String originalFilename,Map<String, String> errorMap){
        if (!StringUtil.isEmpty(originalFilename)) {
            if (originalFilename.length() > 100) {
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0022");
                errorMap.put(MasterCodeConstants.MASTER_CODE_UPLOAD_FILE, errMsg);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            }
        }
    }

    public void checkUploadFile(BaseProcessClass bpc) {
        List<Map<String,List<String>>> errResult = (List<Map<String, List<String>>>) ParamUtil.getSessionAttr(bpc.request,"ERR_RESULT_LIST_MAP");
        if (errResult != null && errResult.size()>0){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
        }else{
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(bpc.request, "UPLOAD_DATE", new Date());
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
    public void doDelete(BaseProcessClass bpc) throws ParseException {
        logAboutStart("doDelete");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        Date date = new Date();
        String dateStr = Formatter.formatDateTime(date);
        String dateReplace = dateStr.replace(" "," at ");
        String ackMsg = "";
        if ("doDelete".equals(type)) {
            String masterCodeId = ParamUtil.getString(bpc.request, SystemAdminBaseConstants.CRUD_ACTION_VALUE);
            MasterCodeDto masterCodeDto = masterCodeService.findMasterCodeByMcId(masterCodeId);
            if (masterCodeDto != null){
                if (masterCodeDto.getEffectiveFrom().before(new Date())) {
                    String codeCategory = masterCodeService.findCodeCategoryByDescription(masterCodeDto.getCodeCategory());
                    masterCodeDto.setCodeCategory(codeCategory);
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    Date yesterday = Formatter.changeDateToMax(c.getTime());
                    masterCodeDto.setEffectiveTo(yesterday);
                    masterCodeDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    masterCodeService.updateMasterCode(masterCodeDto);
                    List<MasterCodeDto> syncMasterCodeList = IaisCommonUtils.genNewArrayList();
                    masterCodeDto.setUpdateAt(new Date());
                    syncMasterCodeList.add(masterCodeDto);
                    masterCodeService.syncMasterCodeFe(syncMasterCodeList);
                    ackMsg = MessageUtil.replaceMessage("ACKMCM003",dateReplace,"Date");
                } else {
                    masterCodeService.deleteMasterCodeById(masterCodeId);
                    List<MasterCodeDto> syncMasterCodeList = IaisCommonUtils.genNewArrayList();
                    masterCodeDto.setUpdateAt(new Date());
                    masterCodeDto.setNeedDelete(true);
                    syncMasterCodeList.add(masterCodeDto);
                    masterCodeService.syncMasterCodeFe(syncMasterCodeList);
                    ackMsg = MessageUtil.replaceMessage("ACKMCM005",dateReplace,"Date");
                }
                MasterCodeUtil.refreshCache();
            }
        }
        ParamUtil.setRequestAttr(request,"DELETE_ACKMSG",ackMsg);
    }

    public void prepareCode(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        prepareSelect(request);
        List<MasterCodeCategoryDto> masterCodeCategoryDtoList = masterCodeService.getCodeCategoryIsEdit();
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
        String mcuperrErrMsg8 = MessageUtil.getMessageDesc("MCUPERR008");
        List<MasterCodeToExcelDto> masterCodeToExcelDtos = masterCodeService.findAllMasterCode();
        String type = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        if (!SystemAdminBaseConstants.SAVE_ACTION.equals(type)) {
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
            return;
        }
        MasterCodeDto masterCodeDto = new MasterCodeDto();
        getValueFromPage(masterCodeDto, request);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult validationResult = WebValidationHelper.validateProperty(masterCodeDto, SystemAdminBaseConstants.SAVE_ACTION);
        Optional<MasterCodeToExcelDto> cartOptional = Optional.empty();
        if (!StringUtil.isEmpty(masterCodeDto.getCodeCategory()) && !StringUtil.isEmpty(masterCodeDto.getCodeValue())){
            cartOptional = masterCodeToExcelDtos.stream().filter(item -> item.getCodeValue().equals(masterCodeDto.getCodeValue())
                    && item.getCodeCategory().equals(masterCodeDto.getCodeCategory())).findFirst();
        }
        if (AppConsts.COMMON_STATUS_IACTIVE.equals(masterCodeDto.getStatus())){
            if (masterCodeDto.getEffectiveFrom() != null) {
                if (masterCodeDto.getEffectiveFrom().before(new Date())){
                    validationResult.setHasErrors(true);
                }
            }
        }
        if (AppConsts.COMMON_STATUS_ACTIVE.equals(masterCodeDto.getStatus())){
            if (masterCodeDto.getEffectiveTo() != null){
                if (masterCodeDto.getEffectiveTo().before(new Date())){
                    validationResult.setHasErrors(true);
                }
            }
        }
        if (masterCodeDto.getEffectiveFrom() != null && masterCodeDto.getEffectiveTo() != null) {
            if (!masterCodeDto.getEffectiveFrom().before(masterCodeDto.getEffectiveTo())) {
                validationResult.setHasErrors(true);
            }
        }
        if (cartOptional.isPresent()) {
            validationResult.setHasErrors(true);
        }
        if (masterCodeDto.getSequence() != null){
            if (masterCodeDto.getSequence() == -1 || masterCodeDto.getSequence() == -2){
                validationResult.setHasErrors(true);
            }
        }
        if (validationResult != null && validationResult.isHasErrors()) {
            errorMap = validationResult.retrieveAll();
            if (masterCodeDto.getSequence() != null){
                if (masterCodeDto.getSequence() == -1 || masterCodeDto.getSequence() == -2){
                    errorMap.put("sequence", mcuperrErrMsg8);
                    masterCodeDto.setSequence(null);
                }
            }
            if (AppConsts.COMMON_STATUS_IACTIVE.equals(masterCodeDto.getStatus())){
                if (masterCodeDto.getEffectiveFrom() != null){
                    if (masterCodeDto.getEffectiveFrom().before(new Date())){
                        validationResult.setHasErrors(true);
                        String errMsg = MessageUtil.getMessageDesc("MCUPERR007");
                        //The effective date of inactive data must be a future time
                        errorMap.put("effectiveFrom", errMsg);
                    }
                }
            }
            if (AppConsts.COMMON_STATUS_ACTIVE.equals(masterCodeDto.getStatus())){
                if (masterCodeDto.getEffectiveTo() != null){
                    if (masterCodeDto.getEffectiveTo().before(new Date())){
                        validationResult.setHasErrors(true);
                        String errMsg = MessageUtil.getMessageDesc("MCUPERR009");
                        //The effective date of inactive data must be a future time
                        errorMap.put("effectiveTo", errMsg);
                    }
                }
            }
            if (masterCodeDto.getEffectiveFrom() != null && masterCodeDto.getEffectiveTo() != null) {
                if (!masterCodeDto.getEffectiveFrom().before(masterCodeDto.getEffectiveTo())) {
                    String errMsg = MessageUtil.getMessageDesc("EMM_ERR004");
                    errorMap.put("effectiveTo", errMsg);
                }
            }
            if (cartOptional.isPresent()) {
                validationResult.setHasErrors(true);
                String errMsg = MessageUtil.replaceMessage("SYSPAM_ERROR0005","Code Value","Record Name");
                errorMap.put("codeValue", errMsg);
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            }
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            ParamUtil.setRequestAttr(request, "codeCategory", ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_CATEGORY));
            return;
        }
        boolean isEffect = isEffect(masterCodeDto);
        log.info(StringUtil.changeForLog("isEffect:"+isEffect));
        if(!isEffect){
            masterCodeDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
        }
        MasterCodeDto msDto = masterCodeService.saveMasterCode(masterCodeDto);
        //eic
        List<MasterCodeDto> syncMasterCodeList = IaisCommonUtils.genNewArrayList();
        msDto.setUpdateAt(new Date());
        syncMasterCodeList.add(msDto);
        masterCodeService.syncMasterCodeFe(syncMasterCodeList);
        MasterCodeUtil.refreshCache();
        ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
        Date date = new Date();
        String dateStr = Formatter.formatDateTime(date);
        String dateReplace = dateStr.replace(" "," at ");
        String ackMsg = MessageUtil.replaceMessage("ACKMCM001",dateReplace,"Date");
        ParamUtil.setRequestAttr(request,"CREATE_ACKMSG",ackMsg);

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
        String errorFlag = (String)ParamUtil.getRequestAttr(bpc.request, "errorFlag");
        List<SelectOption> mcStatusSelectList = IaisCommonUtils.genNewArrayList();
        mcStatusSelectList.add(new SelectOption("CMSTAT001", "Active"));
        mcStatusSelectList.add(new SelectOption("CMSTAT003", "Inactive"));
        ParamUtil.setRequestAttr(bpc.request, "mcStatusSelectList", mcStatusSelectList);
        if(!AppConsts.FALSE.equals(errorFlag)){
            String masterCodeId = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_VALUE);
            if (!masterCodeId.isEmpty()) {
                MasterCodeDto masterCodeDto = masterCodeService.findMasterCodeByMcId(masterCodeId);
                ParamUtil.setSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR, masterCodeDto);
            }
        }
    }

    /**
     * AutoStep: doEdit
     *
     * @param bpc
     * @throws
     */
    public void doEdit(BaseProcessClass bpc) throws ParseException, CloneNotSupportedException {
        logAboutStart("doEdit");
        HttpServletRequest request = bpc.request;
        String mcuperrErrMsg8 = MessageUtil.getMessageDesc("MCUPERR008");
        String type = ParamUtil.getString(request, SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        if (!SystemAdminBaseConstants.EDIT_ACTION.equals(type)) {
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
            return;
        }
        MasterCodeDto oldMasterCodeDto = (MasterCodeDto) ParamUtil.getSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR);
        MasterCodeDto masterCodeDto = (MasterCodeDto) CopyUtil.copyMutableObject(oldMasterCodeDto);
        getEditValueFromPage(masterCodeDto, request);
        if (StringUtil.isEmpty(masterCodeDto.getVersion())){
            masterCodeDto.setVersion(1d);
        }

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult validationEditResult = WebValidationHelper.validateProperty(masterCodeDto, "edit");
        if(validationEditResult != null) {
            errorMap = validationEditResult.retrieveAll();
        }
        if(AppConsts.COMMON_STATUS_IACTIVE.equals(masterCodeDto.getStatus())){
            LocalDate newFromDate = transferLocalDate(masterCodeDto.getEffectiveFrom());
            LocalDate newToDate = transferLocalDate(masterCodeDto.getEffectiveTo());
            LocalDate nowDate = LocalDate.now();
            boolean newIsEffect = isEffect(newFromDate,newToDate,nowDate);
            if (newIsEffect){
                String errMsg = MessageUtil.getMessageDesc("MCUPERR005");
                validationEditResult.setHasErrors(true);
                errorMap.put("status", errMsg);
            }
        }
        if (AppConsts.COMMON_STATUS_ACTIVE.equals(masterCodeDto.getStatus())){
            if (masterCodeDto.getEffectiveFrom() != null){
                if (masterCodeDto.getEffectiveTo() != null){
                    if (masterCodeDto.getEffectiveTo().before(new Date())){
                        validationEditResult.setHasErrors(true);
                    }
                }
            }
        }
        if (masterCodeDto.getEffectiveFrom() != null && masterCodeDto.getEffectiveTo() != null) {
            if (!masterCodeDto.getEffectiveFrom().before(masterCodeDto.getEffectiveTo())) {
                validationEditResult.setHasErrors(true);
            }
        }
        if (masterCodeDto.getSequence() != null) {
            if (masterCodeDto.getSequence() == -1 || masterCodeDto.getSequence() == -2) {
                validationEditResult.setHasErrors(true);
            }
        }
        if (validationEditResult != null && validationEditResult.isHasErrors()) {
            logAboutStart("Edit validation");
            if(AppConsts.COMMON_STATUS_IACTIVE.equals(masterCodeDto.getStatus())){
                errorMap.remove("effectiveFrom");
                errorMap.remove("effectiveTo");
            }
            if (AppConsts.COMMON_STATUS_IACTIVE.equals(masterCodeDto.getStatus())){
                if (masterCodeDto.getEffectiveFrom() != null){
                    if (masterCodeDto.getEffectiveFrom().before(new Date())){
                        validationEditResult.setHasErrors(true);
                        String errMsg = MessageUtil.getMessageDesc("MCUPERR007");
                        //The effective date of inactive data must be a future time
                        errorMap.put("effectiveFrom", errMsg);
                    }
                }
            }
            if (AppConsts.COMMON_STATUS_ACTIVE.equals(masterCodeDto.getStatus())){
                if ((masterCodeDto.getEffectiveTo() != null)){
                    if (masterCodeDto.getEffectiveTo().before(new Date())){
                        validationEditResult.setHasErrors(true);
                        String errMsg = MessageUtil.getMessageDesc("MCUPERR009");
                        //The effective date of inactive data must be a future time
                        errorMap.put("effectiveTo", errMsg);
                    }
                }
            }
            if (masterCodeDto.getEffectiveFrom() != null && masterCodeDto.getEffectiveTo() != null) {
                if (!masterCodeDto.getEffectiveFrom().before(masterCodeDto.getEffectiveTo())) {
                    String errMsg = MessageUtil.getMessageDesc("EMM_ERR004");
                    errorMap.put("effectiveTo", errMsg);
                }
            }
            if (masterCodeDto.getSequence() != null) {
                if (masterCodeDto.getSequence() == -1 || masterCodeDto.getSequence() == -2) {
                    errorMap.put("sequence", mcuperrErrMsg8);
                    masterCodeDto.setSequence(null);
                }
            }
            if(errorMap != null && errorMap.size() > 0){
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR, masterCodeDto);
                ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
                ParamUtil.setRequestAttr(request, "errorFlag", AppConsts.FALSE);
                return;
            }
        }
        List<MasterCodeDto> syncMasterCodeList = IaisCommonUtils.genNewArrayList();
        if(AppConsts.COMMON_STATUS_IACTIVE.equals(masterCodeDto.getStatus())){
            //inactive all
            List<MasterCodeDto> masterCodeDtos = masterCodeService.inactiveMsterCode(masterCodeDto.getMasterCodeKey());
            syncMasterCodeList.addAll(masterCodeDtos);
            String codeCategory = masterCodeService.findCodeCategoryByDescription(masterCodeDto.getCodeCategory());
            masterCodeDto.setCodeCategory(codeCategory);

            masterCodeService.updateMasterCode(masterCodeDto);
        }else{
            //update old
            LocalDate oldFromDate = transferLocalDate(oldMasterCodeDto.getEffectiveFrom());
            LocalDate oldToDate = transferLocalDate(oldMasterCodeDto.getEffectiveTo());
            LocalDate newFromDate = transferLocalDate(masterCodeDto.getEffectiveFrom());
            LocalDate newToDate = transferLocalDate(masterCodeDto.getEffectiveTo());
            LocalDate nowDate = LocalDate.now();
            //new master code can effect
            boolean newIsEffect = isEffect(newFromDate,newToDate,nowDate);
            boolean newCodeActive = newIsEffect && AppConsts.COMMON_STATUS_ACTIVE.equals(masterCodeDto.getStatus());
            boolean oldCodeExpired;
            if(AppConsts.COMMON_STATUS_ACTIVE.equals(oldMasterCodeDto.getStatus())){
                boolean oldIsEffect = isEffect(oldFromDate,oldToDate,nowDate);
                if(oldIsEffect){
                    oldCodeExpired = false;
                }else{
                    oldCodeExpired = true;
                }
            }else{
                oldCodeExpired = true;
            }
            if(oldCodeExpired || newCodeActive){
                oldMasterCodeDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                //inactive all
                List<MasterCodeDto> masterCodeDtos = masterCodeService.inactiveMsterCode(masterCodeDto.getMasterCodeKey());
                syncMasterCodeList.addAll(masterCodeDtos);
            }
            String codeCategory = masterCodeService.findCodeCategoryByDescription(oldMasterCodeDto.getCodeCategory());
            oldMasterCodeDto.setCodeCategory(codeCategory);
            oldMasterCodeDto =  masterCodeService.updateMasterCode(oldMasterCodeDto);
            syncMasterCodeList.add(oldMasterCodeDto);
            //create new
            masterCodeDto.setMasterCodeId(null);
            Double version =  masterCodeDto.getVersion();
            if (StringUtil.isEmpty(version)){
                masterCodeDto.setVersion(1d);
            }else{
                //get max version ms
                MasterCodeDto maxMsDto = masterCodeService.getMaxVersionMsDto(oldMasterCodeDto.getMasterCodeKey());
                Double maxVersion = maxMsDto.getVersion();
                if (maxVersion != null){
                    masterCodeDto.setVersion(maxMsDto.getVersion() + 1);
                }else{
                    masterCodeDto.setVersion(1d);
                }
            }
            if(nowDate.isBefore(newFromDate) || nowDate.isAfter(newToDate)){
                masterCodeDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            }
            String codeCategory2 = masterCodeService.findCodeCategoryByDescription(masterCodeDto.getCodeCategory());
            masterCodeDto.setCodeCategory(codeCategory2);
            masterCodeDto = masterCodeService.updateMasterCode(masterCodeDto);
            syncMasterCodeList.add(masterCodeDto);
        }
        //eic update fe
        Date now = new Date();
        for(MasterCodeDto masterCodeDto1:syncMasterCodeList){
            masterCodeDto1.setUpdateAt(now);
        }
        masterCodeService.syncMasterCodeFe(syncMasterCodeList);
        MasterCodeUtil.refreshCache();

        ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
        Date date = new Date();
        String dateStr = Formatter.formatDateTime(date);
        String dateReplace = dateStr.replace(" "," at ");
        String ackMsg = MessageUtil.replaceMessage("ACKMCM002",dateReplace,"Date");
        ParamUtil.setRequestAttr(request,"UPDATE_ACKMSG",ackMsg);

    }

    private void getEditValueFromPage(MasterCodeDto masterCodeDto, HttpServletRequest request) throws ParseException {
        String codeSequenceEd = ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_SEQUENCE_ED);
        masterCodeDto.setCodeValue(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_VALUE_ED));
        masterCodeDto.setCodeDescription(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_DESCRIPTION_ED));
        masterCodeDto.setStatus(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_STATUS_ED));
        masterCodeDto.setRemarks(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_REMARKS_ED));
        if (StringUtil.isEmpty(codeSequenceEd)){
            masterCodeDto.setSequence(null);
        }else{
            if (!isDouble(codeSequenceEd)) {
                masterCodeDto.setSequence(-1);
            }else{
                double codeCategorySequenceInt = ParamUtil.getDouble(request, MasterCodeConstants.MASTER_CODE_SEQUENCE_ED) * 1000;
                if (codeCategorySequenceInt < 0){
                    masterCodeDto.setSequence(-2);
                }else{
                    int i =  (int)codeCategorySequenceInt;
                    masterCodeDto.setSequence(i);
                }

            }
        }
        masterCodeDto.setEffectiveFrom(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_FROM_ED)));
        masterCodeDto.setEffectiveTo(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_TO_ED)));
        masterCodeDto.setIsEditable(1);
        masterCodeDto.setIsCentrallyManage(1);
        masterCodeDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
    }

    private void getValueFromPage(MasterCodeDto masterCodeDto, HttpServletRequest request) throws ParseException {
        String codeSequenceCMC = ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_SEQUENCE_CMC);
        masterCodeDto.setCodeValue(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_VALUE_CMC));
        masterCodeDto.setCodeCategory(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_CATEGORY_CMC));
        masterCodeDto.setCodeDescription(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_DESCRIPTION_CMC));
        masterCodeDto.setFilterValue(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_FILTER_VALUE_CMC));
        masterCodeDto.setStatus(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_STATUS_CMC));
        masterCodeDto.setRemarks(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_REMARKS_CMC));
        if (StringUtil.isEmpty(codeSequenceCMC)){
            masterCodeDto.setSequence(null);
        }else{
            if (!isDouble(codeSequenceCMC)) {
                masterCodeDto.setSequence(-1);
            }else{
                double codeCategorySequenceInt = ParamUtil.getDouble(request, MasterCodeConstants.MASTER_CODE_SEQUENCE_CMC) * 1000;
                if (codeCategorySequenceInt < 0){
                    masterCodeDto.setSequence(-2);
                }else{
                    int i =  (int)codeCategorySequenceInt;
                    masterCodeDto.setSequence(i);
                }

            }
        }
        masterCodeDto.setVersion(StringUtil.isEmpty(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_VERSION_CMC)) ? null : ParamUtil.getDouble(request, MasterCodeConstants.MASTER_CODE_VERSION_CMC));
        masterCodeDto.setEffectiveFrom(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_FROM_CMC)));
        masterCodeDto.setEffectiveTo(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_TO_CMC)));
        masterCodeDto.setIsEditable(1);
        masterCodeDto.setIsCentrallyManage(1);
        masterCodeDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
    }

    private void getCategoryValueFromPage(MasterCodeDto masterCodeDto, HttpServletRequest request) throws ParseException {
        String codeCategorySequence = ParamUtil.getString(request, "codeCategorySequence");
        masterCodeDto.setMasterCodeId(null);
        masterCodeDto.setCodeValue(ParamUtil.getString(request, "codeCategoryValue"));
        masterCodeDto.setCodeDescription(ParamUtil.getString(request, "codeCategoryDescription"));
        masterCodeDto.setStatus(ParamUtil.getString(request, "codeCategoryStatus"));
        masterCodeDto.setRemarks(ParamUtil.getString(request, "codeCategoryRemarks"));
        if (StringUtil.isEmpty(codeCategorySequence)){
            masterCodeDto.setSequence(null);
        }else{
            if (!isDouble(codeCategorySequence)) {
                masterCodeDto.setSequence(-1);
            }else{
                double codeCategorySequenceInt = ParamUtil.getDouble(request, "codeCategorySequence") * 1000;
                if (codeCategorySequenceInt < 0){
                    masterCodeDto.setSequence(-2);
                }else{
                    int i =  (int)codeCategorySequenceInt;
                    masterCodeDto.setSequence(i);
                }

            }
        }
        masterCodeDto.setEffectiveFrom(Formatter.parseDate(ParamUtil.getString(request, "categoryEsd")));
        masterCodeDto.setEffectiveTo(Formatter.parseDate(ParamUtil.getString(request, "categoryEed")));
        masterCodeDto.setIsEditable(1);
        masterCodeDto.setIsCentrallyManage(1);
        masterCodeDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
    }

    private void logAboutStart(String methodName) {
        log.debug(StringUtil.changeForLog("**** The  " + methodName + "  Start ****"));
    }

    private static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    private void prepareSelect(HttpServletRequest request) {
        List<SelectOption> selectCodeStatusList = IaisCommonUtils.genNewArrayList();
        selectCodeStatusList.add(new SelectOption("CMSTAT001", "Active"));
        selectCodeStatusList.add(new SelectOption("CMSTAT003", "Inactive"));
        ParamUtil.setRequestAttr(request, "codeStatus", selectCodeStatusList);
    }

    private Map<String, String> validationFile(HttpServletRequest request, MultipartFile file){
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        if (file==null || file.isEmpty()){
            errorMap.put(MasterCodeConstants.MASTER_CODE_UPLOAD_FILE, "GENERAL_ERR0020");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }else if(file.getOriginalFilename()!=null){
            String originalFilename = file.getOriginalFilename();
            fileValidation(request,originalFilename,errorMap);
        }

        String originalFileName = file.getOriginalFilename();
        if (!FileUtils.isExcel(originalFileName)){
            errorMap.put(MasterCodeConstants.MASTER_CODE_UPLOAD_FILE, "CHKL_ERR041");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }

        if (FileUtils.outFileSize(file.getSize())){
            String errMsg= MessageUtil.replaceMessage("GENERAL_ERR0043", String.valueOf(systemParamConfig.getUploadFileLimit()),"configNum");
            errorMap.put(MasterCodeConstants.MASTER_CODE_UPLOAD_FILE, errMsg);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }
        return errorMap;
    }

    private boolean isEffect(LocalDate fromDate,LocalDate toDate,LocalDate nowDate){
        boolean leftFlag;
        boolean rightFlag;
        if(fromDate == null){
            leftFlag = true;
        }else{
            leftFlag = nowDate.isEqual(fromDate) || nowDate.isAfter(fromDate);
        }

        if(toDate == null){
            rightFlag = true;
        }else{
            rightFlag = nowDate.isEqual(toDate) || nowDate.isBefore(toDate);
        }
        return leftFlag && rightFlag;
    }

    private boolean isEffect(MasterCodeDto masterCodeDto){
        LocalDate fromDate = transferLocalDate(masterCodeDto.getEffectiveFrom());
        LocalDate toDate = transferLocalDate(masterCodeDto.getEffectiveTo());
        LocalDate nowDate = LocalDate.now();
        return isEffect(fromDate,toDate,nowDate);
    }

    private LocalDate transferLocalDate(Date date){
        LocalDate localDate = null;
        if(date != null){
            localDate = LocalDate.parse(DateUtil.formatDate(date,Formatter.DATE), DateTimeFormatter.ofPattern(Formatter.DATE));
        }
        return localDate;
    }
}
