package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/2/15 10:34
 **/
@Component
public class ApptInspectionDateValidate implements CustomizeValidator {

    @Autowired
    private AppointmentClient appointmentClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(request, "apptInspectionDateDto");
        Date specificDate = apptInspectionDateDto.getSpecificDate();
        if(specificDate == null){
            return null;
        }
        Map<String, String> errMap = new HashMap<>();
        List<Date> inspectionDate = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        String strSpecDate = sdf.format(specificDate);
        List<TaskDto> taskDtoList = apptInspectionDateDto.getTaskDtos();
        if(!IaisCommonUtils.isEmpty(taskDtoList)){
            for(TaskDto tDto : taskDtoList){
                List<TaskDto> taskDtos = organizationClient.getTasksByUserIdAndRole(tDto.getUserId(), tDto.getRoleId()).getEntity();
                for(TaskDto taskDto : taskDtos){
                    AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(taskDto.getRefNo(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                    if(appPremisesRecommendationDto.getStatus().equals(AppConsts.COMMON_STATUS_ACTIVE)){
                        inspectionDate.add(appPremisesRecommendationDto.getRecomInDate());
                    }
                }
            }
        }
        for(Date inspDate : inspectionDate){
            String inspecDate = sdf.format(inspDate);
            if(specificDate.equals(inspecDate)){
                errMap.put("specificDate", "UC_INSP_ERR0007");
            }
        }
        String dateContainFlag = appointmentClient.isAvailableAppointmentDates(strSpecDate).getEntity();
        if(AppConsts.FALSE.equals(dateContainFlag)){
            errMap.put("specificDate", "UC_INSP_ERR0007");
        }
        return errMap;
    }
}
