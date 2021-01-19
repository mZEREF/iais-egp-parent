package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;

import java.util.List;

public interface PostCodeService {
    public void createAll(List<PostCodeDto> list);
}
