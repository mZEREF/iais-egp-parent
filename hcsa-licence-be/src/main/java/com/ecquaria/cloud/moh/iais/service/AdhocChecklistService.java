package com.ecquaria.cloud.moh.iais.service;

/*
 *author: yichen
 *date time:12/10/2019 1:00 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdhocChecklistService {

    List<ChecklistConfigDto> getInspectionChecklist(ApplicationDto application);
}
