package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.KpiAndReminderService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2020/1/2 20:32
 */
@Service
@Slf4j
public class KpiAndReminderServiceImpl implements KpiAndReminderService {

    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Override
    public void saveKpiAndReminder(HttpServletRequest request) {

        String crud_action_value = request.getParameter("crud_action_value");


        Map errorMap = doValidate(request);
            if(!errorMap.isEmpty()){
                request.setAttribute("message","Please fill up all mandatory fields");
                request.setAttribute("crud_action_type","validate");
                request.setAttribute("errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                return;
            }


        HcsaSvcKpiDto parameter = null;
        try {
            parameter = getParameter(request);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if("servceiConfig".equals(crud_action_value)){
            request.setAttribute("crud_action_type","servcieConfig");
            request.setAttribute("parameter",parameter);
            return;
        }

        hcsaConfigClient.saveKpiAndReminder(parameter);
        request.setAttribute("message","You have successfully created required KPI");
        request.setAttribute("crud_action_type","submit");
    }

    @Override
    public void  getKpiAndReminder(HttpServletRequest request) {
        List<HcsaServiceDto> entity = hcsaConfigClient.getActiveServices().getEntity();

        request.setAttribute("hcsaServiceDtos",entity);

    }

    @Override
    public HcsaSvcKpiDto searchKpi(String service, String module) {
        HcsaSvcKpiDto entity = hcsaConfigClient.searchResult(service, module).getEntity();
        return entity;
    }

    /***********************/

    private HcsaSvcKpiDto getParameter(HttpServletRequest request) throws ParseException {

        Map<String ,Integer> kpi=new HashMap<>();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        OrgUserDto entity = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
        String module = request.getParameter("module");
        String service = request.getParameter("service");
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDto = (List<HcsaSvcRoutingStageDto>) request.getSession().getAttribute("hcsaSvcRoutingStageDtos");
        for(HcsaSvcRoutingStageDto every:hcsaSvcRoutingStageDto){
            String stageCode = every.getStageCode();
            String id = every.getId();
            String parameter = request.getParameter(stageCode);
            kpi.put(id,Integer.parseInt(parameter));
        }

        String reminderThreshold = request.getParameter("reminderThreshold");
        String createDate = request.getParameter("createDate");
        HcsaSvcKpiDto hcsaSvcKpiDto =new HcsaSvcKpiDto();
        hcsaSvcKpiDto.setModule(module);
        hcsaSvcKpiDto.setRemThreshold(Integer.parseInt(reminderThreshold));
        hcsaSvcKpiDto.setStageIdKpi(kpi);
        hcsaSvcKpiDto.setVersion(1);
        hcsaSvcKpiDto.setServiceCode(service);
        if(entity!=null){
            String id = entity.getId();
            hcsaSvcKpiDto.setCreateBy(id);
            hcsaSvcKpiDto.setUpdateBy(id);
        }
        hcsaSvcKpiDto.setCreateDate(new SimpleDateFormat("dd/MM/yyyy hh:mm:ssa",Locale.ENGLISH).parse(createDate));
        AuditTrailDto batchJobDto = AuditTrailHelper.getBatchJobDto("INTERNET");
//        hcsaSvcKpiDto.setAuditTrailDto(batchJobDto);

        return hcsaSvcKpiDto;
    }


    private Map doValidate(HttpServletRequest request){
        Map<String,String> errorMap=new HashMap<>();
        String configKpi = (String)request.getSession().getAttribute("configKpi");
        if(!"configKpi".equals(configKpi)){
            String service = request.getParameter("service");
            request.getSession().setAttribute("service",service);
            if(StringUtil.isEmpty(service)){
                errorMap.put("service","UC_CHKLMD001_ERR001");
            }
        }
        String module = request.getParameter("module");
        request.getSession().setAttribute("module",module);
        String reminderThreshold = request.getParameter("reminderThreshold");
        request.getSession().setAttribute("reminderThreshold",reminderThreshold);
        List<HcsaSvcRoutingStageDto> entity = (List<HcsaSvcRoutingStageDto>) request.getSession().getAttribute("hcsaSvcRoutingStageDtos");

        for(HcsaSvcRoutingStageDto every:entity){
            String stageCode = every.getStageCode();
            String stageCode1 = request.getParameter(stageCode);
          request.setAttribute(stageCode,stageCode1);
            if(StringUtil.isEmpty(stageCode1)){
                errorMap.put(stageCode,"UC_CHKLMD001_ERR001");
            }else {
                if(!stageCode1.matches("^[0-9]{0,5}$")){
                    errorMap.put(stageCode,"UC_CHKLMD001_ERR001");
                }
            }
        }

        if(StringUtil.isEmpty(module)){
            errorMap.put("module","UC_CHKLMD001_ERR001");
        }
        request.setAttribute("module",module);

        if(StringUtil.isEmpty(reminderThreshold)){
            errorMap.put("reminderThreshold","UC_CHKLMD001_ERR001");
        }else {
            if(!reminderThreshold.matches("^[0-9]{0,5}$")){
                errorMap.put("reminderThreshold","UC_CHKLMD001_ERR002");
            }
        }

        return errorMap;
    }


}
