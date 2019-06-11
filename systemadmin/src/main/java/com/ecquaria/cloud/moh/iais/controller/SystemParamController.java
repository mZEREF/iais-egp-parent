package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.dto.SystemParamDTO;
import com.ecquaria.cloud.moh.iais.service.SystemParamService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    @author yichen_guo@ecquaria.com

 */

@Delegator(value = "systemParamController")
public class SystemParamController {

    @Autowired
    private SystemParamService service;

    public void view(HttpServletRequest request){
        List<Map<String, String>> list = new ArrayList<>();
        service.listSystemParam().forEach(i -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", i.getId());
            map.put("value", i.getValue());
            map.put("description", i.getDescription());
            list.add(map);
        });
    }


    public void updateParamByPkId(HttpServletRequest request){
        SystemParamDTO dto = (SystemParamDTO)request.getAttribute("systemParam");
        if(dto != null){
            if(dto.getId() != null && dto.getId().length() != 0){
                service.updateParamByPkId(dto.getId(), dto.getValue());
            }
        }

    }


    /*public void insertRecord(HttpServletRequest request){
        service.insertRecord(sys);
    }*/
}
