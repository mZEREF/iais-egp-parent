package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.CreateRoleService;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Wenkang
 * @date 2019/12/23 15:40
 */
@Delegator(value = "createRoleDelegator")
@Slf4j
public class CreateRoleDelegator {

    private CreateRoleService createRoleService;

    public void prepareData(BaseProcessClass bpc){
        log.info(" the  ****   role   ***   ");
        createRoleService.findAllRoles();

        log.info("the ** *   role   ***   end  ");
    }

    public void  createRole(BaseProcessClass bpc){
    log.info("*****   create   ***  role   start ");

        String str  = (String)ParamUtil.getSessionAttr(bpc.request, "crud_action_type");
        if(StringUtil.isEmpty(str)){
            return;
        }

    }

    public void doSave(BaseProcessClass bpc){

        log.info("**** start ***  save");


    }
}
