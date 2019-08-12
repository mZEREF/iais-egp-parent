package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.dao.PostCodeDao;
import com.ecquaria.cloud.moh.iais.dto.PostCodeDto;
import com.ecquaria.cloud.moh.iais.entity.PostCode;
import com.ecquaria.cloud.moh.iais.service.PostCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class PostCodeServiceImpl implements PostCodeService {

    @Autowired
    private PostCodeDao postCodeDao;

    @Override
    public PostCode getPostCodeByCode(String postCode) {
        return postCodeDao.getPostCodeByCode(postCode);
    }

    @Override
    public String savePostCode(PostCodeDto postCodet) {
        PostCode postCode = new PostCode();
        postCode.setAddressType(postCodet.getAddressType());
        postCode.setBlkHseNo(postCodet.getBlkHseNo());
        postCode.setBuildingName(postCodet.getBuildingName());
        postCode.setPostalCode(postCodet.getPostalCode());
        postCode.setStreetName(postCodet.getStreetName());
        postCodeDao.saveAndFlush(postCode);
        return postCode.getPostalCode();
    }

    @Override
    public void clean() {
        postCodeDao.deleteAll();
    }

    @Override
    public void createAll(List list) {
        postCodeDao.save(list);
    }
}
