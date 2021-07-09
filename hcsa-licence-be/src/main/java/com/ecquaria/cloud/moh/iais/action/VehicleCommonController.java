package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;


/**
 * @author wangyu
 * @date 2021/7/9
 */
@Controller
@Slf4j
public class VehicleCommonController {

    @Autowired
    private ApplicationService applicationService;

    public void setVehicleInformation(HttpServletRequest request, TaskDto taskDto, ApplicationViewDto applicationViewDto,String vehicleOpenFlag){
        //get vehicle flag
        String vehicleFlag = applicationService.getVehicleFlagToShowOrEdit(taskDto, vehicleOpenFlag, applicationViewDto);
        //get vehicleNoList for edit
        List<String> vehicleNoList = applicationService.getVehicleNoByFlag(vehicleFlag, applicationViewDto);
        //sort AppSvcVehicleDto List
        applicationViewDto = applicationService.sortAppSvcVehicleListToShow(vehicleNoList, applicationViewDto);
        ParamUtil.setSessionAttr(request,  HcsaLicenceBeConstant.APP_VEHICLE_NO_LIST, (Serializable) vehicleNoList);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.APP_VEHICLE_FLAG, vehicleFlag);
    }

    public void clearVehicleInformationSession(HttpServletRequest request){
        ParamUtil.setSessionAttr(request,  HcsaLicenceBeConstant.APP_VEHICLE_NO_LIST, null);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.APP_VEHICLE_FLAG, null);
    }


}
