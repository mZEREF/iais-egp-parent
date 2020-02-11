package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shicheng
 * @date 2020/2/11 11:32
 **/
@Service
@Slf4j
public class ApptInspectionDateServiceImpl implements ApptInspectionDateService {

    @Override
    public List<String> getInspectionDate(String taskId, ApptInspectionDateDto apptInspectionDateDto) {
        return null;
    }

    @Override
    public List<SelectOption> getProcessDecList() {
        String[] processDecArr = new String[]{InspectionConstants.PROCESS_DECI_ALLOW_SYSTEM_TO_PROPOSE_DATE, InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE};
        List<SelectOption> processDecOption = MasterCodeUtil.retrieveOptionsByCodes(processDecArr);
        return processDecOption;
    }

    @Override
    public List<SelectOption> getInspectionDateHours() {
        return null;
    }

    @Override
    public List<SelectOption> getAmPmOption() {
        List<SelectOption> amPmOption = new ArrayList<>();
        SelectOption so1 = new SelectOption("am", "am");
        SelectOption so2 = new SelectOption("pm", "pm");
        amPmOption.add(so1);
        amPmOption.add(so2);
        return amPmOption;
    }

    @Override
    public void saveLeadSpecificDate(ApptInspectionDateDto apptInspectionDateDto) {

    }

    @Override
    public void saveSystemInspectionDate(ApptInspectionDateDto apptInspectionDateDto) {

    }
}
