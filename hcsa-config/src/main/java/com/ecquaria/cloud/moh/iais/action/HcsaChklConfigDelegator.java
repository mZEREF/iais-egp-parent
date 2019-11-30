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
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Delegator(value = "hcsaChklConfigDelegator")
@Slf4j
public class HcsaChklConfigDelegator {

    private HcsaChklService hcsaChklService;
    private FilterParameter filterParameter;

    private List<String> subtypeNames = null;
    private List<String> svcNames = null;

    @Autowired
    public HcsaChklConfigDelegator(HcsaChklService hcsaChklService, FilterParameter filterParameter){
        this.hcsaChklService = hcsaChklService;
        this.filterParameter = filterParameter;
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
        ParamUtil.setSessionAttr(request, "addedItemIdList", null);
    }

    /**
     * StartStep: prepare
     * @param bpc
     * @throws IllegalAccessException
     */
    public void prepare(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(HcsaChecklistConstants.ACTION_CANCEL.equals(currentAction) || HcsaChecklistConstants.BACK_LAST_PAGE_BUTTON.equals(currentAction)){
            IaisEGPHelper.clearSessionAttr(request, ChecklistConfigQueryDto.class);
        }

        preSelectOption(request);

        filterParameter.setClz(ChecklistConfigQueryDto.class);
        filterParameter.setSearchAttr(HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_SEARCH);
        filterParameter.setResultAttr(HcsaChecklistConstants.PARAM_CHECKLIST_CONFIG_RESULT);
        filterParameter.setSortField("config_id");

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
            return;
        }

        String section = ParamUtil.getString(request, "section");
        String sectionDesc = ParamUtil.getString(request, "sectionDesc");

        if(StringUtils.isEmpty(section) || StringUtils.isEmpty(sectionDesc)){
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
            }

            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, configDto);
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
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
    }


    private Boolean judgeConfigIsCommon(String var){
        return true;
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

        String common = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_COMMON);
        String module = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_MODULE);
        String type = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_TYPE);
        String svcName = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE);
        String svcSubType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CONFIG_SERVICE_SUB_TYPE);

        boolean isCommon = judgeConfigIsCommon(common);

        ChecklistConfigDto configDto = new ChecklistConfigDto();
        configDto.setCommon(isCommon);
        configDto.setType(type);
        configDto.setSvcName(svcName);
        configDto.setModule(type);
        configDto.setSvcSubType(svcSubType);
        ValidationResult validationResult = WebValidationHelper.validateProperty(configDto, "addConfigInfo");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            configDto.setType(MasterCodeUtil.getCodeDesc(type));
            configDto.setModule(MasterCodeUtil.getCodeDesc(module));
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, configDto);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
        }
    }

    /**
     * @AutoStep: saveChecklistConfig
     * @param:
     * @return:
     * @author: yichen
     */
    public void saveChecklistConfig(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
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
            String currentValidateId = (String) ParamUtil.getSessionAttr(request, "currentValidateId");

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

    /**
     * @AutoStep: preViewConfig
     * @param:
     * @return:
     * @author: yichen
     */
    public void preViewConfig(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        ChecklistConfigDto preViewConfig = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
        List<ChecklistSectionDto> sectionDtos = preViewConfig.getSectionDtos();
        for (ChecklistSectionDto sec : sectionDtos){
            String section = sec.getSection();
            String order = ParamUtil.getString(request, section);
            sec.setOrder(Integer.valueOf(order));
            List<ChecklistItemDto> checklistItemDtos = sec.getChecklistItemDtos();
            for (ChecklistItemDto item : checklistItemDtos){
                String itemOrder = ParamUtil.getString(request, item.getItemId());
                item.setSectionItemOrder(Integer.valueOf(itemOrder));
            }
        }

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, preViewConfig);
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
        String currentValidateId = ParamUtil.getMaskedString(request, "currentValidateId");
        ParamUtil.setSessionAttr(request, "currentValidateId", currentValidateId);
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
                hcsaChklService.submitConfig(configDto);
            }

            ParamUtil.setSessionAttr(request, "currentValidateId", null);
        }catch (NullPointerException e){
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }
    }



}
