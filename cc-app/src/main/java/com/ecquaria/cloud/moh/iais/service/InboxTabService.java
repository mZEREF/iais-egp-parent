package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationDto;

import java.util.List;

public interface InboxTabService {
    List<ApplicationDto> selectAll();
}
