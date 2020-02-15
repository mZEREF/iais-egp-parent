package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Wenkang
 * @date 2020/2/11 16:19
 */
@Delegator("congfigSeriviceDelegator")
@Log4j
public class ConfigServiceDelegator {
    @Autowired
    private ConfigService configService;
    public void start(BaseProcessClass bpc){
        log.info("*********startt***********");
    }

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
        configService.addNewService(bpc.request);
    }
    public void saveOrUpdate(BaseProcessClass bpc){
        log.info("*********saveOrUpdate  start***********");

        configService.saveOrUpdate(bpc.request);
    }
    public void saveDate(BaseProcessClass bpc){
        log.info("*********saveDate  start***********");
    }
    public void editOrDelete(BaseProcessClass bpc){
        log.info("*********editOrDelete  start***********");
    }

    public void edit(BaseProcessClass bpc){
        log.info("*********edit  start***********");
        configService.viewPageInfo(bpc.request);

    }

    public void editOrSave(BaseProcessClass bpc){
        log.info("*********editOrSave  start***********");

    }
    public void editView(BaseProcessClass bpc){
        log.info("*********editView  start***********");

    }

    public void delete(BaseProcessClass bpc){
        log.info("*********delete  start***********");
    }

    public void deleteOrCancel(BaseProcessClass bpc){
        log.info("*********deleteOrCancel  start***********");
    }
}
