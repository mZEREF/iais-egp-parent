package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskScoreDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.TaskScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sop.rbac.user.User;

import java.util.List;

/**
 * TaskScoreServiceImpl
 *
 * @author suocheng
 * @date 11/21/2019
 */
@Service
@Slf4j
public class TaskScoreServiceImpl implements TaskScoreService {
    @Override
    public List<TaskScoreDto> getTaskScores(String workGroupName) {
        return RestApiUtil.getListByPathParam(RestApiUrlConsts.TASKSCORES_WORKGROUPNAME,workGroupName,TaskScoreDto.class);
    }

    @Override
    public TaskScoreDto getLowestTaskScore(List<TaskScoreDto> taskScoreDtos, List<User> users) {
        TaskScoreDto result = null;
        //There is not user in this workgroup return null
        if(users == null || users.size() ==0){
          return  result;
        }
        //There is not taskScoreDtos ,return the users first.
        if(taskScoreDtos == null || taskScoreDtos.size() == 0){
            result = new TaskScoreDto();
            result.setUserId(users.get(0).getUserIdentifier().getId());
            result.setUserDomain(users.get(0).getUserIdentifier().getUserDomain());
            result.setScore(0);
        }else{
            //if user do not Exist in the taskScoreDtos, return this user
            for(User user : users){
               if(!StringUtil.isEmpty(user.getUserIdentifier().getId()) &&
                       !StringUtil.isEmpty(user.getUserIdentifier().getUserDomain()) ){
                   boolean isExist = isExist(taskScoreDtos,user.getUserIdentifier().getId(),
                           user.getUserIdentifier().getUserDomain());
                   if(!isExist){
                       result = new TaskScoreDto();
                       result.setUserId(user.getUserIdentifier().getId());
                       result.setUserDomain(user.getUserIdentifier().getUserDomain());
                       result.setScore(0);
                       break;
                   }
               }
            }
        }
        //there is not new , return the Lowest Score taskScoreDtos. because there is sort in the SQL side
        if(result == null){
            result = taskScoreDtos.get(0);
            result.setScore(0);
        }
        return result;
    }

    @Override
    public TaskScoreDto createTaskScore(TaskScoreDto taskScoreDto) {
        taskScoreDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return RestApiUtil.save(RestApiUrlConsts.IAIS_TASK_SCORE,taskScoreDto,TaskScoreDto.class);
    }

    private boolean isExist(List<TaskScoreDto> taskScoreDtos,String userId,String domain){
      boolean result = false;
      for (TaskScoreDto taskScoreDto : taskScoreDtos){
         if(userId.equals(taskScoreDto.getUserId()) && domain.equals(taskScoreDto.getUserDomain())){
          result = true;
          break;
         }
      }
      return result;
    }
}
