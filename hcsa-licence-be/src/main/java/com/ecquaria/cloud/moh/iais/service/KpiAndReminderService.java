package com.ecquaria.cloud.moh.iais.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wenkang
 * @date 2020/1/2 20:32
 */
public interface KpiAndReminderService  {

    void saveKpiAndReminder(HttpServletRequest request);

    void  getKpiAndReminder(HttpServletRequest request);
}
