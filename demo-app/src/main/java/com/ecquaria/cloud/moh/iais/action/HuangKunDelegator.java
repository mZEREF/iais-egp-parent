package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.Dto.HuangKunRoomDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
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

    private  final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(HuangKunRoomDto.class)
            .searchAttr("roomSearchParam")
            .resultAttr("roomSearchResult")
            .sortFieldToMap("id", SearchParam.ASCENDING)
            .sortFieldToMap("roomType", SearchParam.ASCENDING)
            .sortFieldToMap("roomNo", SearchParam.ASCENDING).build();

    @Autowired
    HuangKunRoomService huangKunRoomService;

    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,"roomSearchParam", null);
        preSelectOption(request);
        SearchResult<HuangKunRoomDto> rooms = huangKunRoomService.doQuery();
        ParamUtil.setRequestAttr(request, "roomSearchResult", rooms);
    }

    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam param = IaisEGPHelper.getSearchParam(request, filterParameter);
        ParamUtil.setSessionAttr(request, "roomSearchParam", param);
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
    }

    public void doSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchResult<HuangKunRoomDto> rooms=new SearchResult<>();
        String roomType = request.getParameter("roomType");
        if (roomType==null||"".equals(roomType)){
            rooms = huangKunRoomService.doQuery();
        }else {
            rooms = huangKunRoomService.queryRoomByType(roomType);
        }
        ParamUtil.setRequestAttr(request, "roomSearchResult", rooms);
    }

    public void doback(BaseProcessClass bpc){
    }

    public void doEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        String roomType = ParamUtil.getString(request, "roomType");
        String roomNo = ParamUtil.getString(request, "roomNo");
        HuangKunRoomDto roomEditDto = (HuangKunRoomDto) ParamUtil.getSessionAttr(request, "roomRequestDto");
    }
}
