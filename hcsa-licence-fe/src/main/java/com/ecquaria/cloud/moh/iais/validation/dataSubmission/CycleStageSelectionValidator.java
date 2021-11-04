package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.Date;
import java.util.Map;

/**
 * @Description CycleStageSelectionValidator
 * @Auther chenlei on 11/4/2021.
 */
public class CycleStageSelectionValidator implements CustomizeValidator {

    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> errorMsg = IaisCommonUtils.genNewHashMap();
        if (!(obj instanceof CycleStageSelectionDto)) {
            return CustomizeValidator.super.validate(obj, profile, request);
        }
        if ("save".equals(profile)) {
            CycleStageSelectionDto selectionDto = (CycleStageSelectionDto) obj;
            ArDataSubmissionService service = SpringHelper.getBean(ArDataSubmissionService.class);
            Date lastStartDate = service.getLastCompletedCycleStartDate(selectionDto.getPatientCode(), selectionDto.getHciCode());
            String period = MasterCodeUtil.getCodeDesc("DS_CP_001");
            if (lastStartDate != null && StringUtil.isDigit(period)) {
                int p = Integer.parseInt(period);
                int target = Formatter.compareDateByDay(lastStartDate);
                if (target < p) {
                    errorMsg.put("patientIdNumber", MessageUtil.getMessageDesc("DS_ERR013"));
                }
            }
        }
        return errorMsg;
    }

}
