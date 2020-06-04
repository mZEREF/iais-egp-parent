package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2020-03-09 12:41
 **/
@Component
public class BlastValidate implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        BlastManagementDto blastManagementDto = (BlastManagementDto) ParamUtil.getSessionAttr(request, "edit");
        if(blastManagementDto.getEmailAddress() != null){
            for (String item :blastManagementDto.getEmailAddress()
                 ) {
                if(!item.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
                    errMap.put("addr","Please key in a valid email address");
                }
            }
        }
        if(blastManagementDto.getSchedule() == null){
            errMap.put("date","The field is mandatory.");
        }
        String HH = blastManagementDto.getHH();
        String MM = blastManagementDto.getMM();
        if(HH == null){
            errMap.put("HH","The field is mandatory.");
        }else if(!(StringUtils.isNumeric(HH) &&  Integer.parseInt(HH) < 24)){
            errMap.put("HH","Field format is wrong");
        }
        if(MM == null){
            errMap.put("HH","The field is mandatory.");
        }else if(!(StringUtils.isNumeric(MM) &&  Integer.parseInt(MM) < 60)){
            errMap.put("HH","Field format is wrong");
        }
        if(blastManagementDto.getSchedule() != null && HH != null && MM != null) {
            SimpleDateFormat newformat = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
            Date schedule = new Date();
            schedule = blastManagementDto.getSchedule();
            long time = schedule.getTime() + Long.parseLong(HH) * 60 * 60 * 1000 + Long.parseLong(MM) * 60 * 1000;
            schedule.setTime(time);
            Date now = new Date();
            if (schedule.compareTo(now) < 0) {
                errMap.put("date", "Send date and time cannot be earlier than now");
            }
        }
        return errMap;
    }
}
