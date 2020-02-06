package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveDto;
import java.util.List;

/**
 * AppealService
 *
 * @author suocheng
 * @date 2/6/2020
 */

public interface AppealService {
    public List<AppealApproveDto> getAppealApproveDtos();
}
