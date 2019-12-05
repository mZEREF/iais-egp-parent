package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/11/27 13:30
 */
@FeignClient(name = "hcsa-config", configuration = FeignConfiguration.class,
        fallback = FillUpCheckListClientFallBack.class)
public interface FillUpCheckListCilent {
    @GetMapping(path = "/iais-hcsa-checklist/config/results/{svcCode}/{svcType}", produces = { MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<ChecklistQuestionDto>> getcheckListQuestionDtoList(@PathVariable(value = "svcCode") String svcCode,@PathVariable(value = "svcType") String svcType);
}
