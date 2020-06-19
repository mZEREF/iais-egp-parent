package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.service.RescheduleService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RescheduleServiceImpl
 *
 * @author junyu
 * @date 2020/6/18
 */
@Slf4j
@Service
public class RescheduleServiceImpl implements RescheduleService {
    @Autowired
    ApplicationClient applicationClient;


}
