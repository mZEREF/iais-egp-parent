package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/11/27 13:31
 */
public class FillUpCheckListClientFallBack implements  FillUpCheckListCilent{
    @Override
    public FeignResponseEntity<List<ChecklistQuestionDto>> getcheckListQuestionDtoList(String svcCode, String svcType) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
