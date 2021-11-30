package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.KpiAndReminderService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.impl.KpiAndReminderServiceImpl;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @Autowired
    private OrganizationClient organizationClient;
    public void  start(BaseProcessClass bpc){
        KpiAndReminderServiceImpl.clearSession(bpc.request);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG, AuditTrailConsts.FUNCTION_DEFINE_KPI);
        bpc.request.removeAttribute("errorMsg");
    }


    public void cancel(BaseProcessClass bpc){
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName())
                .append("/main-web/eservice/INTRANET/MohHcsaBeDashboard");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(),bpc.request);
        try {
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);

        } catch (IOException e) {
            log.info(e.getMessage(),e);
        }

    }
    public void prepareData(BaseProcessClass bpc){
    log.info("-------------start prepareData  KpiAndReminderDelegator--------------");

        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = hcsaConfigClient.getAllHcsaSvcRoutingStage().getEntity();
        for(HcsaSvcRoutingStageDto every:hcsaSvcRoutingStageDtos){
            String stageCode = every.getStageCode();
            if("INS".equals(stageCode)){
                hcsaSvcRoutingStageDtos.remove(every);
                break;
            }
        }
        for(HcsaSvcRoutingStageDto every:hcsaSvcRoutingStageDtos){
            String stageCode = every.getStageCode();
            if("ASO".equals(stageCode)){
                every.setStageName("Admin Screening Process");
            }
            else if("PSO".equals(stageCode)){
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

        ParamUtil.setSessionAttr(bpc.request, "hcsaSvcRoutingStageDtos" ,hcsaSvcRoutingStageDtos);
        Date date=new Date();
        String format = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT, Locale.ENGLISH).format(date);
        ParamUtil.setSessionAttr(bpc.request, "date",format);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
        ParamUtil.setRequestAttr(bpc.request,"orgUserName", orgUserDto.getDisplayName());
        kpiAndReminderService.getKpiAndReminder(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "orgUserDtoAttr", orgUserDto);
    }


    public void submit(BaseProcessClass bpc){
    log.info("------------ start  submit  KpiAndReminderDelegator ---------------------");

        kpiAndReminderService.saveKpiAndReminder(bpc.request,bpc.response);

        log.info("-------------end submit KpiAndReminderDelegator---------------------------");

    }

    @GetMapping(value = "/kpi-reminder-result")
    @ResponseBody
    public Map kpiAndRe(HttpServletRequest request, String service, String module){
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
            OrgUserDto orgUserDto = (OrgUserDto) ParamUtil.getSessionAttr(request, "orgUserDtoAttr");
            String displayName = orgUserDto.getDisplayName();

            map.put("remThreshold",remThreshold);
            map.put("entity",orgUserDto.getDisplayName());
            if(StringUtil.isEmpty(displayName)){
                map.put("entity",orgUserDto.getUserId());
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
                String  format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(date);
                map.put("remThr",format);
            }else {
                String format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(createDate);
                Formatter.formatDateTime(createDate, "dd/MM/yyyy");
                map.put("remThr",format);
            }

        return map;
    }



}
