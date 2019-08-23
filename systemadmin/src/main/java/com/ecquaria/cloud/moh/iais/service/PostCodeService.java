package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dto.PostCodeDto;

import java.util.List;

public interface PostCodeService {
    public PostCodeDto getPostCodeByCode(String postCode);
    public String savePostCode(PostCodeDto postCode);
    public void createAll(List<PostCodeDto> list);
}
