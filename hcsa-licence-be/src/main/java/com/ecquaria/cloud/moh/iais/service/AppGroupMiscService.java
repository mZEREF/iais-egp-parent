package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.NotificateApplicationDto;
import java.util.List;

/**
 * AppGroupMiscService
 *
 * @author suocheng
 * @date 5/13/2020
 */

public interface AppGroupMiscService {
    public List<NotificateApplicationDto> getNotificateApplicationDtos();

    public NotificateApplicationDto saveNotificateApplicationDto(NotificateApplicationDto notificateApplicationDto);

    public void notificationApplicationUpdateBatchjob();
}
