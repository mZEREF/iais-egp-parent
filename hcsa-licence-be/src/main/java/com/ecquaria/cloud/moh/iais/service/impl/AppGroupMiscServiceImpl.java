package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.NotificateApplicationDto;
import com.ecquaria.cloud.moh.iais.service.AppGroupMiscService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AppGroupMiscServiceImpl
 *
 * @author suocheng
 * @date 5/13/2020
 */
@Service
@Slf4j
public class AppGroupMiscServiceImpl implements AppGroupMiscService {
    @Autowired
    private ApplicationClient applicationClient;
    @Override
    public List<NotificateApplicationDto> getNotificateApplicationDtos() {
        return applicationClient.getNotificateApplicationDtos().getEntity();
    }

    @Override
    public NotificateApplicationDto saveNotificateApplicationDto(NotificateApplicationDto notificateApplicationDto) {
        return applicationClient.updateNotificateApplicationDto(notificateApplicationDto).getEntity();
    }
}
