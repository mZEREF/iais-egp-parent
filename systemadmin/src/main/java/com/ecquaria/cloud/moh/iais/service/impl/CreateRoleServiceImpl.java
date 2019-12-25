package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.service.CreateRoleService;
import com.ecquaria.cloud.moh.iais.service.client.CreateRoleClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/12/24 15:29
 */
@Service
@Slf4j
public class CreateRoleServiceImpl implements CreateRoleService {
    @Autowired
    private CreateRoleClient createRoleClient;
    @Override
    public List<Object> findAllRoles(){

        List<Object> entity = createRoleClient.findAllRoles().getEntity();
         return entity;
    }
}
