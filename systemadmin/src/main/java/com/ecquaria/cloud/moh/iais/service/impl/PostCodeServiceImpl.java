package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.dto.PostCodeDto;
import com.ecquaria.cloud.moh.iais.service.PostCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class PostCodeServiceImpl implements PostCodeService {

    @Override
    public PostCodeDto getPostCodeByCode(String postCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("searchField", "postalCode");
        map.put("filterValue", postCode);
        return RestApiUtil.getByReqParam("postcodes", map, PostCodeDto.class);
    }

    @Override
    public String savePostCode(PostCodeDto postCodet) {
//        PostCode postCode = new PostCode();
//        postCode.setAddressType(postCodet.getAddressType());
//        postCode.setBlkHseNo(postCodet.getBlkHseNo());
//        postCode.setBuildingName(postCodet.getBuildingName());
//        postCode.setPostalCode(postCodet.getPostalCode());
//        postCode.setStreetName(postCodet.getStreetName());
//        postCodeDao.saveAndFlush(postCode);
//        return postCode.getPostalCode();
        return "";
    }


    @Override
    public void createAll(List<PostCodeDto> list) {
        RestApiUtil.save("postcodes",list);
    }
}
