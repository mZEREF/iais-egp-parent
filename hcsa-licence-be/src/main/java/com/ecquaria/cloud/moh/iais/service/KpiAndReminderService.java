package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Wenkang
 * @date 2020/1/2 20:32
 */
public interface KpiAndReminderService  {

    void saveKpiAndReminder(HttpServletRequest request, HttpServletResponse response);

    void  getKpiAndReminder(HttpServletRequest request);
    HcsaSvcKpiDto searchKpi(String service,String module);
}
