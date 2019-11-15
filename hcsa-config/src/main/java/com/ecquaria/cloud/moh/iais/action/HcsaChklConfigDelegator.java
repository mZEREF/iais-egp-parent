package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:10/15/2019 3:39 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;

@Delegator(value = "hcsaChklConfigDelegator")
@Slf4j
public class HcsaChklConfigDelegator {
//
//    private HcsaChklService hcsaChklService;
//    private FilterParameter filterParameter;
//
//    private List<String> subtypeNames = null;
//    private List<String> svcNames = null;
//
//    @Autowired
//    public HcsaChklConfigDelegator(HcsaChklService hcsaChklService, FilterParameter filterParameter){
//        this.hcsaChklService = hcsaChklService;
//        this.filterParameter = filterParameter;
//    }
//
//    /**
//     * StartStep: startStep
//     * @param bpc
//     * @throws IllegalAccessException
//     */
//    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
//        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
//        HttpServletRequest request = bpc.request;
//        IaisEGPHelper.clearSessionAttr(request, HcsaChecklistConstants.class);
//    }
//
//    /**
//     * StartStep: prepare
//     * @param bpc
//     * @throws IllegalAccessException
//     */
//    public void prepare(BaseProcessClass bpc) throws IllegalAccessException {
//        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
//        HttpServletRequest request = bpc.request;
//        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
//        if(HcsaChecklistConstants.ACTION_CANCEL.equals(currentAction) || "backLastPage".equals(currentAction)){
//            IaisEGPHelper.clearSessionAttr(request, ChecklistConfigQueryDto.class);
//        }
//
//        preSelectOption(request);
//
//        filterParameter.setClz(ChecklistConfigQueryDto.class);
//        filterParameter.setSearchAttr(HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_SEARCH);
//        filterParameter.setResultAttr(HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_RESULT);
//        filterParameter.setSortField("item_id");
//
//        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
//        QueryHelp.setMainSql("hcsaconfig", "listChecklistConfig", searchParam);
//
//        SearchResult searchResult =  hcsaChklService.listChecklistConfig(searchParam);
//
//
//
//        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_SEARCH, searchParam);
//        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_RESULT, searchResult);
//    }
//
//    /**
//     * StartStep: addSectionItem
//     * @param bpc
//     * @throws IllegalAccessException
//     */
//    public void addSectionItem(BaseProcessClass bpc){
//        HttpServletRequest request = bpc.request;
//        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
//        if ("backLastPage".equals(currentAction)){
//            return;
//        }
//
//        String section = ParamUtil.getString(request, "section");
//        String sectionDesc = ParamUtil.getString(request, "sectionDesc");
//
//        log.info("add section item");
//
//        if(StringUtils.isEmpty(section) || StringUtils.isEmpty(sectionDesc)){
//            return;
//        }
//
//        try {
//            ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
//            if(configDto != null){
//                List<ChecklistSectionDto> sectionDtos = configDto.getSectionDtos();
//                if(sectionDtos == null){
//                    sectionDtos = new ArrayList<>();
//                }
//
//                ChecklistSectionDto checklistSectionDto = new ChecklistSectionDto();
//                checklistSectionDto.setId(UUID.randomUUID().toString());
//                checklistSectionDto.setSection(section);
//                checklistSectionDto.setDescription(sectionDesc);
//                checklistSectionDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
//                sectionDtos.add(checklistSectionDto);
//                sectionDtos.sort(Comparator.comparing(ChecklistSectionDto::getOrder, Comparator.nullsFirst(Comparator.naturalOrder())));
//                configDto.setSectionDtos(sectionDtos);
//            }
//
//            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, configDto);
//        }catch (NullPointerException e){
//            log.error(e.getMessage(), e);
//            throw new IaisRuntimeException(e);
//        }
//    }
//
//
//    /**
//     * setup option to web page
//     * @param request
//     */
//    private void preSelectOption(HttpServletRequest request){
//        this.subtypeNames =  hcsaChklService.listSubTypeName();
//        this.svcNames = hcsaChklService.listServiceName();
//        List<SelectOption> subtypeSelect = new ArrayList<>();
//        List<SelectOption> svcNameSelect = new ArrayList<>();
//
//        for(String sn : subtypeNames){
//            subtypeSelect.add(new SelectOption(sn, sn));
//        }
//
//        ParamUtil.setRequestAttr(request, "subtypeSelect", subtypeSelect);
//
//        for (String s : svcNames){
//            svcNameSelect.add(new SelectOption(s,s));
//        }
//
//        ParamUtil.setRequestAttr(request, "svcNameSelect", svcNameSelect);
//
//    }
//
//    /**
//     * @AutoStep: doSearch
//     * @param:
//     * @return:
//     * @author: yichen
//     */
//    public void doSearch(BaseProcessClass bpc){
//        HttpServletRequest request = bpc.request;
//        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
//
//        String common = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE);
//
//        String[] moduleCheckBox = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE_CHECKBOX);
//        String[] typeCheckBox = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE_CHECKBOX);
//
//        String svcName = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE);
//        String svcSubType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE);
//
//        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
//
//        if(!StringUtil.isEmpty(common)){
//            searchParam.addFilter(HcsaChecklistConstants.PARAM_CONFIG_MODULE, 1, true);
//        }
//
//        if(!StringUtil.isEmpty(svcName)){
//            searchParam.addFilter(HcsaChecklistConstants.PARAM_CONFIG_SERVICE, svcName, true);
//        }
//
//        if(!StringUtil.isEmpty(svcSubType)){
//            searchParam.addFilter(HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE, svcSubType, true);
//        }
//
//    }
//
//
//    /**
//     * @AutoStep: switchAction
//     * @param:
//     * @return:
//     * @author: yichen
//     */
//    public void switchAction(BaseProcessClass bpc){
//        HttpServletRequest request = bpc.request;
//        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
//    }
//
//
//    private Boolean judgeConfigIsCommon(String var){
//        return true;
//    }
//
//    /**
//     * @AutoStep: addConfigNextAction
//     * @param:
//     * @return:
//     * @author: yichen
//     */
//    public void addConfigNextAction(BaseProcessClass bpc){
//        HttpServletRequest request = bpc.request;
//        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
//        String common = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE);
//        String type = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE);
//        String svcName = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE);
//        String svcSubType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE);
//
//        boolean isCommon = judgeConfigIsCommon(common);
//
//        ChecklistConfigDto configDto = new ChecklistConfigDto();
//        configDto.setCommon(isCommon);
//        configDto.setType(type);
//        configDto.setSvcName(svcName);
//        configDto.setSvcSubType(svcSubType);
//        ValidationResult validationResult = WebValidationHelper.validateProperty(configDto, "addConfigInfo");
//        if(validationResult != null && validationResult.isHasErrors()){
//            Map<String,String> errorMap = validationResult.retrieveAll();
//            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,errorMap);
//            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
//        }else {
//            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, configDto);
//            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
//        }
//    }
//
//    /**
//     * @AutoStep: saveChecklistConfig
//     * @param:
//     * @return:
//     * @author: yichen
//     */
//    public void saveChecklistConfig(BaseProcessClass bpc){
//        HttpServletRequest request = bpc.request;
//    }
//
//    /**
//     * @AutoStep: prepareAddConfig
//     * @param:
//     * @return:
//     * @author: yichen
//     */
//    public void prepareAddConfig(BaseProcessClass bpc){
//        HttpServletRequest request = bpc.request;
//
//        preSelectOption(request);
//    }
//
//
//    /**
//     * @AutoStep: prepareConfigSectionInfo
//     * @param:
//     * @return:
//     * @author: yichen
//     */
//    public void prepareConfigSectionInfo(BaseProcessClass bpc){
//        HttpServletRequest request = bpc.request;
//        List<SelectOption> subtypeSelects = new ArrayList<>();
//
//        List<String> subtypeNames = hcsaChklService.listSubTypeName();
//        for(String s : subtypeNames){
//            subtypeSelects.add(new SelectOption(s, s));
//        }
//
//        ParamUtil.setRequestAttr(request, "subtypeSelect", subtypeSelects);
//    }
//
//
//    /**
//     * @AutoStep: addSectionNextAction
//     * @param:
//     * @return:
//     * @author: yichen
//     */
//    public void addSectionNextAction(BaseProcessClass bpc){
//        HttpServletRequest request = bpc.request;
//
//        String section = ParamUtil.getString(request, "section");
//        String sectionDesc = ParamUtil.getString(request, "sectionDesc");
//        String sectionOrder = ParamUtil.getString(request, "sectionOrder");
//        ChecklistSectionDto checklistSectionDto = new ChecklistSectionDto();
//        checklistSectionDto.setSection(section);
//        checklistSectionDto.setDescription(sectionDesc);
//        checklistSectionDto.setOrder(Integer.valueOf(sectionOrder));
//        checklistSectionDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
//
//        ValidationResult validationResult = WebValidationHelper.validateProperty(checklistSectionDto, "addConfigInfo");
//        if(validationResult != null && validationResult.isHasErrors()){
//            Map<String,String> errorMap = validationResult.retrieveAll();
//            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,errorMap);
//            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
//        }else {
//            try {
//                ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
//
//                ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, configDto);
//            }catch (NullPointerException e){
//                log.error(e.getMessage(), e);
//                throw new IaisRuntimeException(e);
//            }
//        }
//
//    }
//
//    /**
//     * @AutoStep: addChecklistItemNextAction
//     * @param:
//     * @return:
//     * @author: yichen
//     */
//    public void addChecklistItemNextAction(BaseProcessClass bpc){
//        HttpServletRequest request = bpc.request;
//        try {
//            String[] checkBoxItemId = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX);
//            if(checkBoxItemId == null || checkBoxItemId.length <= 0){
//                return;
//            }
//
//            List<ChecklistItemDto> necessary = hcsaChklService.listChklItemByItemId(Arrays.asList(checkBoxItemId));
//            ChecklistConfigDto disposition = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
//            String currentValidateId = (String) ParamUtil.getSessionAttr(request, "currentValidateId");
//            if(disposition != null && currentValidateId != null){
//                List<ChecklistSectionDto> currentSection = disposition.getSectionDtos();
//                for(ChecklistSectionDto section : currentSection){
//                    if(section.getId().equals(currentValidateId)){
//                        List<ChecklistItemDto> collocate = section.getChecklistItemDtos();
//                        if(collocate != null && !collocate.isEmpty()){
//                            for (ChecklistItemDto addCrumb : necessary){
//                                collocate.add(addCrumb);
//                            }
//                        }else {
//                            section.setChecklistItemDtos(necessary);
//                        }
//                    }
//                }
//
//            }
//
//            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, disposition);
//        }catch (NullPointerException e){
//            log.error(e.getMessage(), e);
//            throw new IaisRuntimeException(e);
//        }
//
//
//
//    }
//
//    /**
//     * @AutoStep: preViewConfig
//     * @param:
//     * @return:
//     * @author: yichen
//     */
//    public void preViewConfig(BaseProcessClass bpc){
//        HttpServletRequest request = bpc.request;
//    }
//
//
//    /**
//     * @AutoStep: routeToItemProcess
//     * @param:
//     * @return:
//     * @author: yichen
//     */
//    public void routeToItemProcess(BaseProcessClass bpc){
//        HttpServletRequest request = bpc.request;
//
//        // go checklist checklist item page
//        String currentValidateId = ParamUtil.getMaskedString(request, "currentValidateId");
//        ParamUtil.setSessionAttr(request, "currentValidateId", currentValidateId);
//    }
//
//
//    /**
//     * @AutoStep: preViewConfig
//     * @param:
//     * @return:
//     * @author: yichen
//     */
//    public void submitConfig(BaseProcessClass bpc){
//        HttpServletRequest request = bpc.request;
//        try {
//            ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
//
//            if(configDto != null){
//                hcsaChklService.submitConfig(configDto);
//            }
//
//            ParamUtil.setSessionAttr(request, "currentValidateId", null);
//        }catch (NullPointerException e){
//            log.error(e.getMessage(), e);
//            throw new IaisRuntimeException(e);
//        }
//    }
//


}
