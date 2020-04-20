package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.service.KpiAndReminderService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    private  OrgUserDto entity;
    @Autowired
    private OrganizationClient organizationClient;
    public void  start(BaseProcessClass bpc){
        clearSession(bpc.request);
        bpc.request.removeAttribute("errorMsg");
    }


    public void cancel(BaseProcessClass bpc){
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName())
                .append("/main-web/eservice/INTRANET/MohBackendInbox");
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(),bpc.request);
        try {
            bpc.response.sendRedirect(tokenUrl);

        } catch (IOException e) {
            log.info(e.getMessage(),e);
        }

    }
    public void prepareData(BaseProcessClass bpc){
    log.info("-------------start prepareData  KpiAndReminderDelegator--------------");

        List<HcsaSvcRoutingStageDto> entity = hcsaConfigClient.getAllHcsaSvcRoutingStage().getEntity();
        for(HcsaSvcRoutingStageDto every:entity){
            String stageCode = every.getStageCode();
            if("INS".equals(stageCode)){
                entity.remove(every);
                break;
            }
        }
        for(HcsaSvcRoutingStageDto every:entity){
            String stageCode = every.getStageCode();
            if("ASO".equals(stageCode)){
                every.setStageName("Admin Screening Process");
            }
            else if("".equals(stageCode)){
                every.setStageName("Professional Screening Process");
            }else if("AO1".equals(stageCode)){
                every.setStageName("Approval Officer Level 1");
            }else if("AO2".equals(stageCode)){
                every.setStageName("Approval Officer Level 2");
            }
            else if("AO3".equals(stageCode)){
                every.setStageName("Approval Officer Level 3");
            }
        }

        bpc.request.getSession().setAttribute("hcsaSvcRoutingStageDtos",entity);
        Date date=new Date();
        String format = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT, Locale.ENGLISH).format(date);
        bpc.request.setAttribute("date",format);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        this.entity = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
        bpc.request.setAttribute("entity", this.entity.getDisplayName());
        kpiAndReminderService.getKpiAndReminder(bpc.request);

    }


    public void submit(BaseProcessClass bpc){
    log.info("------------ start  submit  KpiAndReminderDelegator ---------------------");

        kpiAndReminderService.saveKpiAndReminder(bpc.request,bpc.response);

        log.info("-------------end submit KpiAndReminderDelegator---------------------------");

    }

    @GetMapping(value = "/kpi-reminder-result")
    @ResponseBody
    public Map kpiAndRe(String service, String module){
        Map map =new HashMap();
        HcsaSvcKpiDto hcsaSvcKpiDto = kpiAndReminderService.searchKpi( module,service);
        Map<String, Integer> stageIdKpi = hcsaSvcKpiDto.getStageIdKpi();
        Map<String, String> stageNameKpi = hcsaSvcKpiDto.getStageNameKpi();
        String createBy = hcsaSvcKpiDto.getCreateBy();
        if(!StringUtil.isEmpty(createBy)){
            OrgUserDto entity = organizationClient.retrieveOrgUserAccountById(createBy).getEntity();
            Integer remThreshold = hcsaSvcKpiDto.getRemThreshold();
            String displayName = entity.getDisplayName();

            map.put("remThreshold",remThreshold);
            map.put("entity",entity.getDisplayName());
            if(StringUtil.isEmpty(displayName)){
                map.put("entity",entity.getUserId());
            }
        }else {
            Integer remThreshold = hcsaSvcKpiDto.getRemThreshold();
            String displayName = entity.getDisplayName();

            map.put("remThreshold",remThreshold);
            map.put("entity",entity.getDisplayName());
            if(StringUtil.isEmpty(displayName)){
                map.put("entity",entity.getUserId());
            }
        }

        Date createDate = hcsaSvcKpiDto.getCreateDate();
            if(stageIdKpi!=null){
                stageIdKpi.forEach((k,v)->{
                    String s = stageNameKpi.get(k);
                    map.put(s,v);
                });
            }
            if(createDate==null){
                Date date=new Date();
                String  format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ssa", Locale.ENGLISH).format(date);
                map.put("remThr",format);
            }else {
                String format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ssa", Locale.ENGLISH).format(createDate);
                Formatter.formatDateTime(createDate, "dd/MM/yyyy hh:mm:ssa");
                map.put("remThr",format);
            }

        return map;
    }


    public void  clearSession(HttpServletRequest request){
        request.getSession().removeAttribute("module");
        request.getSession().removeAttribute("configKpi");
        request.getSession().removeAttribute("service");
        request.getSession().removeAttribute("reminderThreshold");
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = (List<HcsaSvcRoutingStageDto>)request.getSession().getAttribute("hcsaSvcRoutingStageDtos");
        if(hcsaSvcRoutingStageDtos!=null){
            for(HcsaSvcRoutingStageDto every:hcsaSvcRoutingStageDtos){
                String stageCode = every.getStageCode();
                request.getSession().removeAttribute(stageCode);
            }
        }


    }
}
