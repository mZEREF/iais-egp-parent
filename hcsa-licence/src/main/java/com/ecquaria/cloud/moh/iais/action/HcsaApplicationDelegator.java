package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.WorkGroupService;
import com.ecquaria.cloud.moh.iais.service.impl.ApplicationViewServiceImp;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.rbac.user.User;
import sop.rbac.user.UserIdentifier;
import sop.usergroup.UserGroup;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * HcsaApplicationDelegator
 *
 * @author suocheng
 * @date 10/17/2019
 */
@Delegator("hcsaApplicationDelegator")
@Slf4j
public class HcsaApplicationDelegator {
    @Autowired
    private TaskService taskService;
    public void routingTask(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
        AuditTrailHelper.auditFunction("hcsa-licence", "hcsa licence");
        List<ApplicationDto> applicationDtos = new ArrayList();
        ApplicationDto applicationDto0 = new ApplicationDto();
        applicationDto0.setApplicationNo("test applicaitonNo");
        applicationDto0.setServiceId("test serviceId");
        applicationDtos.add(applicationDto0);
        routingAdminScranTask(applicationDtos);

        log.debug(StringUtil.changeForLog("the do routingTask end ...."));
    }

    public void routingAdminScranTask(List<ApplicationDto> applicationDtos) throws FeignException {
        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
        List<TaskDto> taskDtos = new ArrayList<>();
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
        hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_ASO);
        hcsaSvcStageWorkingGroupDto = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDto);
        UserIdentifier userIdentifier = getUserIdForWorkGroup(hcsaSvcStageWorkingGroupDto.getGroupShortName());
        if(applicationDtos != null && applicationDtos.size() > 0){
            for(ApplicationDto applicationDto : applicationDtos){
                TaskDto taskDto = TaskUtil.getAsoTaskDto(applicationDto.getServiceId(),
                        applicationDto.getApplicationNo(),hcsaSvcStageWorkingGroupDto.getGroupShortName(),
                        userIdentifier.getId(),userIdentifier.getUserDomain(),
                        IaisEGPHelper.getCurrentAuditTrailDto() );
                taskDtos.add(taskDto);
            }
        }else{
            log.error(StringUtil.changeForLog("The applicationDtos is null"));
        }
        taskService.createTasks(taskDtos);
        log.debug(StringUtil.changeForLog("the do routingTask end ...."));
    }
    private UserIdentifier getUserIdForWorkGroup(String workGroupName) throws FeignException {
        List<UserGroup> userGroups = WorkGroupService.getInstance().retrieveAgencyWorkingGroup(workGroupName);
        List<User> users = null;
        if(userGroups!=null && userGroups.size()>0){
            users = WorkGroupService.getInstance().getUsersByGroupNo(userGroups.get(0).getGroupNo());
            //todo: for round robin get the user.
        }
        return users.get(0).getUserIdentifier();
    }


    /**
     * StartStep: prepareData
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareData start ...."));
        String  appNo = "AN1911136061-01";
        ApplicationViewServiceImp applicationViewService = new ApplicationViewServiceImp();
        ApplicationViewDto applicationViewDto = applicationViewService.searchByAppNo(appNo);
        HttpServletRequest request=bpc.request;
        ParamUtil.setRequestAttr(request,"applicationViewDto", applicationViewDto);
        log.debug(StringUtil.changeForLog("the do prepareData end ...."));
    }



}
