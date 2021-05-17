package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;

/**
 * @Description manage licnesee and user
 * @Auther chenlei on 5/17/2021.
 */
public interface HalpUserService {

    FeUserDto syncFeUserFromBe(FeUserDto feUserDto);

}
