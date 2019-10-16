package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dto.ApplicationDto;

import java.util.List;

public interface InboxTabService {
    public List<ApplicationDto> selectAll();
}
