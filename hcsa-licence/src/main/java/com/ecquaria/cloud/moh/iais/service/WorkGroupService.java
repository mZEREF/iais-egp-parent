package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloud.client.rbac.UserConvertor;
import com.ecquaria.cloud.helper.EngineHelper;
import com.ecquaria.cloud.moh.iais.service.client.WorkGroupClient;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.cloudfeign.HystrixCommandExecutor;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import sop.rbac.user.User;
import sop.usergroup.UserGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * WorkGroupService
 *
 * @author suocheng
 * @date 11/19/2019
 */
@Service
@CacheConfig(
        cacheNames = {"rbac"}
)
@Slf4j
public class WorkGroupService  implements Serializable {
    private static final long serialVersionUID = -3326375591287041192L;

    @Autowired
    private WorkGroupClient workGroupClient;

    public WorkGroupService() {
    }

    public static WorkGroupService getInstance() {
        return (WorkGroupService)EngineHelper.getSpringContext().getBean(WorkGroupService.class);
    }

    @HystrixCommand(
            commandKey = "getUsersByGroupNo",
            ignoreExceptions = {FeignException.class}
    )
    public List<User> getUsersByGroupNo(final Long groupNo) throws FeignException {
        FeignResponseEntity<List<ClientUser>> result = workGroupClient.getUsersByGroupNo(groupNo);
        List<ClientUser> list = result == null ? null : (List)result.getEntity();
        if (list != null && list.size() > 0) {
            List<User> users = new ArrayList();
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
                ClientUser clientUser = (ClientUser)var5.next();
                users.add(UserConvertor.converToRbacUser(clientUser));
            }
            return users;
        } else {
            return null;
        }
    }

    @HystrixCommand(
            commandKey = "retrieveAgencyWorkingGroup",
            ignoreExceptions = {FeignException.class}
    )
    public List<UserGroup> retrieveAgencyWorkingGroup(@PathVariable("agencyShortName") final String agencyShortName) {
        try {
            return (List)((FeignResponseEntity)(new HystrixCommandExecutor<FeignResponseEntity<List<UserGroup>>>() {
                protected FeignResponseEntity<List<UserGroup>> doExecute() {
                    return WorkGroupService.this.workGroupClient.retrieveAgencyWorkingGroup(agencyShortName);
                }
            }).execute()).getEntity();
        } catch (FeignException var3) {
            log.error(var3.getMessage(), var3);
            return null;
        }
    }
}
