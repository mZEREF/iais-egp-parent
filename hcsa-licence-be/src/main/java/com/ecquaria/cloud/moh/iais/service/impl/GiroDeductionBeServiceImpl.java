package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.GiroDeductionBeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Shicheng
 * @date 2020/10/19 15:52
 **/
@Service
@Slf4j
public class GiroDeductionBeServiceImpl implements GiroDeductionBeService {

    @Override
    public void sendMessageEmail(List<String> appGroupList) {
        if(!IaisCommonUtils.isEmpty(appGroupList)){
            for(String appGroupNo : appGroupList){

            }
        } else {
            log.info("Giro Deduction appGroupList is null");
        }
    }
}
