package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.entity.sopproject.SOPProjectService;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.service.PostCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class PostCodeServiceImpl implements PostCodeService {

    @Override
    public void createAll(List<PostCodeDto> list) {
        //RestApiUtil.save("postcodes",list);
    }
}
