package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;

import java.util.List;


public interface AssessmentGuideService {

    List<HcsaServiceDto> getServicesInActive();
}
