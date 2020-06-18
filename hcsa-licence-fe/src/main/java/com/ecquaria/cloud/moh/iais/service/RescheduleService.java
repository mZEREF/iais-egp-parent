package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschApptGrpPremsQueryDto;

/**
 * RescheduleService
 *
 * @author junyu
 * @date 2020/6/18
 */
public interface RescheduleService {
    SearchResult<ReschApptGrpPremsQueryDto> searchApptReschPrem(SearchParam searchParam);
}
