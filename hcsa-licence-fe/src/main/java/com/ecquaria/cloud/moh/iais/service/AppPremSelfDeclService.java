package com.ecquaria.cloud.moh.iais.service;

/*
 *author: yichen
 *date time:11/20/2019 1:55 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl;

import java.util.Date;
import java.util.List;


public interface AppPremSelfDeclService {

    List<SelfDecl> getSelfDeclByGroupId(String groupId);

    void saveSelfDeclAndInspectionDate(List<SelfDecl> selfDeclList, String groupId, Date inspStartDate, Date inspEndDate);

    Date getBlockPeriodByAfterApp(String groupId, List<SelfDecl> selfDeclList);

    void alertNotification();
}
