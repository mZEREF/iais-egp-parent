package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.HuangKunPersonDto;
import com.ecquaria.cloud.moh.iais.dto.HuangKunRoomDto;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.HuangKunRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: HuangKunDelegator
 * @author: haungkun
 * @date: 2022/7/7 16:42
 */
@Delegator("huangKunDelegator")
@Slf4j
public class HuangKunDelegator {

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(HuangKunRoomDto.class)
            .searchAttr("roomSearchParam")
            .resultAttr("roomSearchResult")
            .sortFieldToMap("ROOM_NO", SearchParam.ASCENDING).build();

    @Autowired
    HuangKunRoomService huangKunRoomService;

    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        preSelectOption(request);
        ParamUtil.setSessionAttr(request,"roomSearchParam", null);
        ParamUtil.setSessionAttr(request, "roomSearchResult", null);
    }

    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        SearchParam param = IaisEGPHelper.getSearchParam(request, filterParameter);

        QueryHelp.setMainSql("roomsample", "queryRoom", param);
        SearchResult<HuangKunRoomDto> result = huangKunRoomService.doQuery(param);

        ParamUtil.setSessionAttr(request, "roomSearchParam", param);
        ParamUtil.setSessionAttr(request, "roomSearchResult", result);
    }

    public void doSorting(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,  bpc.request);
    }

    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> roomTypeList =  IaisCommonUtils.genNewArrayList();
        List<String> option = huangKunRoomService.listRoomTypes();
        for (String i : option){
            roomTypeList.add(new SelectOption(i, i));
        }
        ParamUtil.setSessionAttr(request, "roomTypeSelect", (Serializable) roomTypeList);
    }

    public void prepareEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String roomId = ParamUtil.getMaskedString(request, "roomId");
        HuangKunRoomDto roomRequestDto = huangKunRoomService.getRoomById(roomId);
        ParamUtil.setSessionAttr(request,"roomRequestDto", roomRequestDto);
        SearchResult<HuangKunPersonDto> result = huangKunRoomService.queryPersonByRoomId(roomId);
        ParamUtil.setRequestAttr(request, "personResult", result);
    }

    public void doSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String roomType = request.getParameter("roomType");
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        if (StringUtil.isNotEmpty(roomType)){
            searchParam.addFilter("roomType", roomType, true);
        }
    }

    public void doBack(BaseProcessClass bpc){
    }

    public void doEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String roomType = ParamUtil.getString(request, "roomType");
        String roomNo = ParamUtil.getString(request, "roomNO");
        HuangKunRoomDto roomEditDto = (HuangKunRoomDto) ParamUtil.getSessionAttr(request, "roomRequestDto");
        roomEditDto.setRoomNO(roomNo);
        roomEditDto.setRoomType(roomType);
        huangKunRoomService.updateRoom(roomEditDto);
    }

    public void doPaging(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    public void prepareAddRoom(BaseProcessClass bpc){
    }

    public void addRoom(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String roomType = ParamUtil.getString(request, "roomType");
        String roomNo = ParamUtil.getString(request, "roomNO");
        HuangKunRoomDto huangKunRoomDto=new HuangKunRoomDto();
        huangKunRoomDto.setRoomNO(roomNo);
        huangKunRoomDto.setRoomType(roomType);
        huangKunRoomService.addRoom(huangKunRoomDto);
    }

    public void prepareAddPerson(BaseProcessClass bpc){
    }

    public void addPerson(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String roomId = ParamUtil.getMaskedString(request, "roomId");
        String displayName = ParamUtil.getString(request, "displayName");
        String mobileNo = ParamUtil.getString(request, "mobileNo");
        HuangKunPersonDto huangKunPersonDto=new HuangKunPersonDto();
        huangKunPersonDto.setDisplayName(displayName);
        huangKunPersonDto.setRoomId(roomId);
        huangKunPersonDto.setMobileNo(mobileNo);
        huangKunRoomService.savePerson(huangKunPersonDto);
        SearchResult<HuangKunPersonDto> result = huangKunRoomService.queryPersonByRoomId(roomId);
        ParamUtil.setRequestAttr(request, "personResult", result);
    }
}
