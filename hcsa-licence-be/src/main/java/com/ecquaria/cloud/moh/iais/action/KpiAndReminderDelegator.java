package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.service.KpiAndReminderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2020/1/2 15:04
 */
@Delegator("kpiAndReminderDelegator")
@Slf4j
public class KpiAndReminderDelegator {

    @Autowired
    private KpiAndReminderService kpiAndReminderService;

    public void prepareData(BaseProcessClass bpc){
    log.info("-------------start prepareData  KpiAndReminderDelegator--------------");
        Date date=new Date();
        String format = new SimpleDateFormat("dd/mm/yyyy hh:mm:ssa", Locale.ENGLISH).format(date);
        bpc.request.setAttribute("date",format);

        kpiAndReminderService.getKpiAndReminder(bpc.request);



    }


    public void submit(BaseProcessClass bpc){
    log.info("------------ start  submit  KpiAndReminderDelegator ---------------------");


        kpiAndReminderService.saveKpiAndReminder(bpc.request);

        log.info("-------------end submit KpiAndReminderDelegator---------------------------");
    }





}
