package com.ecquaria.cloud.moh.iais.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2019/12/5 15:51
 */
@Controller

public class NewApplicationAjaxController {
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public @ResponseBody Map<String,String> doVail(HttpServletRequest request, HttpServletResponse response){
        List<String> subList=new ArrayList<>();
        List<String> sumList=new ArrayList<>();
        List<String> idList=new ArrayList<>();
        List<String> parentId=new ArrayList<>();
        Map<String,String> map=new HashMap<>();
        String[] parameterValues = request.getParameterValues("control--runtime--1");
        for(String every:parameterValues){
         if(every.contains("true")){
             subList.add(every);
         }else if(every.contains("false")){
             sumList.add(every);
         }
        }
        for(String every:subList){
            String[] split = every.split(";");
            if(split.length==3){
                idList.add(split[0]);
            }else if(split.length==4){
                parentId.add(every);
            }
        }
        if(idList.isEmpty()&&!parentId.isEmpty()){
            map.put("error","error massage!");
        }
        for(String every :parentId){
            recursion(every,parentId);
        }

        return map;
    }

    private void recursion(String ids,List<String> parentId){
       for(int i=0;i<parentId.size();  i++){
           String[] split = parentId.get(i).split(";");
            if(split[3].equals(ids)){
                recursion(split[0],parentId);
            }else {
                return;
            }
       }
    }

}
