package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementListDto;
import com.ecquaria.cloud.moh.iais.service.BlastManagementListService;
import com.ecquaria.cloud.moh.iais.service.client.BlastManagementListClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author guyin
 * @date 2020/2/6 10:41
 */
@Service
@Slf4j
public class BlastManagementListServiceImpl implements BlastManagementListService {

    @Autowired
    private BlastManagementListClient blastManagementListClient ;

    @Override
    public SearchResult<BlastManagementListDto> blastList(SearchParam searchParam) {
        return blastManagementListClient.getBlastManagementList(searchParam).getEntity();
    }

    @Override
    public BlastManagementDto saveBlast(BlastManagementDto blastManagementDto){
        return blastManagementListClient.saveBlastList(blastManagementDto).getEntity();
    }

    @Override
    public void deleteBlastList(List<String> list){
        blastManagementListClient.deleteBlastList(list);
    }
    @Override
    public BlastManagementDto getBlastById(String id){
        return blastManagementListClient.getBlastById(id).getEntity();
    }
}
