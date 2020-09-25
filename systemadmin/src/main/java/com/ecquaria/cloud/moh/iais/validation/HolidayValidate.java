package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.PublicHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Map;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2020-03-09 12:41
 **/
@Component
public class HolidayValidate implements CustomizeValidator {

    @Autowired
    private PublicHolidayService publicHolidayService;

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        PublicHolidayDto publicHolidayDto = (PublicHolidayDto) ParamUtil.getSessionAttr(request, "holiday");
        PublicHolidayDto origal = new PublicHolidayDto();
        if(publicHolidayDto.getId() != null){
            origal = publicHolidayService.getHolidayById(publicHolidayDto.getId());
        }
        String from = Formatter.formatDateTime(publicHolidayDto.getFromDate(), "ddMMyyyy");
        if(from == null || from.isEmpty()){
            errMap.put("sub_date", MessageUtil.getMessageDesc("USER_ERR001"));
        }else{
            String res = publicHolidayService.getPublicHolidayInCalender(from);
            if(AppConsts.TRUE.equals(res)) {
                errMap.put("sub_date", MessageUtil.getMessageDesc("OAPPT_ACK009"));
            }
            if(!(origal != null && publicHolidayDto.getFromDate().equals(origal.getFromDate()))){
                PublicHolidayDto publicHolidayDtoDate = publicHolidayService.publicHoliday(publicHolidayDto.getFromDate());
                if(publicHolidayDtoDate != null){
                    errMap.put("sub_date", MessageUtil.getMessageDesc("OAPPT_ACK010"));
                }
            }

        }

        if(!StringUtil.isEmpty(publicHolidayDto.getDescription()) && !(origal != null && publicHolidayDto.getDescription().equals(origal.getDescription()))   ){
            Calendar c = Calendar.getInstance();
            c.setTime(publicHolidayDto.getFromDate());
            int year = c.get(Calendar.YEAR);
            PublicHolidayDto publicHolidayDtoDis = publicHolidayService.publicHolidayByDis(publicHolidayDto.getDescription(),year);
            if(publicHolidayDtoDis != null){
                errMap.put("description", MessageUtil.getMessageDesc("OAPPT_ACK010"));
            }
        }

        return errMap;
    }
}
