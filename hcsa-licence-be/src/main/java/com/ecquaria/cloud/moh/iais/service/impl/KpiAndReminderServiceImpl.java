package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.KpiAndReminderService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2020/1/2 20:32
 */
@Service
@Slf4j
public class KpiAndReminderServiceImpl implements KpiAndReminderService {

    private static final String  MODULE_NAME="new";
    private static final String SERVICE="Clinical Laboratory";
    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Override
    public void saveKpiAndReminder(HttpServletRequest request) {

        Map<String, String> parameter = getParameter(request);
        if(!parameter.isEmpty()){
            request.setAttribute("message","Please fill up all mandatory fields");
            return;
        }
        request.setAttribute("message","You have successfully created required KPI");

    }

    @Override
    public void  getKpiAndReminder(HttpServletRequest request) {
        List<HcsaServiceDto> entity = hcsaConfigClient.getActiveServices().getEntity();
        String parameter = request.getParameter("module");
        String service = request.getParameter("service");
        FeignResponseEntity<List<String>> moduleName = hcsaConfigClient.getModuleName(parameter);
        FeignResponseEntity<HcsaSvcKpiDto> hcsaSvcKpiDtoFeignResponseEntity = hcsaConfigClient.searchResult(service, parameter);

        request.setAttribute("module",moduleName);
        request.setAttribute("hcsaServiceDtos",entity);

    }

    /***********************/

    private Map<String,String>  getParameter(HttpServletRequest request){
        Map<String,String> errorMap=new HashMap<>();
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

        if(StringUtil.isEmpty(module)){
            errorMap.put("module","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(service)){
            errorMap.put("service","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(reminderThreshold)){
            errorMap.put("reminderThreshold","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(adminScreening)){
            errorMap.put("adminScreening","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(professionalScreening)){
            errorMap.put("professionalScreening","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(preInspection)){
            errorMap.put("preInspection","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(inspection)){
            errorMap.put("inspection","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(postInspection)){
            errorMap.put("postInspection","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(levelOne)){
            errorMap.put("levelOne","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(levelTwo)){
            errorMap.put("levelTwo","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(levelThree)){
            errorMap.put("levelThree","UC_CHKLMD001_ERR001");
        }

        return errorMap;
    }


    private void doValidate(){

    }


}
