package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
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

        String from = Formatter.formatDateTime(publicHolidayDto.getFromDate(), "ddMMyyyy");
        if(from == null || from.isEmpty()){
            errMap.put("sub_date", MessageUtil.getMessageDesc(MessageCodeKey.USER_ERR001));
        }else{
            String res = publicHolidayService.getPublicHolidayInCalender(from);
            if(AppConsts.TRUE.equals(res)) {
                errMap.put("sub_date", MessageUtil.getMessageDesc(MessageCodeKey.OAPPT004));
            }
        }

        return errMap;
    }
}
