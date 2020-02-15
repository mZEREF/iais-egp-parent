package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/2/15 10:34
 **/
public class ApptInspectionDateValidate implements CustomizeValidator {

    @Autowired
    private AppointmentClient appointmentClient;

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(request, "apptInspectionDateDto");
        Date specificDate = apptInspectionDateDto.getSpecificDate();
        if(specificDate == null){
            return null;
        }
        Map<String, String> errMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strSpecDate = sdf.format(specificDate);
        String dateContainFlag = appointmentClient.isAvailableAppointmentDates(strSpecDate).getEntity();
        if(AppConsts.FALSE.equals(dateContainFlag)){
            errMap.put("specificDate", "");
        }
        return errMap;
    }
}
