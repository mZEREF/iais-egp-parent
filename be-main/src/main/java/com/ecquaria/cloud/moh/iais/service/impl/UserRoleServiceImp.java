package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.service.UserRoleService;
import com.ecquaria.cloud.moh.iais.service.client.UserRoleClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class UserRoleServiceImp implements UserRoleService {

    @Autowired
    private UserRoleClient userRoleClient;
    @Override
    public String getAvailable(String userId){

        return userRoleClient.getAvailable(userId).getEntity();
    }

    @Override
    public void setAvailable(String userId,String ava){
        List<String> user = new ArrayList<>();
        user.add(0,userId);
        user.add(1,ava);
        userRoleClient.setAvailable(user);
    }

}
