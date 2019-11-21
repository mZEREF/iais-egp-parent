package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssQueryDto;
import com.ecquaria.cloud.moh.iais.service.InspectionServie;
import org.springframework.stereotype.Service;

/**
 * @author Shicheng
 * @date 2019/11/19 14:45
 **/
@Service
public class InspectionServiceImpl implements InspectionServie {
    
    @Override
    public SearchResult<InspecTaskCreAndAssQueryDto> getInspectionPool(SearchParam searchParam) {

        return null;
    }
}
