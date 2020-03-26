package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclaration;

import java.util.List;

/**
 * @author: yichen
 * @description:
 * @date:2020/3/24
 **/

public interface SelfDeclRfiService {
    List<SelfDeclaration> getSelfDeclRfiData(String groupId);
}
