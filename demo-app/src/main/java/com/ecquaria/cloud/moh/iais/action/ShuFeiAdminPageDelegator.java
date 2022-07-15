package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.*;
import com.ecquaria.cloud.moh.iais.common.constant.message.*;
import com.ecquaria.cloud.moh.iais.common.constant.sample.*;
import com.ecquaria.cloud.moh.iais.common.dto.*;
import com.ecquaria.cloud.moh.iais.common.dto.sample.*;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.common.validation.dto.*;
import com.ecquaria.cloud.moh.iais.constant.*;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.*;
import lombok.extern.slf4j.*;
import org.apache.commons.collections.*;
import org.springframework.beans.factory.annotation.*;
import sop.webflow.rt.api.*;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@Delegator(value = "shuFeiAdminPageDelegator")
@Slf4j
public class ShuFeiAdminPageDelegator {

private  final FilterParameter filterParameter = new FilterParameter.Builder()
        .clz(ShuFeiSampleQueryDto.class)
        .searchAttr(MessageConstants.PARAM_MESSAGE_SEARCH)
        .resultAttr(MessageConstants.PARAM_MESSAGE_SEARCH_RESULT)
        .sortFieldToMap("office_Tel_No", SearchParam.ASCENDING).build();

    private  final ShuFeiAdminPageService shuFeiAdminPageService;

    @Autowired
    public ShuFeiAdminPageDelegator(ShuFeiAdminPageService shuFeiAdminPageService){
        this.shuFeiAdminPageService = shuFeiAdminPageService;
    }


