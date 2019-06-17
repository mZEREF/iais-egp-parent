package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dto.PostCodeDto;
import com.ecquaria.cloud.moh.iais.entity.PostCode;

import java.util.List;

public interface PostCodeService {
    public PostCode getPostCodeByCode(String postCode);
    public String savePostCode(PostCodeDto postCode);
    public void clean();
    public void createAll(List list);
}
