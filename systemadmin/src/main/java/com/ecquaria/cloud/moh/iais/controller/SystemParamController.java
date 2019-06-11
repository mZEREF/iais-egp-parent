package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.entity.SystemParam;
import com.ecquaria.cloud.moh.iais.service.SystemParamService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
    @author yichen_guo@ecquaria.com

 */

@Delegator(value = "systemParamController")
public class SystemParamController {

    @Autowired
    private SystemParamService service;

    public void view(HttpServletRequest request){
        List<SystemParam> systemParamList = service.listSystemParam();
        request.setAttribute("systemParamList", systemParamList);
    }

    public void updateValueByGuid(HttpServletRequest request){
        SystemParam systemParam = (SystemParam)request.getAttribute("systemParam");
        if(systemParam != null){
            service.updateValueByGuid(systemParam.getRowguid(), systemParam.getValue());
        }

    }


    public void insertRecord(HttpServletRequest request){
        SystemParam systemParam = (SystemParam) request.getAttribute("systemParam");
        if(systemParam != null){
            service.insertRecord(systemParam);
        }

    }
}
