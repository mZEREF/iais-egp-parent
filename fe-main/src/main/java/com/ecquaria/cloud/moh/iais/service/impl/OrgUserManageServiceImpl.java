package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.dto.OrgUserDto;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrgUserManageServiceImpl implements OrgUserManageService {

    private static final String URL="iais-orgUserRole:8879/iais-orgUserRole/org";

    @Override
    public void saveOrgManage(OrgUserDto orgUserDto) {
        System.err.println("**** OrgUserDto  ****");
//        RestApiUtil.save(URL, orgUserDto,OrgUserDto.class);
    }
}
