package com.ecquaria.cloud.moh.iais.service;

/*
 *author: yichen
 *date time:11/20/2019 1:55 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclaration;

import java.util.List;


public interface AppPremSelfDeclService {

    List<SelfDeclaration> getSelfDeclByGroupId(String groupId);

    void saveSelfDecl(List<SelfDeclaration> selfDeclList);

    Boolean hasSelfDeclRecord(String groupId);
}
