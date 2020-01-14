package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:10/15/2019 3:39 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Delegator(value = "hcsaChklConfigDelegator")
@Slf4j
public class HcsaChklConfigDelegator {

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
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
        HttpServletRequest request = bpc.request;

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_SEARCH, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_RESULT, null);
        ParamUtil.setSessionAttr(request, "addedItemIdList", null);
        ParamUtil.setSessionAttr(request, "actionBtn", null);

    }

    /**
     * StartStep: prepare
     * @param bpc
     * @throws IllegalAccessException
     */
    public void prepare(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, "h2TitleAttr", null);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_COMMON, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_HCI_CODE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_START_DATE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_END_DATE, null);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.ACTION_OPERATIONTYPE, null);
        curSecName = new HashSet<>();

        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(HcsaChecklistConstants.ACTION_CANCEL.equals(currentAction) || HcsaChecklistConstants.BACK_LAST_PAGE_BUTTON.equals(currentAction)){
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, null);
        }

        preSelectOption(request);

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("hcsaconfig", "listChecklistConfig", searchParam);

        SearchResult searchResult =  hcsaChklService.listChecklistConfig(searchParam);


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
            Map<String,String> errorMap = new HashMap<>(1);
            errorMap.put("sectionName","ERR0009");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        if (curSecName.contains(section)){
            Map<String,String> errorMap = new HashMap<>(1);
            errorMap.put("sectionName","CHKL_ERR007");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }


        try {
            ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
            if(configDto != null){
                List<ChecklistSectionDto> sectionDtos = configDto.getSectionDtos();
                if(sectionDtos == null){
                    sectionDtos = new ArrayList<>();
                }

                ChecklistSectionDto checklistSectionDto = new ChecklistSectionDto();

                //going to clear it in submit method
                checklistSectionDto.setId(UUID.randomUUID().toString());

                checklistSectionDto.setSection(section);
                checklistSectionDto.setDescription(sectionDesc);
                checklistSectionDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                sectionDtos.add(checklistSectionDto);
                sectionDtos.sort(Comparator.comparing(ChecklistSectionDto::getOrder, Comparator.nullsFirst(Comparator.naturalOrder())));
                configDto.setSectionDtos(sectionDtos);

                curSecName.add(section);
            }

            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, configDto);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
        }catch (NullPointerException e){
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
        List<SelectOption> subtypeSelect = new ArrayList<>();
        List<SelectOption> svcNameSelect = new ArrayList<>();

        for(String sn : subtypeNames){
            subtypeSelect.add(new SelectOption(sn, sn));
        }

        ParamUtil.setRequestAttr(request, "subtypeSelect", subtypeSelect);

        for (String s : svcNames){
            svcNameSelect.add(new SelectOption(s,s));
        }

        ParamUtil.setRequestAttr(request, "svcNameSelect", svcNameSelect);

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

        String[] moduleCheckBox = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE_CHECKBOX);
        String[] typeCheckBox = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE_CHECKBOX);

        String svcName = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE);
        String svcSubType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE);

        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE, common);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE, svcName);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE, svcSubType);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE_CHECKBOX, typeCheckBox);

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

        if (moduleCheckBox!= null && moduleCheckBox.length > 0){
            for (String value : moduleCheckBox){
                searchParam.addFilter(HcsaChecklistConstants.PARAM_CONFIG_MODULE, value, true);
            }
        }

        if (typeCheckBox!= null && typeCheckBox.length > 0){
            for (String value : typeCheckBox){
                searchParam.addFilter(HcsaChecklistConstants.PARAM_CONFIG_TYPE, value, true);
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
        String value = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        setToSession(request, value);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.ACTION_OPERATIONTYPE, HcsaChecklistConstants.ACTION_CLONE);
    }

    private void setToSession(HttpServletRequest request, String value){
        if (!StringUtils.isEmpty(value)){
            ChecklistConfigDto configDto = hcsaChklService.getChecklistConfigById(value);

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
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if ("backLastPage".equals(currentAction)){
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

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_COMMON, common);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE, module);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE, type);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_HCI_CODE, hciCode);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE, svcName);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE, svcSubType);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_START_DATE, eftStartDate);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CONFIG_EFFECTIVE_END_DATE, eftEndDate);

        try {
            Date starteDate = Formatter.parseDate(eftStartDate);
            Date endDate = Formatter.parseDate(eftEndDate);

            ChecklistConfigDto configDto;
            String operationType = (String) ParamUtil.getSessionAttr(request, "operationType");
            if (!StringUtils.isEmpty(operationType) && HcsaChecklistConstants.ACTION_CLONE.equals(operationType)){
                configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
                configDto.setSvcName(svcName);
                configDto.setHciCode(hciCode);

                if (module != null){
                    configDto.setModule(MasterCodeUtil.getCodeDesc(module));
                }

                if (type != null){
                    configDto.setType(MasterCodeUtil.getCodeDesc(type));
                }


            }else {
                configDto = new ChecklistConfigDto();
                if (common != null){
                    configDto.setCommon(true);
                }

                if (type != null){
                    configDto.setType(MasterCodeUtil.getCodeDesc(type));
                }

                if (module != null){
                    configDto.setModule(MasterCodeUtil.getCodeDesc(module));
                }

                configDto.setHciCode(hciCode);
                configDto.setSvcName(svcName);
                configDto.setSvcSubType(svcSubType);
            }


            //field validate
            ValidationResult validationResult = WebValidationHelper.validateProperty(configDto, "create");
            if(validationResult != null && validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            }else {
                configDto.setEftStartDate(starteDate);
                configDto.setEftEndDate(endDate);
                ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, configDto);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            }

            //record validate
            Boolean existsRecord = hcsaChklService.isExistsRecord(configDto);
            if (existsRecord){
                Map<String,String> errorMap = new HashMap<>(1);
                errorMap.put("configCustomValidation", "Do not create the same configuration,Unless you disable it");
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            }

        } catch (ParseException e) {
            throw new IaisRuntimeException(e.getMessage());
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
        String value = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);

        if (!StringUtils.isEmpty(value)){
            hcsaChklService.deleteRecord(value);
        }
    }

    /**
     * AutoStep: doView
     * @param bpc
     * @throws IllegalAccessException
     */
    public void doView(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String configId = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(!StringUtil.isEmpty(configId)){
            ChecklistConfigDto checklistConfigById = hcsaChklService.getChecklistConfigById(configId);
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
        String value = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);

        ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
        if (configDto != null){
            List<ChecklistSectionDto> sectionDtos = configDto.getSectionDtos();
            Iterator<ChecklistSectionDto> iter = sectionDtos.iterator();
            while (iter.hasNext()){
                String sectionId = iter.next().getId();
                if (value.equals(sectionId)){
                    iter.remove();
                }
            }
        }
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, configDto);
    }

    /**
     * @AutoStep: removeSection
     * @param:
     * @return:
     * @author: yichen
     */
    public void removeSectionItem(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentSectionId = ParamUtil.getString(request, "currentValidateId");
        String value = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);

        ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
        if (configDto != null){
            List<ChecklistSectionDto> sectionDtos = configDto.getSectionDtos();
            for (ChecklistSectionDto sec : sectionDtos){
                if (currentSectionId.equals(sec.getId())){
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
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, configDto);
    }


    /**
     * @AutoStep: submitEditData
     * @param:
     * @return:
     * @author: yichen
     */
    public void submitEditData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
        if (configDto == null){
            return;
        }

        String type = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE);
        String module = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE);
        configDto.setModule(module);
        configDto.setType(type);

        ValidationResult validationResult = WebValidationHelper.validateProperty(configDto, "update");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
        }else {
            configDto.setType(MasterCodeUtil.getCodeDesc(type));
            configDto.setModule(MasterCodeUtil.getCodeDesc(module));
            generateOrderIndex(request, configDto.getSectionDtos());

            ParamUtil.setSessionAttr(request, "h2TitleAttr", "Checklist Config Acknowledgement Page");
            ChecklistConfigDto checklistConfigDto = hcsaChklService.submitConfig(configDto);
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, checklistConfigDto);

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
        String value = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);

        setToSession(request, value);
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
        List<SelectOption> subtypeSelects = new ArrayList<>();

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
            String[] checkBoxItemId = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX);
            if(checkBoxItemId == null || checkBoxItemId.length <= 0){
                return;
            }

            List<ChecklistItemDto> necessary = hcsaChklService.listChklItemByItemId(Arrays.asList(checkBoxItemId));
            ChecklistConfigDto disposition = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
            String currentValidateId = (String) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.PARAM_PAGE_INDEX);

            if(disposition != null && currentValidateId != null){
                List<ChecklistSectionDto> currentSection = disposition.getSectionDtos();
                for(ChecklistSectionDto section : currentSection){
                    if(section.getId().equals(currentValidateId)){
                        List<ChecklistItemDto> collocate = section.getChecklistItemDtos();
                        if(collocate != null && !collocate.isEmpty()){
                            for (ChecklistItemDto addCrumb : necessary){
                               collocate.add(addCrumb);
                            }
                        }else {
                            section.setChecklistItemDtos(necessary);
                        }
                    }
                }

            }

            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, disposition);

        }catch (NullPointerException e){
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }
    }

    private void generateOrderIndex(HttpServletRequest request, List<ChecklistSectionDto> sectionDtos){
        for (ChecklistSectionDto sec : sectionDtos){
            String section = sec.getSection();
            String orderStr = ParamUtil.getString(request, section);
            try {
                int order = Integer.valueOf(orderStr);
                sec.setOrder(order);
            }catch (NumberFormatException e){
                throw  new IaisRuntimeException(e.getMessage());
            }

            List<ChecklistItemDto> checklistItemDtos = sec.getChecklistItemDtos();
            if (checklistItemDtos != null && !checklistItemDtos.isEmpty()){
                for (ChecklistItemDto item : checklistItemDtos){
                    String itemOrder = ParamUtil.getString(request, item.getItemId());
                    item.setSectionItemOrder(Integer.valueOf(itemOrder));
                }
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

        ChecklistConfigDto preViewConfig = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
        if (preViewConfig.getSectionDtos() == null || preViewConfig.getSectionDtos().isEmpty()){
            Map<String,String> errorMap = new HashMap<>(1);
            errorMap.put("configErrorMsg", "Please add the required information.");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        }else {
            ParamUtil.setSessionAttr(request, "actionBtn", "submitView");
            generateOrderIndex(request, preViewConfig.getSectionDtos());
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, preViewConfig);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }

    }


    /**
     * @AutoStep: routeToItemProcess
     * @param:
     * @return:
     * @author: yichen
     */
    public void routeToItemProcess(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        // go checklist checklist item page
        String currentValidateId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.PARAM_PAGE_INDEX);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_PAGE_INDEX, currentValidateId);
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
        CrudHelper.doSorting(searchParam,bpc.request);
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
            ChecklistConfigDto configDto = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
            if(configDto != null){

                String operationType = (String) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.ACTION_OPERATIONTYPE);
                if (HcsaChecklistConstants.ACTION_CLONE.equals(operationType)){
                    configDto.setId(null);
                }

                ParamUtil.setSessionAttr(request, "h2TitleAttr", "Checklist Config Acknowledgement Page");
                ChecklistConfigDto checklistConfigDto = hcsaChklService.submitConfig(configDto);
                ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, checklistConfigDto);
            }

            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_PAGE_INDEX, null);
        }catch (NullPointerException e){
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }
    }



}
