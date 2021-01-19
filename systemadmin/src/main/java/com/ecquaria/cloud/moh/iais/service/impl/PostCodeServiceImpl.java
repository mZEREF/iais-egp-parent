package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.service.PostCodeService;
import com.ecquaria.cloud.moh.iais.service.client.SystemClient;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j

public class PostCodeServiceImpl implements PostCodeService {

    @Autowired
    private SystemClient systemClient;

    @Override
    public void createAll(List<PostCodeDto> list) {
        systemClient.saveAllPostCode(list);
        //RestApiUtil.save("postcodes",list);
    }
}
