package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2019-11-30 13:39
 **/
@Slf4j
@Controller
@RequestMapping("/nonWorkingDayAjax")
public class NonWorkingDayAjaxController {
    private static final String NON_WKR_DAY_ID_ATTR = "nonWkrDayId";
    private static final String NON_WKR_DAY_LIST_ATTR = "nonWkrinDayListAttr";
    private static final String AM_AVAILABILITY__ATTR = "amAvailability";
    private static final String PM_AVAILABILITY__ATTR = "pmAvailability";
    private static final String CURRENT_SHORT_NAME = "currentGroupId";

    private static final String AM_START = "9:00:00";
    private static final String AM_END = "13:00:00";
    private static final String PM_START = "14:00:00";
    private static final String PM_END = "18:00:00";

    @Autowired
    private AppointmentService appointmentService;

    @RequestMapping(value = "change.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> appGroup(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();

        String nonWkrDayId = ParamUtil.getMaskedString(request, NON_WKR_DAY_ID_ATTR);

        ApptNonWorkingDateDto nonWorkingDateDto = new ApptNonWorkingDateDto();
        List<ApptNonWorkingDateDto> nonWorkingDateListByWorkGroupId = (List<ApptNonWorkingDateDto>) ParamUtil.getSessionAttr(request, NON_WKR_DAY_LIST_ATTR);
        nonWorkingDateDto = nonWorkingDateListByWorkGroupId.stream()
                .filter(apptNonWorkingDateDto ->  !StringUtils.isEmpty(apptNonWorkingDateDto.getId()))
                .filter(apptNonWorkingDateDto -> apptNonWorkingDateDto.getId().equals(nonWkrDayId)).findFirst().orElse(null);
        nonWorkingDateListByWorkGroupId.remove(nonWorkingDateDto);

        String shortName = (String) ParamUtil.getSessionAttr(request, CURRENT_SHORT_NAME);
        String amAvailability = ParamUtil.getString(request, AM_AVAILABILITY__ATTR);
        String pmAvailability = ParamUtil.getString(request, PM_AVAILABILITY__ATTR);

        nonWorkingDateDto.setShortName(shortName);
        nonWorkingDateDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

        int am = "Y".equals(amAvailability) ?  0x1 : 0x0;
        int pm = "Y".equals(pmAvailability) ?  0x1 : 0x0;

        nonWorkingDateDto.setStartAt(Time.valueOf(PM_START));
        nonWorkingDateDto.setEndAt(Time.valueOf(PM_END));

        if((am & 0x1) == 1){
            nonWorkingDateDto.setAm(true);
        }else {
            nonWorkingDateDto.setAm(false);
        }
        if((pm & 0x1) == 1){
            nonWorkingDateDto.setPm(true);
        }else {
            nonWorkingDateDto.setPm(false);
        }

        ApptNonWorkingDateDto apptNonWorkingDateDto = appointmentService.updateNonWorkingDate(nonWorkingDateDto);
        nonWorkingDateListByWorkGroupId.add(apptNonWorkingDateDto);
        ParamUtil.setSessionAttr(request, NON_WKR_DAY_LIST_ATTR,(Serializable) nonWorkingDateListByWorkGroupId);
        map.put("nonWorkingDateId", MaskUtil.maskValue(NON_WKR_DAY_ID_ATTR , apptNonWorkingDateDto.getId()));
        return map;
    }

}