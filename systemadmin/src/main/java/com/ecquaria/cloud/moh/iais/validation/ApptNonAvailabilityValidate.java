package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/2/5 9:46
 **/
@Component
public class ApptNonAvailabilityValidate implements CustomizeValidator {

    @Autowired
    private OrganizationClient organizationClient;

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = (ApptNonAvailabilityDateDto) ParamUtil.getSessionAttr(request, "inspNonAvailabilityDto");
        List<Date> inspectionDate = IaisCommonUtils.genNewArrayList();
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> nonAvaDate = MiscUtil.getDateInPeriodByRecurrence(apptNonAvailabilityDateDto.getBlockOutStart(),
                        apptNonAvailabilityDateDto.getBlockOutEnd(), apptNonAvailabilityDateDto.getRecurrence());

        for(Date date : nonAvaDate){
            for(Date inspDate : inspectionDate){
                String nonDate = sdf2.format(date);
                String inspecDate = sdf2.format(inspDate);
                if(nonDate.equals(inspecDate)){
                    errMap.put("nonAvaStartDate", "UC_INSP_ERR0004");
                }
            }
        }
        return errMap;
    }
}
