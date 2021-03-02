package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:10/15/2019 3:39 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigExcel;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.ChecklistConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.ChecklistHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.helper.excel.IrregularExcelWriterUtil;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Delegator(value = "hcsaChklConfigDelegator")
@Slf4j
public class HcsaChklConfigDelegator {

    private static Map<Integer, List<Integer>> excelConfigValueIndex = IaisCommonUtils.genNewHashMap();

    private static Map<Integer, List<Integer>> excelHiddenValueIndex = IaisCommonUtils.genNewHashMap();

    static {
        excelConfigValueIndex.put(1, Collections.singletonList(2));
        excelConfigValueIndex.put(3, Arrays.asList(2, 5));
        excelConfigValueIndex.put(5, Arrays.asList(2,5));
        excelConfigValueIndex.put(7, Arrays.asList(2,5,7));

        excelHiddenValueIndex.put(1, Arrays.asList(15, 16, 17));
    }

    private HcsaChklService hcsaChklService;

    private FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(ChecklistConfigQueryDto.class)
            .searchAttr(HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_SEARCH)
            .resultAttr(HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_RESULT)
            .sortField("config_id").build();

    private List<String> subtypeNames = null;
    private List<String> svcNames = null;

    //Save the section that user added to the current page
    private Set<String> curSecName = null;

    @Autowired
    public HcsaChklConfigDelegator(HcsaChklService hcsaChklService){
        this.hcsaChklService = hcsaChklService;
    }

