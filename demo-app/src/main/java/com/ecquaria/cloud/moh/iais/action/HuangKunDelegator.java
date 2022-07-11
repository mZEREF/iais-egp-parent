package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.Dto.HuangKunRoomDto;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.HuangKunRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: HuangKunDelegator
 * @author: haungkun
 * @date: 2022/7/7 16:42
 */
@Delegator("huangKunDelegator")
@Slf4j
public class HuangKunDelegator {

    private  final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(HuangKunRoomDto.class)
            .searchAttr("roomSearchParam")
            .resultAttr("roomSearchResult")
            .sortFieldToMap("room_Type", SearchParam.ASCENDING).sortFieldToMap("module", SearchParam.ASCENDING).build();

    @Autowired
    HuangKunRoomService huangKunRoomService;

    /**
     * StartStep: startStep
     * @param bpc
     * @throws IllegalAccessException
     */
    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, MessageConstants.class);
        ParamUtil.setSessionAttr(request,"roomSearchParam", null);
        ParamUtil.setRequestAttr(request, "roomSearchResult", null);
    }

    /**
     *  AutoStep: prepareData
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        SearchParam param = IaisEGPHelper.getSearchParam(request, filterParameter);
        SearchResult<HuangKunRoomDto> rooms = huangKunRoomService.doQuery();

        ParamUtil.setRequestAttr(request, "rooms", rooms);
        ParamUtil.setSessionAttr(request, "roomSearchParam", param);
    }

    /**
     * AutoStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.debug("The prepareSwitch start ...");
        log.debug("The prepareSwitch end ...");
    }

    /**
     * AutoStep: sortRecords
     * @param bpc
     */
    public void doSorting(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,  bpc.request);
    }

    /**
     * setup option to web page
     * @param request
     */
    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> moduleList =  IaisCommonUtils.genNewArrayList();
        List<String> option = huangKunRoomService.listModuleTypes();
        for (String i : option){
            moduleList.add(new SelectOption(i, i));
        }
        ParamUtil.setSessionAttr(request, "moduleTypeSelect", (Serializable) moduleList);
    }

    /**
     * AutoStep: editBefore
     * @param bpc
     */
    public void prepareEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String msgId = ParamUtil.getMaskedString(request, "msgQueryId");
        preSelectOption(request);
        if(StringUtil.isNotEmpty(msgId)){
            HuangKunRoomDto huangKunRoomDto= huangKunRoomService.getRoomById(msgId);
            ParamUtil.setSessionAttr(request, MessageConstants.MESSAGE_REQUEST_DTO, huangKunRoomDto);
        }
    }


    /**
     * AutoStep: doSearch
     * @param bpc
     */
    public void doSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!MessageConstants.ACTION_SEARCH.equals(curAct)){
            return;
        }
        String domainType = ParamUtil.getString(request, MessageConstants.PARAM_DOMAIN_TYPE);
        String msgType = ParamUtil.getString(request, MessageConstants.PARAM_MSG_TYPE);
        String module = ParamUtil.getString(request, MessageConstants.PARAM_MODULE);

        MessageDto dto = new MessageDto();
        dto.setDomainType(domainType);
        dto.setMsgType(msgType);
        dto.setModule(module);
        ValidationResult vResult = WebValidationHelper.validateProperty(dto, "search");
        if(vResult != null && vResult.isHasErrors()) {
            Map<String, String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "N");
        }else {
            SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
            if (StringUtil.isNotEmpty(domainType)){
                searchParam.addFilter(MessageConstants.PARAM_DOMAIN_TYPE, domainType, true);
            }

            if(StringUtil.isNotEmpty(msgType)){
                searchParam.addFilter(MessageConstants.PARAM_MSG_TYPE, msgType, true);
            }

            if(StringUtil.isNotEmpty(module)){
                searchParam.addFilter(MessageConstants.PARAM_MODULE, module, true);
            }
        }
    }
}
