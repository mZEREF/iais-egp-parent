package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiTreatmentSubsidiesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
@Slf4j
public class IuiTreatmentSubsidiesDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        IuiTreatmentSubsidiesDto iuiTreatmentSubsidiesDto = (IuiTreatmentSubsidiesDto) obj;
        if (iuiTreatmentSubsidiesDto == null) {
            return null;
        }
        if (iuiTreatmentSubsidiesDto.getIuiCount() >= 3  && StringUtil.isEmpty(iuiTreatmentSubsidiesDto.getThereAppeal())) {
            errMap.put("thereAppeal", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
        }
        if (iuiTreatmentSubsidiesDto.getIuiCount() >= 3  && "No".equals(iuiTreatmentSubsidiesDto.getThereAppeal())) {
            errMap.put("thereAppeal","Disallow submission if patient's total co-funded IUI cycles entered in the system is ≥3 and option selected for 'Is there an Appeal?' is 'No'");
        }

        return errMap;
    }
}
