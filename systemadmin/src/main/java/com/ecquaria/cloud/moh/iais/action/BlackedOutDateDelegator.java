package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageQueryDto;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: yichen
 * @date time:12/28/2019 2:25 PM
 * @description:
 */

@Delegator(value = "blackedOutDateDelegator")
public class BlackedOutDateDelegator {
    private  final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(ApptBlackoutDateQueryDto.class)
            .searchAttr(AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_QUERY)
            .resultAttr(AppointmentConstants.APPOINTMENT_BLACKED_OUT_DATE_RESULT)
            .sortField("id").sortType(SearchParam.ASCENDING).build();

    /**
     * StartStep: preLoad
     * @param bpc
     * @throws IllegalAccessException
     */
    public void preLoad(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;

        SearchParam param = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("systemAdmin", "getBlackedOutDateList", param);

    }
}
