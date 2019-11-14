package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.mastercode.MasterCodeConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Hua_Chong
 * @Date 2019/8/5 15:36
 */
@Delegator(value = "masterCodeDelegator")
@Slf4j
public class MasterCodeDelegator {

    private final FilterParameter filterParameter;
    private final MasterCodeService masterCodeService;

    @Autowired
    private MasterCodeDelegator(FilterParameter filterParameter, MasterCodeService masterCodeService){
        this.filterParameter = filterParameter;
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
        preSelectOption(request);

        filterParameter.setClz(MasterCodeQueryDto.class);
        filterParameter.setSearchAttr(MasterCodeConstants.SEARCH_PARAM);
        filterParameter.setResultAttr(MasterCodeConstants.SEARCH_RESULT);
        filterParameter.setSortField(MasterCodeConstants.MASTERCODE_ID);
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("systemAdmin", "masterCodeQuery",searchParam);

        SearchResult searchResult = masterCodeService.doQuery(searchParam);
        ParamUtil.setSessionAttr(request,MasterCodeConstants.SEARCH_PARAM, searchParam);
        ParamUtil.setRequestAttr(request,MasterCodeConstants.SEARCH_RESULT, searchResult);
    }

    /**
     * AutoStep: PrepareSwitch
     *
     * @throws
     */
    public void prepareSwitch(BaseProcessClass bpc){
        logAboutStart("prepareSwitch");
        ParamUtil.getString(bpc.request, MasterCodeConstants.CRUD_ACTION_TYPE);
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
        String masterCodeId = ParamUtil.getString(request,MasterCodeConstants.CRUD_ACTION_VALUE);
        ParamUtil.setRequestAttr(request, "masterCodeId", masterCodeId);
        List statusSelect = new ArrayList<SelectOption>();
        SelectOption sp1 = new SelectOption("pending",MasterCodeConstants.PENDING);
        statusSelect.add(sp1);
        SelectOption sp2 = new SelectOption("procing",MasterCodeConstants.PROCING);
        statusSelect.add(sp2);
        ParamUtil.setRequestAttr(request,"statusSelect",statusSelect);
        ParamUtil.setRequestAttr(request, MasterCodeConstants.MASTERCODE_USER_ACCOUNT_TILE,"Master Code Create");
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
        preSelectOption(request);
        String rowguid = ParamUtil.getString(request,MasterCodeConstants.CRUD_ACTION_VALUE);
        MasterCodeDto masterCodeDto = masterCodeService.findMasterCodeByRowguid(rowguid);
        ParamUtil.setSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR, masterCodeDto);
        ParamUtil.setRequestAttr(request, MasterCodeConstants.MASTERCODE_USER_ACCOUNT_TILE,"MasterCode Edit");
        List statusSelect = new ArrayList<SelectOption>();
        SelectOption sp1 = new SelectOption("pending",MasterCodeConstants.PENDING);
        statusSelect.add(sp1);
        SelectOption sp2 = new SelectOption("procing",MasterCodeConstants.PROCING);
        statusSelect.add(sp2);
        ParamUtil.setRequestAttr(request, "statusSelect",statusSelect);
    }

    /**
     * AutoStep: doSearch
     *
     * @param bpc
     * @throws
     */
    public void doSearch(BaseProcessClass bpc){
        logAboutStart("doSearch");
        HttpServletRequest request = bpc.request;
        SearchParam param = IaisEGPHelper.getSearchParam(request, true,filterParameter);
        String masterCodeKey = ParamUtil.getString(request, MasterCodeConstants.MASTERCODE_KEY);
        String codeValue = ParamUtil.getString(request,MasterCodeConstants.CRUD_VALUE);
        String[] status = ParamUtil.getStrings(request,MasterCodeConstants.STATUS);
        if(!StringUtil.isEmpty(masterCodeKey)){
            param.addFilter(MasterCodeConstants.MASTERCODE_KEY,masterCodeKey,true);
        }
        if(!StringUtil.isEmpty(codeValue)){
            param.addFilter(MasterCodeConstants.CRUD_VALUE,codeValue,true);
        }
        if(status != null && status.length>0){
            String statusStr = SqlHelper.constructInCondition("mc.STATUS",status.length);
            param.addParam(MasterCodeConstants.STATUS,statusStr);
            for (int i = 0 ; i<status.length; i++ ) {
                param.addFilter("mc.STATUS"+i,status[i]);
            }
        }
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
        CrudHelper.doSorting(searchParam,  bpc.request);
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
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
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
        String id = ParamUtil.getString(bpc.request,MasterCodeConstants.CRUD_ACTION_VALUE);
        if(!StringUtil.isEmpty(id)){
            masterCodeService.deleteMasterCodeById(Long.valueOf(id));
        }
    }

    /**
     * AutoStep: doCreate
     *
     * @param bpc
     * @throws
     */
    public void doCreate(BaseProcessClass bpc){
        logAboutStart("doCreate");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, MasterCodeConstants.CRUD_ACTION_TYPE);
        if("save".equals(type)){
            int masterCodeId = ParamUtil.getInt(request,MasterCodeConstants.CRUD_ACTION_VALUE);
            MasterCodeDto masterCodeDto = new MasterCodeDto();
            getValueFromPage(masterCodeDto, request);
            masterCodeDto.setMasterCodeId(masterCodeId);
            ParamUtil.setSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR, masterCodeDto);
            ValidationResult validationResult = ValidationUtils.validateProperty(masterCodeDto,"create");
            if (validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(request,MasterCodeConstants.ERRORMAP,errorMap);
                ParamUtil.setRequestAttr(request,MasterCodeConstants.ISVALID,MasterCodeConstants.NO);
            }else{
                masterCodeService.saveMasterCode(masterCodeDto);
                ParamUtil.setRequestAttr(request,MasterCodeConstants.ISVALID,MasterCodeConstants.YES);
            }
        }else{
            ParamUtil.setRequestAttr(request,MasterCodeConstants.ISVALID,MasterCodeConstants.YES);
        }
    }

    /**
     * AutoStep: doEdit
     *
     * @param bpc
     * @throws
     */
    public void doEdit(BaseProcessClass bpc){
        logAboutStart("doEdit");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,MasterCodeConstants.CRUD_ACTION_TYPE);
        if("edit".equals(type)){
            MasterCodeDto masterCodeDto = (MasterCodeDto) ParamUtil.getSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR);
            getValueFromPage(masterCodeDto, request);
            ValidationResult validationResult =WebValidationHelper.validateProperty(masterCodeDto, "edit");
            if (validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(request,MasterCodeConstants.ERRORMAP,errorMap);
                ParamUtil.setRequestAttr(request,MasterCodeConstants.ISVALID,MasterCodeConstants.NO);
            }else{
                Map<String,String> successMap = new HashMap<>();
                successMap.put("test","success");
                masterCodeService.saveMasterCode(masterCodeDto);
                ParamUtil.setRequestAttr(request,MasterCodeConstants.ISVALID,MasterCodeConstants.YES);
                ParamUtil.setRequestAttr(request,MasterCodeConstants.SUCCESSMAP,successMap);
            }
        }else{
            ParamUtil.setRequestAttr(request,MasterCodeConstants.ISVALID,MasterCodeConstants.YES);
        }
    }

    private void getValueFromPage(MasterCodeDto masterCodeDto, HttpServletRequest request){
        String masterCodeKey = ParamUtil.getString(request,MasterCodeConstants.MASTERCODE_KEY);
        String rowguid = ParamUtil.getString(request,"rowguid");
        String codeCategory = ParamUtil.getString(request,"code_category");
        String codeValue = ParamUtil.getString(request,MasterCodeConstants.CRUD_VALUE);
        int status = ParamUtil.getInt(request,MasterCodeConstants.STATUS);
        masterCodeDto.setMasterCodeKey(masterCodeKey);
        masterCodeDto.setRowguid(rowguid);
        masterCodeDto.setCodeCategory(codeCategory);
        masterCodeDto.setCodeValue(codeValue);
        masterCodeDto.setStatus(status);
    }

    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> statusOptionList = new ArrayList<>();
        statusOptionList.add(new SelectOption("Procing", MasterCodeConstants.PROCING));
        statusOptionList.add(new SelectOption("Pending", MasterCodeConstants.PENDING));

        List<SelectOption> codeDescriptionOptionList = new ArrayList<>();
        codeDescriptionOptionList.add(new SelectOption("import", "import"));
        codeDescriptionOptionList.add(new SelectOption("ordinary", "ordinary"));

        ParamUtil.setRequestAttr(request, "StatusOption", statusOptionList);
        ParamUtil.setRequestAttr(request, "CodeDesOption", codeDescriptionOptionList);
    }

    private void logAboutStart(String methodName){
        log.debug("**** The"+methodName+"Start ****");

    }
}
