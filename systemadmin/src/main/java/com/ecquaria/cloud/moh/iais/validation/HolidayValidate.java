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
        String from = Formatter.formatDateTime(publicHolidayDto.getFromDate(), "ddMMyyyy");
        if(from == null || from.isEmpty()){
            errMap.put("sub_date", MessageUtil.replaceMessage("GENERAL_ERR0006","Non-working Date","field"));
        }else{
            String res = publicHolidayService.getPublicHolidayInCalender(from);
            if(AppConsts.TRUE.equals(res)) {
                errMap.put("sub_date", MessageUtil.getMessageDesc("OAPPT_ERR004"));
            }
            PublicHolidayDto publicHolidayDtoDate = publicHolidayService.publicHoliday(publicHolidayDto.getFromDate());
            if(publicHolidayDtoDate != null){
                errMap.put("sub_date", MessageUtil.getMessageDesc("OAPPT_ERR007"));
            }

        }
        if(StringUtil.isEmpty(publicHolidayDto.getPhCode())){
            errMap.put("phCode",  MessageUtil.replaceMessage("GENERAL_ERR0006","Holiday Description","field"));
        }
        if(StringUtil.isEmpty(publicHolidayDto.getStatus())){
            errMap.put("status",  MessageUtil.replaceMessage("GENERAL_ERR0006","Status","field"));
        }
        return errMap;
    }
}
