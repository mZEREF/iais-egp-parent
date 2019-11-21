package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssQueryDto;

/**
 * @author Shicheng
 * @date 2019/11/14 18:04
 **/

public interface InspectionServie {
    SearchResult<InspecTaskCreAndAssQueryDto> getInspectionPool(SearchParam searchParam);
}
