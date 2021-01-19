package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.UserRoleService;
import com.ecquaria.cloud.moh.iais.service.client.UserRoleClient;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
        List<String> user = IaisCommonUtils.genNewArrayList();
        user.add(0,userId);
        user.add(1,ava);
        userRoleClient.setAvailable(user);
    }

}
