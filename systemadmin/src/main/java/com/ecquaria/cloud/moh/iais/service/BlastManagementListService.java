package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementListDto;

import java.util.List;

/**
 * @author guyin
 * @date 2019/12/28 10:40
 */
public interface BlastManagementListService {
    SearchResult<BlastManagementListDto> blastList(SearchParam searchParam);
    BlastManagementDto saveBlast(BlastManagementDto blastManagementDto);
    void deleteBlastList(List<String> list);
    BlastManagementDto getBlastById(String id);
}
