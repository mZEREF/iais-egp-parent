package com.ecquaria.cloud.moh.iais.service;

/*
 *author: yichen
 *date time:11/20/2019 1:55 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclSubmitDto;


public interface AppPremSelfDeclService {

    SelfDeclSubmitDto getSelfDeclByGroupId(String groupId);

    void saveSelfDecl(SelfDeclSubmitDto selfDeclSubmitDto);

    Boolean hasSelfDeclRecord(String groupId);
}
