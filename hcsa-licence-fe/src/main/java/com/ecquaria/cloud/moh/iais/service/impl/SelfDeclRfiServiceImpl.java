package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.service.SelfDeclRfiService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: yichen
 * @description:
 * @date:2020/3/24
 **/

@Service
@Slf4j
public class SelfDeclRfiServiceImpl implements SelfDeclRfiService {

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Override
    public List<SelfDecl> getSelfDeclRfiData(String groupId) {
        List<SelfDecl> selfDeclList = applicationClient.getSelfDeclRfiData(groupId).getEntity();
        for (SelfDecl selfDecl : selfDeclList){
            String svcId = selfDecl.getSvcId();
            String svcName = HcsaServiceCacheHelper.getServiceNameById(svcId);
            selfDecl.setSvcName(svcName);


        }

        return null;
    }
}
