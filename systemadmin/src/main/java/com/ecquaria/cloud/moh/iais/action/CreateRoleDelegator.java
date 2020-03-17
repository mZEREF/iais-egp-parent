package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.CreateRoleService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Wenkang
 * @date 2019/12/23 15:40
 */
@Delegator(value = "createRoleDelegator")
@Slf4j
public class CreateRoleDelegator {
    @Autowired
    private CreateRoleService createRoleService;

    public void prepareData(BaseProcessClass bpc){
        log.info(" the  ****   role   ***   ");

        log.info("the ** *   role   ***   end  ");
    }

    public void  createRole(BaseProcessClass bpc){
    log.info("*****   create   ***  role   start ");
    ParamUtil.getSessionAttr(bpc.request,"");


    }
    /*
    * editRole
    * */
    public void editRole(BaseProcessClass bpc){

        log.info("**** start ***  saeditRoleve");
        String crud_action_type = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if(!StringUtil.isEmpty(crud_action_type)){

        }

    }

    public void step(BaseProcessClass bpc){

        log.info("**** start ***  save");
        Map<String,String> errorMap= IaisCommonUtils.genNewHashMap();
        String type = ParamUtil.getRequestString(bpc.request, "crud_action_type");
        if(!StringUtil.isEmpty(type)){
            if("saveRole".equals(type)){
                String tag = bpc.request.getParameter("roleTag");
                String remakes = bpc.request.getParameter("remakes");
                String roleName = bpc.request.getParameter("roleName");
                String roleID = bpc.request.getParameter("roleID");

            }
        }

    }
/**
 *
 * */
        private void doValidate(Map<String,String> errorMap, HttpServletRequest request){

        }

    public void viewRole(BaseProcessClass bpc){

        log.info("**** start ***  view ");
        String crud_action_type = bpc.request.getParameter("crud_action_type");
        if(!StringUtil.isEmpty(crud_action_type)){

        }

        log.info("**** end ***  view ");

    }

    public void searchRole(BaseProcessClass bpc){

        log.info("**** start ***  save");

    }

    public void exportRole(BaseProcessClass bpc){

        log.info("**** start ***  save");

    }

    public void deleteRole(BaseProcessClass bpc){

        log.info("**** start ***  save");

    }


    public void importRole(BaseProcessClass bpc){

        log.info("**** start ***  save");

    }
}
