package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HtmlElementHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author zhilin
 * @date 2020/10/29
 */
@Controller
@Slf4j
public class InspectionForSixthAjaxController {

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private ApplicationGroupService applicationGroupService;

    @RequestMapping(value = "/callInspectionForSixth",method = RequestMethod.POST)
    public @ResponseBody String callInspectionForSixth(HttpServletRequest request){
        String data = "";
        String chooseInspection = request.getParameter("chooseInspection");
        String verified = request.getParameter("verified");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request,"taskDto");
        List<SelectOption> selectOptions = null;
        Map<String, String> attributes = IaisCommonUtils.genNewHashMap();
        attributes.put("name","verified");
        if("N".equals(chooseInspection)){
            selectOptions = setVerifiedDropdownValue(request, applicationViewDto, taskDto, true);
            data = HtmlElementHelper.generateSelect(attributes,selectOptions,null,RoleConsts.USER_ROLE_AO1,selectOptions.size());
        }else{
            selectOptions = setVerifiedDropdownValue(request,applicationViewDto,taskDto,false);
            data = HtmlElementHelper.generateSelect(attributes, selectOptions, null, verified, selectOptions.size());
        }
        return data;
    }

    public List<SelectOption> setVerifiedDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto, TaskDto taskDto, boolean hasInspectionVeryDropdown){
        //get routing stage dropdown send to page.
        log.debug(StringUtil.changeForLog("the do prepareData get the hcsaSvcRoutingStageDtoList"));
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String serviceId = applicationDto.getServiceId();
        ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList = applicationViewService.getStage(serviceId,
                taskDto.getTaskKey(),applicationViewDto.getApplicationDto().getApplicationType(),applicationGroupDto.getIsPreInspection());
        String appStatus = applicationDto.getStatus();
        String applicationType = applicationDto.getApplicationType();

        List<SelectOption> routingStage = IaisCommonUtils.genNewArrayList();
        if(hcsaSvcRoutingStageDtoList!=null){
            if(hcsaSvcRoutingStageDtoList.size()>0){
                for (HcsaSvcRoutingStageDto hcsaSvcRoutingStage:hcsaSvcRoutingStageDtoList) {
                    if(!hasInspectionVeryDropdown){
                        if(RoleConsts.PROCESS_TYPE_INS.equals(hcsaSvcRoutingStage.getStageCode())){
                            continue;
                        }
                    }
                    routingStage.add(new SelectOption(hcsaSvcRoutingStage.getStageCode(),hcsaSvcRoutingStage.getStageName()));
                    if(hcsaSvcRoutingStage.isRecommend()){
                        ParamUtil.setRequestAttr(request,"selectVerified",hcsaSvcRoutingStage.getStageCode());
                        ParamUtil.setSessionAttr(request,"RecommendValue",hcsaSvcRoutingStage.getStageCode());
                    }
                }
                if(appStatus.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01) || appStatus.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02)){
                    HcsaSvcRoutingStageDto canApproveStageDto = getCanApproveStageDto(applicationType, appStatus, serviceId);
                    boolean canApprove = checkCanApproveStage(canApproveStageDto);
                    if(canApprove){
                        routingStage.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL,
                                "Approve"));
                        ParamUtil.setSessionAttr(request,"Ao1Ao2Approve","Y");
                    }
                }
            }else{
                log.debug(StringUtil.changeForLog("the do prepareData add the Approve"));
                //if  this is the last stage
                routingStage.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL,
                        "Approve"));
            }
        }
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus())){
            routingStage.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_BROADCAST_REPLY,"Broadcast Reply For Internal"));
        }
        //set verified values
        ParamUtil.setSessionAttr(request, "verifiedValues", (Serializable)routingStage);
        return routingStage;
    }

    private boolean checkCanApproveStage(HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto){
        if(hcsaSvcRoutingStageDto == null){
            return false;
        }
        boolean flag = false;
        String canApprove = hcsaSvcRoutingStageDto.getCanApprove();
        if("1".equals(canApprove)){
            flag = true;
        }
        return flag;
    }

    private HcsaSvcRoutingStageDto getCanApproveStageDto(String appType, String appStatus, String serviceId){
        if(StringUtil.isEmpty(appType) || StringUtil.isEmpty(appStatus) || StringUtil.isEmpty(serviceId)){
            return null;
        }
        String stageId = HcsaConsts.ROUTING_STAGE_AO1;
        if(appStatus.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01)){
            stageId = HcsaConsts.ROUTING_STAGE_AO1;
        }else if(appStatus.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02)){
            stageId = HcsaConsts.ROUTING_STAGE_AO2;
        }
        HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto = new HcsaSvcRoutingStageDto();
        hcsaSvcRoutingStageDto.setStageId(stageId);
        hcsaSvcRoutingStageDto.setServiceId(serviceId);
        hcsaSvcRoutingStageDto.setAppType(appType);
        HcsaSvcRoutingStageDto result = hcsaConfigClient.getHcsaSvcRoutingStageDto(hcsaSvcRoutingStageDto).getEntity();
        return result;
    }
}