    /**
     * StartStep: startStep
     * @param bpc
     * @throws IllegalAccessException
     */
    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_CHECKLIST_MANAGEMENT, AuditTrailConsts.FUNCTION_CHECKLIST_CONFIG);
        HttpServletRequest request = bpc.request;

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_SEARCH, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_RESULT, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.SELECTED_ITEM_IN_CONFIG, null);
        ParamUtil.setSessionAttr(request, "routeToItemProcess", null);
        ParamUtil.setSessionAttr(request, "addedItemIdList", null);
        ParamUtil.setSessionAttr(request, "actionBtn", null);

    }

    /**
     * StartStep: prepare
     * @param bpc
     * @throws IllegalAccessException
     */
    public void prepare(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;


        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_COMMON, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_HCI_CODE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_START_DATE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_END_DATE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.SELECTED_ITEM_IN_CONFIG, null);

        ParamUtil.setSessionAttr(request, "configIdAttr", null);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.ACTION_OPERATIONTYPE, null);
        curSecName = IaisCommonUtils.genNewHashSet();

        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(HcsaChecklistConstants.ACTION_CANCEL.equals(currentAction) || HcsaChecklistConstants.BACK_LAST_PAGE_BUTTON.equals(currentAction)){
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, null);
        }

        preSelectOption(request);

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("hcsaconfig", "listChecklistConfig", searchParam);

        SearchResult<ChecklistConfigQueryDto> searchResult =  hcsaChklService.listChecklistConfig(searchParam);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_SEARCH, searchParam);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_RESULT, searchResult);
    }


    /**
     * StartStep: addSectionItem
     * @param bpc
     * @throws IllegalAccessException
     */
    public void addSectionItem(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (HcsaChecklistConstants.BACK_LAST_PAGE_BUTTON.equals(currentAction)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            return;
        }

        String section = ParamUtil.getString(request, "section");
        String sectionDesc = ParamUtil.getString(request, "sectionDesc");

        if(StringUtils.isEmpty(section) || StringUtils.isEmpty(sectionDesc)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("sectionName", "GENERAL_ERR0006"));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        if (curSecName.contains(section)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("sectionName", "CHKL_ERR007"));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        try {
            ChecklistConfigDto conf = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
            if(conf != null){
                List<ChecklistSectionDto> secList = conf.getSectionDtos();
                if(secList == null){
                    secList = IaisCommonUtils.genNewArrayList();
                }

                ChecklistSectionDto cs = new ChecklistSectionDto();

                //going to clear it in submit method
                cs.setId(UUID.randomUUID().toString());

                cs.setSection(section);
                cs.setDescription(sectionDesc);
                cs.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                secList.add(cs);
                secList.sort(Comparator.comparing(ChecklistSectionDto::getOrder, Comparator.nullsFirst(Comparator.naturalOrder())));
                conf.setSectionDtos(secList);
                curSecName.add(section);
            }

            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, conf);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
        }catch (IaisRuntimeException e){
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }
    }


    /**
     * setup option to web page
     * @param request
     */
    private void preSelectOption(HttpServletRequest request){
        this.subtypeNames =  hcsaChklService.listSubTypeName();
        this.svcNames = hcsaChklService.listServiceName();
        List<SelectOption> subTypeOptList = IaisCommonUtils.genNewArrayList();
        List<SelectOption> svcNameOptList = IaisCommonUtils.genNewArrayList();

        for(String sn : subtypeNames){
            subTypeOptList.add(new SelectOption(sn, sn));
        }

        ParamUtil.setSessionAttr(request, "subtypeSelect", (Serializable) subTypeOptList);

        for (String s : svcNames){
            svcNameOptList.add(new SelectOption(s,s));
        }

        ParamUtil.setSessionAttr(request, "svcNameSelect", (Serializable) svcNameOptList);

    }

    /**
     * @AutoStep: doSearch
     * @param:
     * @return:
     * @author: yichen
     */
    public void doSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!HcsaChecklistConstants.ACTION_SEARCH.equals(currentAction)){
            return;
        }

        String common = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE);
        String[] mcb = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE_CHECKBOX);
        String[] tcb = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE_CHECKBOX);
        String svcName = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE);
        String svcSubType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE);

        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE, common);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE_CHECKBOX, mcb);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE_CHECKBOX, tcb);

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        if(!StringUtil.isEmpty(common)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_CONFIG_COMMON, 1, true);
        }


        if(!StringUtil.isEmpty(svcName)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_CONFIG_SERVICE, svcName, true);
        }

        if(!StringUtil.isEmpty(svcSubType)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE, svcSubType, true);
        }

        if (mcb!= null){
            //<#if module??> and ${module} </#if>
            String moduleStr = SqlHelper.constructInCondition("svc.module", mcb.length);
            searchParam.addParam("module", moduleStr);
            int indx = 0;
            for (String s : mcb){
                searchParam.addFilter("svc.module"+indx, s);
                indx++;
            }
        }

        if (tcb!= null){
            //<#if type??> and ${type} </#if>
            String typeStr = SqlHelper.constructInCondition("svc.type", tcb.length);
            searchParam.addParam("type", typeStr);
            int indx = 0;
            for (String s : tcb){
                searchParam.addFilter("svc.type"+indx, s);
                indx++;
            }

        }

    }


    /**
     * @AutoStep: switchAction
     * @param:
     * @return:
     * @author: yichen
     */
    public void switchAction(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
    }

    /**
     * @AutoStep: switchAction
     * @param:
     * @return:
     * @author: yichen
     */
    public void cloneConfig(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        preSelectOption(request);

        String cloneConfId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.CURRENT_MASK_ID);

        ParamUtil.setSessionAttr(request, "configIdAttr", cloneConfId);

        setToSession(request, cloneConfId);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.ACTION_OPERATIONTYPE, HcsaChecklistConstants.ACTION_CLONE);
    }

    private void setToSession(HttpServletRequest request, String value){
        if (!StringUtils.isEmpty(value)){
            ChecklistConfigDto configDto = hcsaChklService.getChecklistConfigById(value);

            List<String> selectedItemIdList = IaisCommonUtils.genNewArrayList();
            configDto.getSectionDtos().forEach(i -> {
                i.getChecklistItemDtos().forEach(j -> {
                    selectedItemIdList.add(j.getItemId());
                });
            });

            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.SELECTED_ITEM_IN_CONFIG, (Serializable) selectedItemIdList);
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, configDto);
        }
    }

    /**
     * @AutoStep: addConfigNextAction
     * @param:
     * @return:
     * @author: yichen
     */
    public void addConfigNextAction(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if ("backLastPage".equals(curAct)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            return;
        }

        String common = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_COMMON);
        String module = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE);
        String type = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE);
        String svcName = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE);
        String hciCode = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_HCI_CODE);
        String svcSubType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE);
        String eftStartDate = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_START_DATE);
        String eftEndDate = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_END_DATE);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE, module);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE, type);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_HCI_CODE, hciCode);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE, svcName);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE, svcSubType);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_START_DATE, eftStartDate);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_END_DATE, eftEndDate);

        try {
            ChecklistConfigDto conf;
            String operationType = (String) ParamUtil.getSessionAttr(request, "operationType");
            if (!StringUtils.isEmpty(operationType) && HcsaChecklistConstants.ACTION_CLONE.equals(operationType)){
                conf = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
                conf.setSvcName(null);
                conf.setEftStartDate(null);
                conf.setEftEndDate(null);
                conf.setSvcSubType(null);
                conf.setCommon(false);
                conf.setId(null);
                conf.setHciCode(null);
                conf.setModule(null);
                conf.setVersion(null);
                conf.setSvcCode(null);
                conf.setType(null);
            }else {
                conf = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
                if(conf == null){
                    conf = new ChecklistConfigDto();
                }
            }

            if ("1".equals(common)) {
                conf.setCommon(true);
                ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_COMMON, "1");
            }else {
                ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_COMMON, null);
            }

            if (module != null){
                conf.setModule(MasterCodeUtil.getCodeDesc(module));
            }

            if (type != null){
                conf.setType(MasterCodeUtil.getCodeDesc(type));
            }

            conf.setSvcName(svcName);
            conf.setSvcSubType(svcSubType);
            conf.setHciCode(hciCode);
            conf.setEftStartDate(eftStartDate);
            conf.setEftEndDate(eftEndDate);
            conf.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

            //field validate
            ValidationResult validationResult = WebValidationHelper.validateProperty(conf, common == null ? "save" : "commonSave");
            if(validationResult != null && validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
                return;
            }else {
                ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, conf);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            }

            //record validate
            boolean isExistsRecord = hcsaChklService.isExistsRecord(conf);
            if (isExistsRecord){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("configCustomValidation", "CHKL_ERR019"));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            }

        } catch (Exception e) {
            log.error("when add section item of config has error ", e);
        }

    }

    /**
     * @AutoStep: saveChecklistConfig
     * @param:
     * @return:
     * @author: yichen
     */
    public void deleteRecord(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String confId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.CURRENT_MASK_ID);

        if (!StringUtils.isEmpty(confId)){
            boolean delSuccess = hcsaChklService.deleteRecord(confId);
            if (!delSuccess){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("configCustomValidation", "CHKL_ERR022"));
            }
        }
    }

    /**
     * AutoStep: doView
     * @param bpc
     * @throws IllegalAccessException
     */
    public void doView(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String confId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.CURRENT_MASK_ID);
        if(!StringUtil.isEmpty(confId)){
            ChecklistConfigDto checklistConfigById = hcsaChklService.getChecklistConfigById(confId);
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, checklistConfigById);
            ParamUtil.setSessionAttr(request, "actionBtn", "dataView");
        }
    }

    /**
     * @AutoStep: removeSection
     * @param:
     * @return:
     * @author: yichen
     */
    public void removeSection(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String value = ParamUtil.getMaskedString(request, HcsaChecklistConstants.PARAM_PAGE_INDEX);

        ChecklistConfigDto conf = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
        if (conf != null){
            List<ChecklistSectionDto> sectionDtos = conf.getSectionDtos();
            Iterator<ChecklistSectionDto> iter = sectionDtos.iterator();
            while (iter.hasNext()){
                String sectionId = iter.next().getId();
                if (value.equals(sectionId)){
                    iter.remove();
                }
            }
        }
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, conf);
    }

    /**
     * @AutoStep: removeSection
     * @param:
     * @return:
     * @author: yichen
     */
    public void removeSectionItem(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String curSecId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.PARAM_PAGE_INDEX);
        String value = ParamUtil.getMaskedString(bpc.request,"sectionItemid");

        ChecklistConfigDto conf = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
        if (conf != null){
            List<ChecklistSectionDto> sectionList = conf.getSectionDtos();
            for (ChecklistSectionDto sec : sectionList){
                if (curSecId.equals(sec.getId())){
                    List<ChecklistItemDto> itemDtoList = sec.getChecklistItemDtos();
                    Iterator<ChecklistItemDto> iter = itemDtoList.iterator();
                    while (iter.hasNext()){
                        String itemId = iter.next().getItemId();
                        if (value.equals(itemId)){
                            iter.remove();
                            break;
                        }
                    }
                }
            }
        }
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, conf);
    }


    /**
     * @AutoStep: submitEditData
     * @param:
     * @return:
     * @author: yichen
     */
    public void submitEditData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        ChecklistConfigDto conf = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
        if (conf == null){
            return;
        }

        String type = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE);
        String module = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE);
        conf.setModule(module);
        conf.setType(type);

        ValidationResult validationResult = WebValidationHelper.validateProperty(conf, "update");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
        }else {
            conf.setType(MasterCodeUtil.getCodeDesc(type));
            conf.setModule(MasterCodeUtil.getCodeDesc(module));
            generateOrderIndex(request, conf.getSectionDtos());

            conf.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            ChecklistConfigDto checklistConfigDto = hcsaChklService.submitConfig(conf);
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, checklistConfigDto);
            ParamUtil.setRequestAttr(request,"ackMsg", MessageUtil.dateIntoMessage("CHKL_ACK004"));
        }
    }

    /**
     * @AutoStep: saveChecklistConfig
     * @param:
     * @return:
     * @author: yichen
     */
    public void loadEditData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String confId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.CURRENT_MASK_ID);
        ParamUtil.setSessionAttr(request, "configIdAttr", confId);

        setToSession(request, confId);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.ACTION_OPERATIONTYPE, HcsaChecklistConstants.ACTION_EDIT);
    }

    /**
     * @AutoStep: prepareAddConfig
     * @param:
     * @return:
     * @author: yichen
     */
    public void prepareAddConfig(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        preSelectOption(request);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.ACTION_OPERATIONTYPE, HcsaChecklistConstants.ACTION_CREATE);
    }


    /**
     * @AutoStep: backToPage
     * @param:
     * @return:
     * @author: yichen
     */
    public void backToPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String operationType = (String) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.ACTION_OPERATIONTYPE);
        if (HcsaChecklistConstants.ACTION_EDIT.equals(operationType)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, null);
        }else {
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        }

    }


    /**
     * @AutoStep: prepareConfigSectionInfo
     * @param:
     * @return:
     * @author: yichen
     */
    public void prepareConfigSectionInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        List<SelectOption> subtypeSelects = IaisCommonUtils.genNewArrayList();
        List<String> subtypeNames = hcsaChklService.listSubTypeName();
        for(String s : subtypeNames){
            subtypeSelects.add(new SelectOption(s, s));
        }
        ParamUtil.setRequestAttr(request, "subtypeSelect", subtypeSelects);
    }

    /**
     * @AutoStep: addChecklistItemNextAction
     * @param:
     * @return:
     * @author: yichen
     */
    public void addChecklistItemNextAction(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        try {
            String[] checked = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX);
            if(checked == null || checked.length <= 0){
                return;
            }

            List<String> unMarkList = IaisCommonUtils.genNewArrayList();
            for (String i : checked){
                unMarkList.add(MaskUtil.unMaskValue(HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX, i));
            }

            List<ChecklistItemDto> necessary = hcsaChklService.listChklItemByItemId(unMarkList);
            ChecklistConfigDto disposition = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
            String currentValidateId = (String) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.PARAM_PAGE_INDEX);
            List<String> selectedItemIdToConfig = (List<String>) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.SELECTED_ITEM_IN_CONFIG);
            if (IaisCommonUtils.isEmpty(selectedItemIdToConfig)){
                selectedItemIdToConfig = IaisCommonUtils.genNewArrayList();
            }

            if(disposition != null && currentValidateId != null){
                List<ChecklistSectionDto> currentSection = disposition.getSectionDtos();
                for(ChecklistSectionDto section : currentSection){
                    if(section.getId().equals(currentValidateId)){
                        List<ChecklistItemDto> collocate = section.getChecklistItemDtos();
                        if(collocate != null && !collocate.isEmpty()){
                            for (ChecklistItemDto addCrumb : necessary){
                                selectedItemIdToConfig.add(addCrumb.getItemId());
                                collocate.add(addCrumb);
                            }
                        }else {
                            section.setChecklistItemDtos(necessary);
                        }
                    }
                }

            }

            selectedItemIdToConfig.addAll(unMarkList);
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.SELECTED_ITEM_IN_CONFIG, (Serializable) selectedItemIdToConfig);
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, disposition);

        }catch (IaisRuntimeException e){
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }
    }

    private void generateOrderIndex(HttpServletRequest request, List<ChecklistSectionDto> sectionDtos){
        for (ChecklistSectionDto sec : sectionDtos){
            String section = sec.getSection();
            String orderStr = ParamUtil.getString(request, section);
            int order =Integer.parseInt(orderStr);
            sec.setOrder(order + 1);
            List<ChecklistItemDto> checklistItemDtos = sec.getChecklistItemDtos();
            if (!IaisCommonUtils.isEmpty(checklistItemDtos)){
                for (ChecklistItemDto item : checklistItemDtos){
                    String itemId = item.getItemId() + orderStr;
                    int itemOrder = Integer.parseInt(ParamUtil.getString(request, itemId));
                    item.setSectionItemOrder(itemOrder + 1);
                }
                checklistItemDtos.sort(Comparator.comparing(ChecklistItemDto::getSectionItemOrder));
            }
        }
    }

    /**
     * @AutoStep: preViewConfig
     * @param:
     * @return:
     * @author: yichen
     */
    public void preViewConfig(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        ChecklistConfigDto preViewConf = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
        if (IaisCommonUtils.isEmpty(preViewConf.getSectionDtos())){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("configErrorMsg", "CHKL_ERR018"));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        List<ChecklistSectionDto> checklistSectionDtoList = preViewConf.getSectionDtos();
        for (ChecklistSectionDto sectionDto : checklistSectionDtoList){
            if (IaisCommonUtils.isEmpty(sectionDto.getChecklistItemDtos())){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("configErrorMsg", "CHKL_ERR017"));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                return;
            }
        }

        ParamUtil.setSessionAttr(request, "actionBtn", "submitView");
        generateOrderIndex(request, preViewConf.getSectionDtos());
        preViewConf.getSectionDtos().sort(Comparator.comparing(ChecklistSectionDto::getOrder));
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, preViewConf);
        ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.YES);

    }


    /**
     * @AutoStep: routeToItemProcess
     * @param: curValhId : current validate id
     * @return:
     * @author: yichen
     */
    public void routeToItemProcess(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);

        // go checklist checklist item page
        String curValhId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.PARAM_PAGE_INDEX);
        ParamUtil.setSessionAttr(request, "routeToItemProcess", curAct);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_PAGE_INDEX, curValhId);
    }

    /**
     * AutoStep: doSort
     * @param bpc
     * @throws IllegalAccessException
     */
    public void doSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,bpc.request);
    }

    /**
     * AutoStep: doPage
     * @param bpc
     * @throws IllegalAccessException
     */
    public void doPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam,bpc.request);
    }


    /**
     * AutoStep: preUploadData
     * @param bpc
     * @throws IllegalAccessException
     */
    public void preUploadData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, "switchUploadPage", "Checklist Config Upload");
    }

    /**
     * AutoStep: setRequest
     * @param bpc
     * @throws IllegalAccessException
     */
    public void setRequest(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.setMultipartAction(request);
    }

    /**
     * AutoStep: setMultipart
     * @param bpc
     * @throws IllegalAccessException
     */
    public void setMultipart(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.setMultipartAction(request);
    }


    /**
     * AutoStep: preUploadTemplate
     * @param bpc
     * @throws IllegalAccessException
     */
    public void preUploadTemplate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, "switchUploadPage", "Checklist Config Upload");
    }



    /**
     * AutoStep: updateTemplate
     * @param bpc
     * @throws IllegalAccessException
     */
    public void updateTemplate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        MultipartFile file = mulReq.getFile("selectedFile");

        boolean fileHasError = ChecklistHelper.validateFile(request, file);
        if (fileHasError){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        try {
            File toFile = FileUtils.multipartFileToFile(file);
            List<String> ids = FileUtils.transformToList(toFile, 1, excelHiddenValueIndex);
            String prevId = ids.get(0);
            String nextId = ids.get(1);
            String version = ids.get(2);

            ChecklistConfigDto configInSystem = hcsaChklService.getChecklistConfigById(nextId);
            if (configInSystem != null){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR019"));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
                return;
            }

            List<ChecklistConfigExcel> item = FileUtils.transformToJavaBean(toFile, ChecklistConfigExcel.class);
            List<String> confInfoList = FileUtils.transformToList(toFile, 1, excelConfigValueIndex);
            log.info(StringUtil.changeForLog("update config template ids" + ids));
            ChecklistConfigDto excelTemplate = convertToConfigByTemplate(confInfoList);
            excelTemplate.setNextId(nextId);
            excelTemplate.setVersion(Integer.valueOf(version));
            excelTemplate.setWebAction(HcsaChecklistConstants.UPDATE);
            excelTemplate.setId(prevId);
            excelTemplate.setExcelTemplate(item);
            FileUtils.deleteTempFile(toFile);

            boolean hasTemplateError = ChecklistHelper.validateTemplate(request, excelTemplate);
            if (hasTemplateError){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
                return;
            }

            List<ErrorMsgContent> errMsgContentList = hcsaChklService.updateConfigTemplate(excelTemplate);
            errMsgContentList = errMsgContentList.stream().filter(i -> !i.getErrorMsgList().isEmpty()).collect(Collectors.toList());
            ChecklistHelper.replaceErrorMsgContentMasterCode(errMsgContentList);
            ParamUtil.setRequestAttr(request, "messageContent", errMsgContentList);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
        } catch (Exception e) {
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR011"));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            log.error(e.getMessage(), e);
        }


    }


    /**
     * AutoStep: exportConfigTemplate
     * @param bpc
     * @throws IllegalAccessException
     */
    public void exportConfigTemplate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String confId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.CURRENT_MASK_ID);
        log.info(StringUtil.changeForLog("exportConfigTemplate ++++++++ config id" + confId));
        if (!StringUtils.isEmpty(confId)){
            ChecklistConfigDto config = hcsaChklService.getChecklistConfigById(confId);
            try {
                String[] val = {config.isCommon() ? "Yes" : "No", config.getModule(), config.getType(), config.getSvcName(), config.getSvcSubType(), config.getHciCode(), config.getEftStartDate(), config.getEftEndDate()};

                List<ChecklistConfigExcel> updateItem = hcsaChklService.convertToUploadTemplateByConfig(config);

                File inputFile = ResourceUtils.getFile("classpath:template/Checklist_Config_Update_Template.xlsx");

                log.info(StringUtil.changeForLog("before export config template" + inputFile.getPath()));

                File configInfoTemplate = IrregularExcelWriterUtil.writerToExcelByIndex(inputFile, 1, val, excelConfigValueIndex);

                log.info(StringUtil.changeForLog("export temp config template " + configInfoTemplate.getPath()));

                String prevConfigId = config.getId();
                String nextConfigId = hcsaChklService.callProceduresGenUUID();
                String currentVersion = config.getVersion().toString();
                String[] hiddenVal = {prevConfigId, nextConfigId, currentVersion};

                File versionFile = IrregularExcelWriterUtil.writerToExcelByIndex(configInfoTemplate, 1, hiddenVal, excelHiddenValueIndex, true);

                log.info(StringUtil.changeForLog("after export config template" + versionFile.getPath()));

                File latest = ExcelWriter.writerToExcel(updateItem, ChecklistConfigExcel.class, versionFile,  "Checklist_Config_Update_Template", true, false);
                FileUtils.writeFileResponseProcessContent(request, latest);

                FileUtils.deleteTempFile(configInfoTemplate);
                FileUtils.deleteTempFile(versionFile);
                FileUtils.deleteTempFile(latest);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            }
        }else {
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
        }
    }

    private ChecklistConfigDto convertToConfigByTemplate(List<String> data){
        ChecklistConfigDto excelTpl = new ChecklistConfigDto();
        boolean isCommon = "Yes".equals(data.get(0)) ? true : false;
        if (isCommon){
            excelTpl.setCommon(true);
        }else {
            excelTpl.setModule(data.get(1));
            excelTpl.setType(data.get(2));
            excelTpl.setSvcName(data.get(3));
            excelTpl.setSvcSubType(data.get(4));
            excelTpl.setHciCode(data.get(5));
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(data.get(3));
            if (hcsaServiceDto != null){
                excelTpl.setSvcCode(hcsaServiceDto.getSvcCode());
            }
        }

        excelTpl.setEftStartDate(data.get(6));
        excelTpl.setEftEndDate(data.get(7));
        return excelTpl;
    }

    /**
     * AutoStep: createUploadConfig
     * @param bpc
     * @throws IllegalAccessException
     */
    public void createUploadConfig(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        MultipartFile file = mulReq.getFile("selectedFile");

        boolean fileHasError = ChecklistHelper.validateFile(request, file);
        if (fileHasError){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        try {
            File toFile = FileUtils.multipartFileToFile(file);
            List<ChecklistConfigExcel> confItemExcelTpl = FileUtils.transformToJavaBean(toFile, ChecklistConfigExcel.class);
            List<String> configInfo = FileUtils.transformToList(toFile, 1, excelConfigValueIndex);
            FileUtils.deleteTempFile(toFile);

            //uncheck ArrayIndexOutOfBoundsException
            ChecklistConfigDto confExcelTpl = convertToConfigByTemplate(configInfo);
            confExcelTpl.setExcelTemplate(confItemExcelTpl);
            confExcelTpl.setWebAction(HcsaChecklistConstants.CREATE);

            boolean hasTemplateError = ChecklistHelper.validateTemplate(request, confExcelTpl);
            if (hasTemplateError){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
                return;
            }

            //record validate
            boolean existsRecord = hcsaChklService.isExistsRecord(confExcelTpl);
            if (existsRecord){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR019"));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
                return;
            }

            List<ErrorMsgContent> errorMsgContentList = hcsaChklService.createConfigTemplate(confExcelTpl);
            errorMsgContentList = errorMsgContentList.stream().filter(i -> !i.getErrorMsgList().isEmpty()).collect(Collectors.toList());
            ChecklistHelper.replaceErrorMsgContentMasterCode(errorMsgContentList);
            ParamUtil.setRequestAttr(request, "messageContent", errorMsgContentList);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID,IaisEGPConstant.YES);

        } catch (Exception e) {
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR011"));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            log.error(e.getMessage(), e);
        }
    }




    /**
     * @AutoStep: preViewConfig
     * @param:
     * @return:
     * @author: yichen
     */
    public void submitConfig(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        try {
            ChecklistConfigDto conf = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
            if(conf != null){
                String operationType = (String) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.ACTION_OPERATIONTYPE);
                if (HcsaChecklistConstants.ACTION_CLONE.equals(operationType)){
                    ParamUtil.setRequestAttr(request,"ackMsg", MessageUtil.dateIntoMessage("CHKL_ACK004"));
                    conf.setWebAction(HcsaChecklistConstants.CLONE);
                }else if (HcsaChecklistConstants.ACTION_EDIT.equals(operationType)){
                    ParamUtil.setRequestAttr(request,"ackMsg", MessageUtil.dateIntoMessage("CHKL_ERR021"));
                    conf.setWebAction(HcsaChecklistConstants.UPDATE);
                }else if (HcsaChecklistConstants.ACTION_CREATE.equals(operationType)){
                    conf.setWebAction(HcsaChecklistConstants.CREATE);
                    ParamUtil.setRequestAttr(request,"ackMsg", MessageUtil.dateIntoMessage("CHKL_ACK004"));
                }

                conf.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                ChecklistConfigDto checklistConfigDto = hcsaChklService.submitConfig(conf);
                ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, checklistConfigDto);
            }

            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_PAGE_INDEX, null);
        }catch (NullPointerException e){
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }
    }
}
