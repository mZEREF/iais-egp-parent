package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;

import com.ecquaria.cloud.moh.iais.service.KpiAndReminderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
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

    public void  start(BaseProcessClass bpc){
        clearSession(bpc.request);
       bpc.request.removeAttribute("errorMsg");
    }


    public void prepareData(BaseProcessClass bpc){
    log.info("-------------start prepareData  KpiAndReminderDelegator--------------");

//        clearSession(bpc.request);
//        bpc.request.removeAttribute("errorMsg");
        Date date=new Date();
        String format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ssa", Locale.ENGLISH).format(date);
        bpc.request.setAttribute("date",format);

        kpiAndReminderService.getKpiAndReminder(bpc.request);



    }


    public void submit(BaseProcessClass bpc){
    log.info("------------ start  submit  KpiAndReminderDelegator ---------------------");

        kpiAndReminderService.saveKpiAndReminder(bpc.request);

        log.info("-------------end submit KpiAndReminderDelegator---------------------------");

    }

    @GetMapping(value = "/kpi-reminder-result")
    @ResponseBody
    public Map kpiAndRe(String service, String module){
        Map map =new HashMap();
        HcsaSvcKpiDto hcsaSvcKpiDto = kpiAndReminderService.searchKpi( module,service);
        Map<String, Integer> stageIdKpi = hcsaSvcKpiDto.getStageIdKpi();
        Map<String, String> stageNameKpi = hcsaSvcKpiDto.getStageNameKpi();
        Integer remThreshold = hcsaSvcKpiDto.getRemThreshold();
        map.put("remThreshold",remThreshold);
        Date createDate = hcsaSvcKpiDto.getCreateDate();
        String format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ssa", Locale.ENGLISH).format(createDate);
        stageIdKpi.forEach((k,v)->{
            String s = stageNameKpi.get(k);
            map.put(s,v);
        });
        map.put("remThr",format);
        return map;
    }


    public void  clearSession(HttpServletRequest request){
        request.getSession().removeAttribute("module");
        request.getSession().removeAttribute("service");
        request.getSession().removeAttribute("reminderThreshold");
        request.getSession().removeAttribute("adminScreening");
        request.getSession().removeAttribute("professionalScreening");
        request.getSession().removeAttribute("preInspection");
        request.getSession().removeAttribute("inspection");
        request.getSession().removeAttribute("postInspection");
        request.getSession().removeAttribute("levelOne");
        request.getSession().removeAttribute("levelTwo");
        request.getSession().removeAttribute("levelThree");


    }
}