    /**
     * setup option to web page
     * @param request
     */
    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> m =  IaisCommonUtils.genNewArrayList();
        List<ShuFeiPersonSampleDto> option =shuFeiAdminPageService.getAllPerson();
        for (ShuFeiPersonSampleDto i : option){
            m.add(new SelectOption(i.getId(), i.getId()));
        }
        ParamUtil.setSessionAttr(request, "m", (Serializable) m);
    }

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) throws IllegalAccessException{
        AuditTrailHelper.auditFunction("iais-demo", "Room Person Message Management");
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, DemoConstants.class);
        ParamUtil.setSessionAttr(request, MessageConstants.PARAM_MESSAGE_SEARCH, null);
        ParamUtil.setSessionAttr(request, MessageConstants.PARAM_MESSAGE_SEARCH_RESULT, null);
    }
    /**
     * AutoStep: PrepareData
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        preSelectOption(request);
        SearchParam param = IaisEGPHelper.getSearchParam(request,filterParameter);
        QueryHelp.setMainSql("ShuFeiSampleDemo", "searchShuFeiSampleDemo",param);
        SearchResult searchResult = shuFeiAdminPageService.doQuery(param);
        ParamUtil.setSessionAttr(request, MessageConstants.PARAM_MESSAGE_SEARCH, param);
        ParamUtil.setRequestAttr(request, MessageConstants.PARAM_MESSAGE_SEARCH_RESULT, searchResult);
        List<SelectOption> moduleList =  IaisCommonUtils.genNewArrayList();
        List<String> option = shuFeiAdminPageService.getRoomType();
        Set<String> s = new HashSet<>();
        for (String i : option){
            s.add(i);
        }
        for (String j : s) {
            moduleList.add(new SelectOption(j,j));
        }
        ParamUtil.setSessionAttr(request, "roomType", (Serializable) moduleList);
        List<SelectOption> moduleList1 =  IaisCommonUtils.genNewArrayList();
        List<String> option1 = shuFeiAdminPageService.getRoomNo();
        Set<String> f = new HashSet<>();
        for (String i : option1){
            f.add(i);
        }
        for (String j : f) {
            moduleList1.add(new SelectOption(j, j));
        }
        ParamUtil.setSessionAttr(request, "roomNo", (Serializable) moduleList1);
    }

    /**
     * AutoStep: PrepareCreate
     *
     * @param bpc
     * @throws
     */
    public void prepareCreate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String t = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setRequestAttr(request, "type", t);
        String n = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setRequestAttr(request,"no",n);
        List<SelectOption> moduleList =  IaisCommonUtils.genNewArrayList();
        List<String> option = shuFeiAdminPageService.getRoomType();
        Set<String> s = new HashSet<>();
        for (String i : option){
            s.add(i);
        }
        for (String j : s) {
            moduleList.add(new SelectOption(j,j));
        }
        ParamUtil.setSessionAttr(request, "roomType", (Serializable) moduleList);
        List<SelectOption> moduleList1 =  IaisCommonUtils.genNewArrayList();
        List<String> option1 = shuFeiAdminPageService.getRoomNo();
        Set<String> f = new HashSet<>();
        for (String i : option1){
            f.add(i);
        }
        for (String j : f) {
            moduleList1.add(new SelectOption(j, j));
        }
        ParamUtil.setSessionAttr(request, "roomNo", (Serializable) moduleList1);
        Map<String,String> errorMap = (Map<String, String>) ParamUtil.getRequestAttr(request,IaisEGPConstant.ERRORMSG);
        if(MapUtils.isEmpty(errorMap)){
            ParamUtil.setSessionAttr(request, "ShuFeiCreateSampleDto", null);
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
        String roomType = ParamUtil.getString(request, MessageConstants.PARAM_DOMAIN_TYPE);
        String roomNo = ParamUtil.getString(request, MessageConstants.PARAM_MSG_TYPE);

        if (!StringUtil.isEmpty(roomType) && !StringUtil.isEmpty(roomNo)){
            String roomId = shuFeiAdminPageService.selectRoomId(roomType,roomNo);
            SearchParam param = IaisEGPHelper.getSearchParam(request, true, filterParameter);
            param.addFilter("roomId",roomId,true);
        }
        System.out.println("=================================================================================================");
    }

    /**
     * AutoStep: doPage
     *
     * @param bpc
     * @throws
     */
    public void changePage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * AutoStep: sortRecords
     * @param bpc
     */
    public void sortRecords(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,  bpc.request);
    }

    /**
     * AutoStep: doCreate
     *
     * @param bpc
     * @throws
     */
    public void doCreate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if("save".equals(curAct)){
            String type = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
            String no = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
            ShuFeiCreateSampleDto shuFeiCreateSampleDto = new ShuFeiCreateSampleDto();
            getValueFromPage(shuFeiCreateSampleDto, request);
            ParamUtil.setSessionAttr(request, "ShuFeiCreateSampleDto", shuFeiCreateSampleDto);
            ValidationResult validationResult =WebValidationHelper.validateProperty(shuFeiCreateSampleDto,"create");
            if (validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG,errorMap);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
            }else{
                shuFeiAdminPageService.saveCreatePerson(shuFeiCreateSampleDto);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            }
        }else{
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
        }
    }

    /**
     * AutoStep: PrepareEdit
     *
     * @param bpc
     * @throws
     */
    public void perpareEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String personId = ParamUtil.getMaskedString(request,DemoConstants.CRUD_ACTION_VALUE);
        System.out.println("....................personId..................."+personId);
        ShuFeiCreateSampleDto shuFeiCreateSampleDto;
        if(StringUtil.isEmpty(personId)) {
            shuFeiCreateSampleDto = (ShuFeiCreateSampleDto) ParamUtil.getSessionAttr(request, "ShuFeiCreateSampleDto");
        }else{
            shuFeiCreateSampleDto = shuFeiAdminPageService.getByPersonId(personId);
            shuFeiCreateSampleDto.setId(personId);
            shuFeiCreateSampleDto.setEditFlag(true);
            System.out.println("===============shuFeiCreateSampleDto===============>>>"+shuFeiCreateSampleDto);
        }
        ParamUtil.setSessionAttr(request, "ShuFeiCreateSampleDto", shuFeiCreateSampleDto);
    }
    /**
     * AutoStep: doEdit
     * user do edit with message management
     * @param bpc
     */
    public void doEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if("edit".equals(curAct)){
            ShuFeiCreateSampleDto shuFeiCreateSampleDto = (ShuFeiCreateSampleDto) ParamUtil.getSessionAttr(request,"ShuFeiCreateSampleDto");
            getValueFromPage(shuFeiCreateSampleDto, request);
            ValidationResult validationResult =WebValidationHelper.validateProperty(shuFeiCreateSampleDto, "edit");
            if (validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,errorMap);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
            }else{
                Map<String,String> successMap = IaisCommonUtils.genNewHashMap();
                successMap.put("test","suceess");
                shuFeiAdminPageService.saveCreatePerson(shuFeiCreateSampleDto);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
                ParamUtil.setRequestAttr(request,"successMap",successMap);
            }
        }else{
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
        }
    }

    private void getValueFromPage(ShuFeiCreateSampleDto shuFeiCreateSampleDto, HttpServletRequest request) {
        String roomNo = ParamUtil.getString(request,"roomNo");
        String roomType = ParamUtil.getString(request,"roomType");
        String displayName = ParamUtil.getString(request,"displayName");
        String mobileNo = ParamUtil.getString(request,"mobileNo");
        String officeTelNo = ParamUtil.getString(request,"officeTelNo");
        String emailAddr = ParamUtil.getString(request,"emailAddr");

        shuFeiCreateSampleDto.setRoomNo(roomNo);
        shuFeiCreateSampleDto.setRoomType(roomType);
        shuFeiCreateSampleDto.setDisplayName(displayName);
        shuFeiCreateSampleDto.setMobileNo(mobileNo);
        shuFeiCreateSampleDto.setOfficeTelNo(officeTelNo);
        shuFeiCreateSampleDto.setEmailAddr(emailAddr);
        shuFeiCreateSampleDto.setEffectiveFrom(new Date());
        shuFeiCreateSampleDto.setEffectiveTo(new Date());
        String roomId = shuFeiAdminPageService.selectRoomId(roomType,roomNo);
        if (roomId == null){
            ShuFeiRoomSampleDto shuFeiRoomSampleDto = new ShuFeiRoomSampleDto();
            shuFeiRoomSampleDto.setRoomType(roomType);
            shuFeiRoomSampleDto.setRoomNo(roomNo);
            shuFeiAdminPageService.saveRoom(shuFeiRoomSampleDto);
            roomId = shuFeiAdminPageService.selectRoomId(roomType,roomNo);
            shuFeiCreateSampleDto.setRoomId(roomId);
        }else {
            shuFeiCreateSampleDto.setRoomId(roomId);
        }

    }



}
