package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * RoundRobinCommPoolBatchJob
 *
 * @author suocheng
 * @date 4/9/2020
 */
@Delegator("roundRobinCommPoolBatchJob")
@Slf4j
public class RoundRobinCommPoolBatchJob {
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private TaskService taskService;

    public void doBatchJob(BaseProcessClass bpc) throws FeignException {
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob start ..."));
        String date = getDate();
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob date -- >:" +date));
        List<TaskDto> taskDtoList = taskService.getTaskDtoByDate(date);
        if(!IaisCommonUtils.isEmpty(taskDtoList)){
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob taskDtoList.size() -- >:" +taskDtoList.size()));
          for (TaskDto taskDto : taskDtoList){
              if(!RoleConsts.USER_ROLE_BROADCAST.equals(taskDto.getRoleId())){
                  try{
                      ApplicationDto applicationDto=applicationClient.getAppByNo(taskDto.getApplicationNo()).getEntity();
                      if(applicationDto.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL)){
                          //update status
                      }
                      String workGroupId = taskDto.getWkGrpId();
                      log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob taskId -- >:" +taskDto.getId()));
                      log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob workGroupId -- >:" +workGroupId));
                      TaskDto taskScoreDto = taskService.getUserIdForWorkGroup(workGroupId);
                      taskDto.setUserId(taskScoreDto.getUserId());
                      taskDto.setDateAssigned(new Date());
                      taskDto.setAuditTrailDto(AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET));
                      taskDto = taskService.updateTask(taskDto);
                  }catch (Exception e){
                      log.error(StringUtil.changeForLog("This  Task can not assign id-->:"+taskDto.getId()));
                  }
              }else{
                  log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob broadcast taskId -- >:" +taskDto.getId()));
              }
          }
        }else{
            log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob do  not need roud robin task !!!"));
        }
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob end ..."));
    }

    private String getDate(){
        int day = systemParamConfig.getRoundRobinCpDays();
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob day -- >:" +day));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR,-day);
        Date result = calendar.getTime();
        String dateStr = Formatter.formatDateTime(result,AppConsts.DEFAULT_DATE_FORMAT);
        log.info(StringUtil.changeForLog("the RoundRobinCommPoolBatchJob dateStr -- >:" +dateStr));
        return dateStr;
    }
}
