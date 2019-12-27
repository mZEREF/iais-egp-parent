package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import com.ecquaria.cloud.moh.iais.service.client.IntranetUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author weilu
 * @date 2019/12/25 17:41
 */
@Service
@Slf4j
public class IntranetUserServiceImpl implements IntranetUserService {

    @Autowired
    private IntranetUserClient intranetUserClient ;

    @Override
    public void createIntranetUser(OrgUserDto orgUserDto) {

    }

    @Override
    public SearchResult<OrgUserQueryDto> doQuery(SearchParam param) {
        return intranetUserClient.doQuery(param).getEntity();
    }
}
