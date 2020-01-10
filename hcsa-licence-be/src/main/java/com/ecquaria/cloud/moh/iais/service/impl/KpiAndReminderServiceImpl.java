package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
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

        Map errorMap = doValidate(request);
            if(!errorMap.isEmpty()){
                request.setAttribute("message","Please fill up all mandatory fields");
                request.setAttribute("crud_action_type","cancel");
                request.setAttribute("errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                return;
            }

        HcsaSvcKpiDto parameter = null;
        try {
            parameter = getParameter(request);
        } catch (ParseException e) {
            e.printStackTrace();
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
        String reminderThreshold = request.getParameter("reminderThreshold");
        String adminScreening = request.getParameter("adminScreening");
        String professionalScreening = request.getParameter("professionalScreening");
        String preInspection = request.getParameter("preInspection");
        String inspection = request.getParameter("inspection");
        String postInspection = request.getParameter("postInspection");
        String levelOne = request.getParameter("levelOne");
        String levelTwo = request.getParameter("levelTwo");
        String levelThree = request.getParameter("levelThree");
        String createDate = request.getParameter("createDate");
        HcsaSvcKpiDto hcsaSvcKpiDto =new HcsaSvcKpiDto();
        hcsaSvcKpiDto.setModule(module);
        hcsaSvcKpiDto.setRemThreshold(Integer.parseInt(reminderThreshold));
        kpi.put("12848A70-820B-EA11-BE7D-000C29F371DC",Integer.parseInt(adminScreening));
        kpi.put("13848A70-820B-EA11-BE7D-000C29F371DC",Integer.parseInt(professionalScreening));
        kpi.put("298BCC95-5130-EA11-BE7D-000C29F371DC",Integer.parseInt(inspection));
        kpi.put("15848A70-820B-EA11-BE7D-000C29F371DC",Integer.parseInt(levelOne));
        kpi.put("16848A70-820B-EA11-BE7D-000C29F371DC",Integer.parseInt(levelTwo));
        kpi.put("17848A70-820B-EA11-BE7D-000C29F371DC",Integer.parseInt(levelThree));
        kpi.put("288BCC95-5130-EA11-BE7D-000C29F371DC",Integer.parseInt(preInspection));
        kpi.put("2A8BCC95-5130-EA11-BE7D-000C29F371DC",Integer.parseInt(postInspection));
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
        String module = request.getParameter("module");
        request.getSession().setAttribute("module",module);
        String service = request.getParameter("service");
        request.getSession().setAttribute("service",service);
        String reminderThreshold = request.getParameter("reminderThreshold");
        request.getSession().setAttribute("reminderThreshold",reminderThreshold);
        String adminScreening = request.getParameter("adminScreening");
        request.getSession().setAttribute("adminScreening",adminScreening);
        String professionalScreening = request.getParameter("professionalScreening");
        request.getSession().setAttribute("professionalScreening",professionalScreening);
        String preInspection = request.getParameter("preInspection");
        request.getSession().setAttribute("preInspection",preInspection);
        String inspection = request.getParameter("inspection");
        request.getSession().setAttribute("inspection",inspection);
        String postInspection = request.getParameter("postInspection");
        request.getSession().setAttribute("postInspection",postInspection);
        String levelOne = request.getParameter("levelOne");
        request.getSession().setAttribute("levelOne",levelOne);
        String levelTwo = request.getParameter("levelTwo");
        request.getSession().setAttribute("levelTwo",levelTwo);
        String levelThree = request.getParameter("levelThree");
        request.getSession().setAttribute("levelThree",levelThree);
        String createDate = request.getParameter("createDate");
        if(StringUtil.isEmpty(module)){
            errorMap.put("module","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(service)){
            errorMap.put("service","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(reminderThreshold)){
            errorMap.put("reminderThreshold","UC_CHKLMD001_ERR001");
        }else {
            if(!reminderThreshold.matches("^[0-9]*$")){
                errorMap.put("reminderThreshold","UC_CHKLMD001_ERR002");
            }
        }
        if(StringUtil.isEmpty(adminScreening)){
            errorMap.put("adminScreening","UC_CHKLMD001_ERR001");
        }else {
            if(!adminScreening.matches("^[0-9]*$")){
                errorMap.put("adminScreening","UC_CHKLMD001_ERR002");
            }
        }
        if(StringUtil.isEmpty(professionalScreening)){
            errorMap.put("professionalScreening","UC_CHKLMD001_ERR001");
        }else {
            if(!professionalScreening.matches("^[0-9]*$")){
                errorMap.put("professionalScreening","UC_CHKLMD001_ERR002");
            }
        }
        if(StringUtil.isEmpty(preInspection)){
            errorMap.put("preInspection","UC_CHKLMD001_ERR001");
        }else {
            if(!preInspection.matches("^[0-9]*$")){
                errorMap.put("preInspection","UC_CHKLMD001_ERR002");
            }
        }
        if(StringUtil.isEmpty(inspection)){
            errorMap.put("inspection","UC_CHKLMD001_ERR001");
        }else {
            if(!inspection.matches("^[0-9]*$")){
                errorMap.put("inspection","UC_CHKLMD001_ERR002");
            }
        }
        if(StringUtil.isEmpty(postInspection)){
            errorMap.put("postInspection","UC_CHKLMD001_ERR001");
        }
        else {
            if(!postInspection.matches("^[0-9]*$")){
                errorMap.put("postInspection","UC_CHKLMD001_ERR002");
            }
        }
        if(StringUtil.isEmpty(levelOne)){
            errorMap.put("levelOne","UC_CHKLMD001_ERR001");
        }else {
            if(!levelOne.matches("^[0-9]*$")){
                errorMap.put("levelOne","UC_CHKLMD001_ERR002");
            }
        }
        if(StringUtil.isEmpty(levelTwo)){
            errorMap.put("levelTwo","UC_CHKLMD001_ERR001");
        }else {
            if(!levelTwo.matches("^[0-9]*$")){
                errorMap.put("levelTwo","UC_CHKLMD001_ERR002");
            }
        }
            if(StringUtil.isEmpty(levelThree)){
            errorMap.put("levelThree","UC_CHKLMD001_ERR001");
        }else {
            if(!levelThree.matches("^[0-9]*$")){
                errorMap.put("levelThree","UC_CHKLMD001_ERR002");
            }
        }

        return errorMap;
    }


}
