package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.StepColorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/6/2 10:20
 */
@Service
@Slf4j
public class StepColorServiceImpl implements StepColorService {

    @Override
    public void setStepColor(Map<String, String> map, String serviceConfig, AppSubmissionDto appSubmissionDto) {
        List<String> strList = new ArrayList<>(5);
        if(map!=null){
            map.forEach((k, v) -> {
                if (!StringUtil.isEmpty(v)) {
                    strList.add(v);
                }
            });
        }
        if(serviceConfig!=null){
            strList.add(serviceConfig);
        }
        appSubmissionDto.setStepColor(strList);
    }
}
