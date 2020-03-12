package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Stack;

/**
 * @author Wenkang
 * @date 2020/2/11 16:19
 */
@Delegator("congfigSeriviceDelegator")
@Log4j
public class ConfigServiceDelegator {
    @Autowired
    private ConfigService configService;
    @Autowired
    private OrganizationClient organizationClient;
    private OrgUserDto entity;
    public void start(BaseProcessClass bpc){
        log.info("*********startt***********");
      /*  LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        this.entity = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
        bpc.request.getSession().setAttribute("orgUserDto",entity);*/

    }
    private Stack stack=new Stack();
    public void switchOr(BaseProcessClass bpc){
    log.info("*********switchOr  start***********");

    log.info("*********switchOr  end***********");
    }
    public void prepare(BaseProcessClass bpc){
        log.info("*********prepare  start***********");

    }

    public void list(BaseProcessClass bpc){
        log.info("*********list  start***********");
        configService.getAllHcsaServices(bpc.request);

    }
    public void addNewService(BaseProcessClass bpc){
        log.info("*********addNewService  start***********");
        bpc.request.getSession().removeAttribute("routingStage");
        configService.addNewService(bpc.request);
    }
    public void saveOrUpdate(BaseProcessClass bpc){
        log.info("*********saveOrUpdate  start***********");

        configService.saveOrUpdate(bpc.request);
    }
    public void saveDate(BaseProcessClass bpc){
        log.info("*********saveDate  start***********");
        configService.saData(bpc.request);
    }
    public void editOrDelete(BaseProcessClass bpc){
        log.info("*********editOrDelete  start***********");

    }

    public void edit(BaseProcessClass bpc){
        log.info("*********edit  start***********");
        configService.viewPageInfo(bpc.request);
        StringBuffer requestURL = bpc.request.getRequestURL();
        String queryString = bpc.request.getQueryString();
        String s = requestURL.append(queryString).toString();
        stack.push(s);
    }

    public void editOrSave(BaseProcessClass bpc){
        log.info("*********editOrSave  start***********");
        String crud_action_value = bpc.request.getParameter("crud_action_type");
        if("save".equals(crud_action_value)){
            bpc.request.setAttribute("crud_action_type","save");
        }
        if("edit".equals(crud_action_value)){
            bpc.request.setAttribute("crud_action_type","edit");
        }


    }
    public void editView(BaseProcessClass bpc){
        log.info("*********editView  start***********");

        configService.viewPageInfo(bpc.request);
    }

    public void delete(BaseProcessClass bpc){

        log.info("*********delete  start***********");

        configService.delete(bpc.request);

    }

    public  void selectVersionAsNewTem(BaseProcessClass bpc){

        log.info("*********selectVersionAsNewTem  start***********");

    }
    public void deleteOrCancel(BaseProcessClass bpc){

        log.info("*********deleteOrCancel  start***********");
        configService.deleteOrCancel(bpc.request);

    }

    public void update(BaseProcessClass bpc){

        log.info("*********update  start***********");
        configService.update(bpc.request);

    }
}
