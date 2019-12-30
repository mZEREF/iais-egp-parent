package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: yichen
 * @date time:12/28/2019 2:25 PM
 * @description:
 */

@Delegator(value = "blackedOutDateDelegator")
public class BlackedOutDateDelegator {
    @Autowired
    private AppointmentService appointmentService;

    private  final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(ApptBlackoutDateQueryDto.class)
            .searchAttr(AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_QUERY)
            .resultAttr(AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_RESULT)
            .sortField("id").sortType(SearchParam.ASCENDING).build();

    /**
     * StartStep: startStep
     * @param bpc
     * @throws IllegalAccessException
     */
    public void startStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_QUERY, null);
        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_RESULT, null);
        //ParamUtil.setSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR, null);

    }

    /**
     * StartStep: preLoad
     * @param bpc
     * @throws IllegalAccessException
     */
    public void preLoad(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        //userid -->> current user group , find it lead to group


        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("systemAdmin", "getBlackedOutDateList", searchParam);
//        SearchResult<ApptBlackoutDateQueryDto> searchResult = appointmentService.doQuery(searchParam);

//        SearchParam taskSearchParam = new SearchParam(TaskQueryDto.class.getName());
//        QueryHelp.setMainSql("systemAdmin", "getTaskByEachInspectionGroup", taskSearchParam);




//        ParamUtil.setSessionAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_QUERY, searchParam);
//        ParamUtil.setRequestAttr(request, AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_RESULT, searchResult);
    }

    /**
     * StartStep: preCreate
     * @param bpc
     * @throws IllegalAccessException
     */
    public void preCreate(BaseProcessClass bpc)  {
        HttpServletRequest request = bpc.request;


    }
}
