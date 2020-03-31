package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclSubmitDto;

/**
 * @author: yichen
 * @description:
 * @date:2020/3/24
 **/

public interface SelfDeclRfiService {
    SelfDeclSubmitDto getSelfDeclRfiData(String groupId);
}
